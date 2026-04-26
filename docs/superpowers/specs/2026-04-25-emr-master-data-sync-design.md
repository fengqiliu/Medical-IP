# EMR 主数据同步 — 设计规格

> **项目名称：** 医技影像平台  
> **设计日期：** 2026-04-25  
> **阶段定位：** 接入层二阶段

---

## 1. 背景与目标

RIS / PACS / EMR 接入骨架已经补齐，但当前仍停留在“适配器可注入、标准化可执行、同步日志可记录”的阶段。下一步最有价值的落地，是先让 EMR 主数据真正进入平台数据库。

本次目标是把 EMR 的患者与就诊数据跑通一条真实同步链路：

- `EMRAdapter` 负责取数
- `DataStandardizer` 负责字段标准化
- `SyncOrchestratorService` 负责调用、持久化与记录同步结果
- 标准化后的数据写入现有 `patient`、`encounter` 表

本次不扩展前端页面，也不同时推进 RIS/PACS 真实联调。

---

## 2. 本次范围

### 2.1 必做

- 为 `EMRAdapter` 增加真实的 HTTP 取数实现
- 为 EMR 患者、就诊数据补齐更明确的字段映射与类型转换
- 在同步编排层增加 EMR 主数据落表逻辑
- 保证重复同步不会无限重复插入
- 在 `DataSyncLog` 中记录同步成功、失败和同步数量
- 为 EMR 主数据同步补单元测试

### 2.2 明确不做

- RIS / PACS 真实数据同步
- 复杂主索引合并算法
- 前端监控台或手工触发页面
- 同步重试队列和人工干预队列
- 复杂的差异比对和审计报告

---

## 3. 设计原则

- **主数据优先。** 先把患者与就诊主数据同步稳定，再让检验/影像数据建立在它之上。
- **最小写入闭环。** 只做 `patient`、`encounter` 的最小可用 upsert，不扩大到更多业务表。
- **幂等优先。** 同步重复执行时，不应产生明显重复数据。
- **保留现有分层。** 适配器、标准化、编排、日志四层边界保持不变。
- **失败可见。** 单条记录或单次批次失败都要反映到同步日志中。

---

## 4. 目标结构

### 4.1 EMRAdapter

`EMRAdapter` 从纯占位实现升级为真实接入实现：

- 读取 `emr-service.base-url`
- 调用类似 `/api/patients`、`/api/encounters` 的 EMR 接口
- 支持 `lastSyncTime` 作为增量同步参数
- `fetchPatients` / `fetchEncounters` 返回原始 Map 列表

其他 fetch 方法仍可保持空结果，因为这次范围只覆盖主数据。

### 4.2 标准化层

`FieldMappingService` 与 `DataStandardizer` 继续保留，但 EMR 映射要更明确：

- `PATIENT_ID`、`UNIFIED_PATIENT_ID`、`NAME`、`GENDER`、`BIRTH_DATE`
- `ENCOUNTER_ID`、`PATIENT_ID`、`ENCOUNTER_TYPE`、`DEPARTMENT_ID`、`VISIT_TIME`

其中：

- `birthDate` 需要支持日期转换
- `visitDatetime` 需要支持日期时间转换
- 未映射字段不进入本次持久化逻辑

### 4.3 编排与落表

`SyncOrchestratorService` 新增 EMR 主数据持久化分支：

1. 拉取患者、就诊原始数据
2. 标准化为统一字段
3. 先同步患者
4. 再同步就诊
5. 汇总成功数量与失败信息
6. 写入 `DataSyncLog`

---

## 5. 持久化策略

### 5.1 Patient

患者同步按以下顺序判断是否已存在：

1. 优先用 `unifiedPatientId`
2. 若没有 `unifiedPatientId`，回退到外部 `id`

写入策略：

- 已存在：更新基础字段（姓名、性别、生日、身份证号等）
- 不存在：插入新患者

### 5.2 Encounter

就诊同步按外部 `id` 判断是否已存在。

写入策略：

- 已存在：更新基础字段
- 不存在：插入新就诊

约束：

- 若引用的患者尚未同步成功，则该条就诊记录记为失败并继续处理后续记录
- 不在本次引入自动补偿机制

---

## 6. 数据流

本次目标数据流如下：

`Scheduler -> SyncOrchestratorService -> EMRAdapter -> DataStandardizer -> Patient upsert -> Encounter upsert -> DataSyncLog`

### 6.1 批次成功

当患者与就诊均同步成功时：

- 更新 `syncedCount`
- 记录 `SUCCESS`
- 保留本次 `lastSyncTime`

### 6.2 部分失败

当批次中出现部分记录失败时：

- 不中断整批
- 继续后续记录处理
- `DataSyncLog` 记录 `FAILED` 或 `PARTIAL_FAILED`
- `errorMessage` 汇总关键错误

本次推荐使用 `FAILED` 表示任意失败，避免引入新的状态枚举传播成本。

---

## 7. 错误处理

- HTTP 调用失败：本次 source 直接记失败
- 单条患者记录转换失败：跳过该条并记录错误摘要
- 单条就诊记录缺少患者归属：跳过该条并记录错误摘要
- 数据格式异常：标准化阶段尽量返回空值，由持久化阶段做最小校验

错误处理策略以“继续同步剩余记录”为主，而不是整批回滚。

---

## 8. 测试策略

至少补以下测试：

1. `EMRAdapter` 取数测试或 HTTP 调用封装测试
2. `DataStandardizer` 的 EMR 字段映射测试
3. `SyncOrchestratorService` 的 EMR 患者/就诊落表测试
4. 重复同步时不重复插入的幂等测试
5. 遇到坏记录时仍继续处理后续记录的测试

测试重点放在：

- 字段映射是否正确
- 写入顺序是否正确
- 幂等行为是否成立
- 错误是否会进入同步日志

---

## 9. 验收标准

本次完成后，应满足以下条件：

1. `EMRAdapter` 能从配置地址拉取患者和就诊数据
2. 标准化后的患者数据能写入 `patient`
3. 标准化后的就诊数据能写入 `encounter`
4. 同步重复执行不会明显重复插入同一患者和就诊
5. 同步日志能反映成功或失败及同步数量
6. 后端测试与打包继续通过

---

## 10. 后续演进

完成 EMR 主数据同步后，下一步再按顺序推进：

1. 接 RIS 真实影像检查数据
2. 接 PACS 报告或影像引用数据
3. 增加同步统计、重试和人工干预能力
4. 在前端补接入监控或管理页面

本设计故意只覆盖 EMR 主数据闭环，不把后续接入并入同一次实现。
