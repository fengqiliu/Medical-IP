# 医技影像平台

临床医技360°全景系统 — 整合医院 LIS、RIS、PACS、EMR 等业务系统的患者检验与影像数据，提供统一数据查看和 AI 辅助摘要。

## 系统架构

```
┌──────────────────┐     ┌──────────────────┐     ┌──────────────────┐
│   前端 (React)    │────▶│  后端 (Spring Boot)│────▶│  AI 服务 (FastAPI) │
│   端口 :3000      │     │   端口 :8080       │     │   端口 :8000       │
└──────────────────┘     └──────────────────┘     └──────────────────┘
                                    │
                          ┌─────────┼─────────┐
                          ▼         ▼         ▼
                       ┌─────┐  ┌─────┐  ┌──────┐
                       │PostgreSQL│  │Redis │  │外部系统│
                       │ 主数据库  │  │ 缓存  │  │LIS/RIS/PACS/EMR│
                       └─────┘  └─────┘  └──────┘
```

## 功能特性

**Phase 1 — 临床医技核心**
- **患者360视图**：整合患者所有检验和影像数据，一站式查看
- **时间线展示**：按时间轴展示患者历次就诊、检验、影像检查事件
- **检验报告查看**：检验项目结果展示，含参考值对比和异常标识
- **影像报告查看**：影像检查报告查看，含检查所见与诊断意见
- **AI 辅助摘要**：基于 LLM 自动生成检验结果趋势摘要和影像报告要点
- **数据权限控制**：基于角色的行级数据访问控制
- **访问审计**：完整的操作审计日志，记录所有数据访问

**Phase 2 — 系统管理**
- **用户管理**：系统用户创建、编辑、启用/禁用、密码重置
- **角色管理**：角色创建与编辑，支持菜单级权限分配
- **部门管理**：医院部门/科室维护
- **岗位管理**：岗位/职位维护
- **审计日志**：访问日志查询与筛选

## 技术栈

| 层级     | 技术                                                            |
| -------- | --------------------------------------------------------------- |
| 前端     | React 18, TypeScript 5.3, Vite 5, Ant Design 5, Zustand 4        |
| 后端     | Spring Boot 3.2.3, Java 17, Spring Security, MyBatis-Plus 3.5.5 |
| AI 服务  | FastAPI, LangChain, OpenAI GPT-3.5-turbo, Pydantic v2            |
| 数据库   | PostgreSQL 14+, Redis 6+                                         |
| 认证     | JWT (jjwt 0.12.5), 24小时过期，行级数据权限                        |

## 快速开始

### 环境要求

- Node.js 18+
- Java 17+
- Python 3.10+
- PostgreSQL 14+
- Redis 6+

### 1. 初始化数据库

```bash
# 创建数据库
createdb medical360

# 执行初始化脚本
psql -d medical360 -f backend/src/main/resources/sql/init.sql
```

### 2. 启动后端

```bash
cd backend

# 编辑 src/main/resources/application.yml 配置数据库连接

mvn clean package
java -jar target/medical-360-backend-1.0.0.jar
# 或者: mvn spring-boot:run
```

后端启动于 http://localhost:8080

默认测试用户：`doctor1` / `123456`

### 3. 启动 AI 服务

```bash
cd ai-service

cp .env.example .env
# 编辑 .env 填入 OPENAI_API_KEY

pip install -r requirements.txt
python -m app.main
```

AI 服务启动于 http://localhost:8000

### 4. 启动前端

```bash
cd frontend

npm install
npm run dev
```

前端启动于 http://localhost:3000

## 项目结构

