# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

医技影像平台 (Clinical Medical 360° Panorama Platform) — 整合患者检验、影像等医技数据，提供统一数据查看和 AI 辅助摘要。

## 仓库结构

```
frontend/                      # React 18 + TypeScript + Vite
backend/                       # Spring Boot 3.2.3 + Java 17
ai-service/                    # FastAPI + LangChain AI 摘要服务
docs/                          # 设计规格与实施计划
```

**注意**: main 分支使用 sparse-checkout 仅包含 Phase 2 系统管理模块。完整代码在 `feature/integration-skeleton` 分支。

## 开发命令

### 后端 (backend/)

```bash
cd backend
mvn clean package                  # 构建 JAR → target/medical-360-backend-1.0.0.jar
mvn spring-boot:run                # 运行 (端口 8080)
mvn test                           # 运行全部测试
mvn test -Dtest=PatientServiceTest # 运行单个测试类
mvn test -Dtest=PatientServiceTest#testMethod  # 运行单个测试方法

# 环境依赖：PostgreSQL 14+ (localhost:5432/medical360), Redis 6+
# 初始化 SQL：backend/src/main/resources/sql/init.sql
# 默认测试用户：doctor1 / 123456
```

### 前端 (frontend/)

```bash
cd frontend
npm install                        # 安装依赖
npm run dev                        # Vite dev server (端口 3000, proxy /api → :8080)
npm run build                      # 生产构建 (tsc + vite build)
npm run preview                    # 预览生产构建
```

### AI 服务 (ai-service/)

```bash
cd ai-service
cp .env.example .env               # 编辑填入 OPENAI_API_KEY
pip install -r requirements.txt
python -m app.main                 # 启动 (端口 8000)
# 无认证保护，仅内部网络访问
```

## 技术栈

| 层级   | 关键技术                                                                    |
| ------ | --------------------------------------------------------------------------- |
| 前端   | React 18, TypeScript 5.3 (strict), Vite 5, Ant Design 5, Zustand 4, Axios   |
| 后端   | Spring Boot 3.2.3, Java 17, Spring Security, MyBatis-Plus 3.5.5, JWT (jjwt) |
| 数据库 | PostgreSQL, Redis                                                           |
| AI     | FastAPI, LangChain, OpenAI GPT-3.5-turbo, Pydantic v2                       |

## 核心架构

### 后端分层

```
Controller → Service (接口) → ServiceImpl → MyBatis Mapper → PostgreSQL
     ↓
Result<T> 统一响应封装 (code, message, data)

SecurityFilterChain:
  JwtAuthenticationFilter → DataPermissionInterceptor → Controller
```

- `controller/` — REST 控制器，返回 `Result<T>`
- `service/` + `service/impl/` — 业务逻辑，接口与实现分离
- `mapper/` — MyBatis-Plus `BaseMapper` 接口，优先用内置 query 方法再手写 SQL
- `entity/` — 数据库实体，`@TableName` + `@TableId` 注解
- `dto/` — API 请求/响应 DTO，使用 `@Builder` + `@Data`
- `common/` — `Result<T>` 和 `PageResult` 统一响应
- `security/` — JWT 认证 (`JwtTokenProvider`, `JwtAuthenticationFilter`) + 数据权限 (`DataPermissionInterceptor`, `PermissionService`)
- `integration/` — 外部数据源接入层：
  - `adapter/` — `DataSourceAdapter` 接口 + LIS/RIS/PACS/EMR 适配器实现
  - `mapper/` — `DataStandardizer` + `FieldMappingService` 数据标准化
  - `sync/` — `SyncOrchestratorService` 编排 + `DataSyncScheduler` 定时同步
- `handler/` — `GlobalExceptionHandler` 全局异常处理
- `config/` — Spring 配置类 (CORS, Security, RestTemplate)

### 前端分层

```
main.tsx (BrowserRouter + ConfigProvider)
  └── App.tsx (路由定义, PublicRoute/PrivateRoute 守卫)
      └── AppLayout (Ant Design Layout: Sidebar + Header + Content/Outlet)
          ├── pages/Dashboard/
          ├── pages/Patient360/:patientId
          ├── pages/Timeline/:patientId
          ├── pages/LabDetail/:orderId
          └── pages/ImagingDetail/:orderId
```

- `api/` — Axios 实例 (`@/api/index.ts`)：baseURL `/api`, JWT 拦截器, 401 重定向
- `stores/` — Zustand store (`authStore.ts`)：token/user 持久化到 localStorage
- `pages/` — 页面组件，每页面一个 `*/index.tsx`
- `components/` — 可复用组件 (PatientHeader, Timeline, AISummaryCard, LabResultTable, ImagingReportCard, AbnormalTag)
- `types/` — TypeScript 类型定义，按域拆分
- 路径别名 `@/*` → `src/*` (tsconfig.json + vite.config.ts 同步配置)

### API 约定

所有后端端点返回 `Result<T>`：

```json
{ "code": 200, "message": "success", "data": { ... } }
```

前端 Axios 拦截器自动从 `response.data.data` 提取数据。
错误通过 `Result.error("ERROR_CODE")` 返回，由 `GlobalExceptionHandler` 统一捕获包装。

### 认证与权限

1. 前端登录 → `POST /api/auth/login` → 后端验证 → 返回 JWT (24小时过期)
2. JWT 存储于 localStorage，Axios 拦截器注入 `Authorization: Bearer <token>`
3. `JwtAuthenticationFilter` 解析 token，设置 SecurityContext (username + roles)
4. `DataPermissionInterceptor` 拦截 `/api/patient/*` 路径，校验行级权限
5. `AccessLogService` (异步) 记录所有数据访问事件到 `access_log` 表

## 数据模型 (14 张表)

`patient`, `encounter`, `lab_order`, `lab_result`, `imaging_order`, `imaging_report`, `clinical_event`, `department`, `position`, `users`, `role`, `user_role`, `role_menu`, `access_log`, `data_sync_log`

## 环境变量

- 后端: `application.yml` (数据源、Redis、JWT_SECRET、外部服务 URL)
- 前端: `frontend/.env.example` → `.env.local`
- AI: `ai-service/.env.example` → `.env` (OPENAI_API_KEY, OPENAI_BASE_URL, MODEL_NAME)

## 可用 Claude Code Agents

- **backend-dev** — Spring Boot 后端开发 (Controller→Service→Mapper 分层，JWT 集成，DTO 设计)
- **frontend-dev** — React/TypeScript 前端开发 (页面、组件、API 集成、Zustand 状态管理)
- **medical-imaging-reviewer** — 安全/代码审查 (OWASP Top 10, 患者数据隐私, API 集成验证)