```
medical-360/
├── frontend/
│   ├── src/
│   │   ├── api/              # API 请求层 (axios 实例 + 各模块接口)
│   │   ├── components/       # 可复用组件
│   │   │   └── Layout/       #   应用布局 (AppLayout: Sidebar + Header)
│   │   ├── pages/            # 页面组件
│   │   │   ├── Login/        #   登录页
│   │   │   ├── Dashboard/    #   工作台仪表盘
│   │   │   ├── Patient360/   #   患者360视图
│   │   │   ├── Timeline/     #   患者时间线
│   │   │   ├── LabDetail/    #   检验详情
│   │   │   ├── ImagingDetail/#   影像详情
│   │   │   └── System/       #   系统管理 (Phase 2)
│   │   ├── stores/           # Zustand 状态管理 (authStore)
│   │   └── types/            # TypeScript 类型定义
│   ├── vite.config.ts        # Vite 配置 (proxy /api → :8080)
│   └── package.json
│
├── backend/
│   ├── src/main/java/com/medical360/
│   │   ├── common/           # Result<T> 统一响应, PageResult
│   │   ├── config/           # Spring 配置 (SecurityFilterChain, CORS, RestTemplate)
│   │   ├── controller/       # REST 控制器 (Auth, Patient, Timeline, System)
│   │   ├── dto/              # 数据传输对象
│   │   ├── entity/           # 数据库实体 (MyBatis-Plus 注解)
│   │   ├── handler/          # GlobalExceptionHandler 全局异常处理
│   │   ├── integration/      # 外部系统接入层
│   │   │   ├── adapter/      #   数据源适配器 (LIS/RIS/PACS/EMR)
│   │   │   ├── mapper/       #   数据标准化 (FieldMappingService + DataStandardizer)
│   │   │   └── sync/         #   同步编排 (SyncOrchestratorService + DataSyncScheduler)
│   │   ├── mapper/           # MyBatis Mapper 接口
│   │   ├── security/         # JWT 认证 (JwtTokenProvider, JwtAuthenticationFilter)
│   │   │                    #   数据权限 (DataPermissionInterceptor, PermissionService)
│   │   └── service/          # 业务服务层
│   └── pom.xml
│
└── ai-service/
    ├── app/
    │   ├── models/           # Pydantic 请求模型 (LabSummaryRequest, ImagingSummaryRequest)
    │   ├── routers/          # FastAPI 路由 (lab/summary, imaging/summary)
    │   └── services/         # LLM 服务 (ChatOpenAI 调用, Prompt 构建)
    ├── config.py             # 环境变量读取 (OPENAI_API_KEY 等)
    └── requirements.txt
```

## API 接口

### 后端 API (端口 8080)

#### 认证
| 方法   | 路径              | 说明             |
| ------ | ----------------- | ---------------- |
| POST   | `/api/auth/login` | 用户登录，返回 JWT |
| GET    | `/api/auth/current` | 获取当前用户信息 |
| POST   | `/api/auth/logout`  | 登出             |

#### 临床数据
| 方法   | 路径                          | 说明             |
| ------ | ----------------------------- | ---------------- |
| GET    | `/api/patient/360/{patientId}` | 患者360视图      |
| GET    | `/api/timeline/{patientId}`    | 患者时间线       |

#### 系统管理 (Phase 2)
| 方法     | 路径                              | 说明             |
| -------- | --------------------------------- | ---------------- |
| GET/POST | `/api/system/users`               | 用户列表 / 创建   |
| GET/PUT/DELETE | `/api/system/users/{id}`      | 用户详情/更新/删除 |
| PUT      | `/api/system/users/{id}/password`  | 重置密码          |
| PUT      | `/api/system/users/{id}/enabled`   | 启用/禁用用户     |
| GET      | `/api/system/users/all`            | 全部用户（下拉选择）|
| GET/POST | `/api/system/roles`               | 角色列表 / 创建   |
| GET/PUT/DELETE | `/api/system/roles/{id}`      | 角色详情/更新/删除 |
| GET/POST | `/api/system/departments`         | 部门列表 / 创建   |
| GET/PUT/DELETE | `/api/system/departments/{id}` | 部门详情/更新/删除 |
| GET/POST | `/api/system/positions`           | 岗位列表 / 创建   |
| GET/PUT/DELETE | `/api/system/positions/{id}`  | 岗位详情/更新/删除 |
| GET      | `/api/system/access-logs`         | 访问日志查询      |

### AI 服务 API (端口 8000)

| 方法   | 路径                  | 说明               |
| ------ | --------------------- | ------------------ |
| POST   | `/lab/summary`        | 生成检验结果 AI 摘要 |
| POST   | `/imaging/summary`    | 生成影像报告 AI 摘要 |
| GET    | `/health`             | 健康检查            |

## 前端路由

| 路径                     | 页面           | 权限    |
| ------------------------ | -------------- | ------- |
| `/login`                 | 登录页         | 公开    |
| `/dashboard`             | 工作台仪表盘   | 需登录  |
| `/patient360/:patientId` | 患者360视图    | 需登录  |
| `/timeline/:patientId`   | 患者时间线     | 需登录  |
| `/lab/:orderId`          | 检验详情       | 需登录  |
| `/imaging/:orderId`      | 影像详情       | 需登录  |
| `/system/*`              | 系统管理       | 管理员  |

## 认证与数据权限

### 认证流程

1. 前端登录获取 JWT → 存储于 localStorage
2. Axios 拦截器自动在请求头注入 `Authorization: Bearer <token>`
3. `JwtAuthenticationFilter` 验证 token，设置 Spring SecurityContext
4. JWT 有效期为 24 小时

### 数据权限

- `DataPermissionInterceptor` 拦截 `/api/patient/*` 路径
- 基于 SecurityContext 中的用户身份校验患者数据访问权限
- 未经授权的访问返回 `403 Forbidden`
- 所有数据访问异步记录至 `access_log` 表

## 统一响应格式

所有后端 API 返回 `Result<T>` 结构：

```json
{
  "code": 200,
  "message": "success",
  "data": { ... }
}
```

错误时 `code` 为对应错误码，`message` 包含错误描述。

## 环境变量

### 后端 (application.yml)

通过 Spring Boot 配置文件或环境变量设置：
- `spring.datasource.*` — PostgreSQL 连接信息
- `spring.data.redis.*` — Redis 连接信息
- `JWT_SECRET` — JWT 签名密钥
- `AI_SERVICE_URL` — AI 服务地址 (默认 `http://localhost:8000`)
- `LIS_URL`, `RIS_URL`, `PACS_URL`, `EMR_URL` — 外部系统地址

### AI 服务 (.env)

```env
OPENAI_API_KEY=your-api-key-here
OPENAI_BASE_URL=https://api.openai.com/v1
MODEL_NAME=gpt-3.5-turbo
```

### 前端 (.env.local)

```env
VITE_API_BASE_URL=/api
```

## 数据模型

| 表名              | 说明           |
| ----------------- | -------------- |
| `patient`         | 患者基本信息   |
| `encounter`       | 就诊记录       |
| `lab_order`       | 检验医嘱       |
| `lab_result`      | 检验结果明细   |
| `imaging_order`   | 影像检查医嘱   |
| `imaging_report`  | 影像报告       |
| `clinical_event`  | 临床事件时间线 |
| `department`      | 部门/科室      |
| `position`        | 岗位/职位      |
| `users`           | 系统用户       |
| `role`            | 角色           |
| `user_role`       | 用户-角色关联  |
| `role_menu`       | 角色-菜单权限  |
| `access_log`      | 访问审计日志   |
| `data_sync_log`   | 数据同步日志   |

## 外部系统接入

后端通过 `integration/` 层对接医院现有业务系统：

```
外部系统 (LIS/RIS/PACS/EMR)
       │
       ▼
DataSourceAdapter (接口)
       │
       ▼
FieldMappingService → DataStandardizer (数据标准化)
       │
       ▼
SyncOrchestratorService (同步编排) → DataSyncScheduler (定时触发)
       │
       ▼
本地数据表 (patient, lab_order, imaging_order 等)
```

各适配器实现 `DataSourceAdapter` 接口，统一从 `fetchPatients()`、`fetchLabOrders()`、`fetchImagingReports()` 等方法拉取增量数据。

## License

MIT
