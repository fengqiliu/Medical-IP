# 医技影像平台

临床医技360全景系统，整合患者的检验、影像等医技数据，提供统一的数据查看和 AI 辅助摘要功能。

## 系统架构

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│     前端        │     │     后端        │     │    AI 服务       │
│   React 18     │────▶│  Spring Boot   │     │    FastAPI      │
│  + TypeScript  │     │    + Java 17    │     │   + LangChain   │
│     (3000)     │     │     (8080)      │     │     (8000)      │
└─────────────────┘     └─────────────────┘     └─────────────────┘
```

## 功能特性

- **患者360视图**：整合患者所有医技数据
- **时间线展示**：按时间轴展示患者就诊事件
- **检验报告查看**：检验项目结果与参考值对比
- **影像报告查看**：影像检查报告与 AI 摘要
- **AI 辅助摘要**：基于 LLM 自动生成检验/影像报告摘要
- **数据权限控制**：基于角色的行级数据访问控制
- **访问审计**：完整的操作审计日志

## 技术栈

| 层级   | 技术                                                      |
| ------ | --------------------------------------------------------- |
| 前端   | React 18, TypeScript, Vite, Ant Design 5, Zustand         |
| 后端   | Spring Boot 3.2.3, Java 17, Spring Security, MyBatis-Plus |
| AI     | FastAPI, LangChain, OpenAI GPT-3.5-turbo                  |
| 数据库 | PostgreSQL, Redis                                         |
| 认证   | JWT (jjwt 0.12.5)                                         |

## 快速开始

### 环境要求

- Node.js 18+
- Java 17+
- Python 3.10+
- PostgreSQL 14+
- Redis 6+

### 1. 启动后端

```bash
cd backend

# 配置数据库连接 (src/main/resources/application.yml)
# 修改 spring.datasource.url, username, password

mvn clean package
java -jar target/medical-360-backend-1.0.0.jar
```

后端服务启动于 http://localhost:8080

### 2. 启动 AI 服务

```bash
cd ai-service

cp .env.example .env
# 编辑 .env，填入 OPENAI_API_KEY

pip install -r requirements.txt
python -m app.main
```

AI 服务启动于 http://localhost:8000

### 3. 启动前端

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
│   │   ├── api/          # API 请求封装
│   │   ├── components/   # 通用组件 (Layout, Timeline, AISummaryCard 等)
│   │   ├── pages/        # 页面组件
│   │   ├── stores/       # Zustand 状态管理
│   │   └── types/        # TypeScript 类型定义
│   ├── package.json
│   └── vite.config.ts
│
├── backend/
│   ├── src/main/java/com/medical360/
│   │   ├── config/       # Spring 配置 (Security, CORS)
│   │   ├── controller/  # REST 控制器
│   │   ├── dto/         # 数据传输对象
│   │   ├── entity/      # 数据库实体
│   │   ├── handler/     # 全局异常处理
│   │   ├── integration/ # 中台接入层 (mapper, adapter, sync)
│   │   ├── mapper/      # MyBatis Mapper
│   │   ├── security/    # JWT 认证与数据权限
│   │   └── service/     # 业务服务
│   └── pom.xml
│
└── ai-service/
    ├── app/
    │   ├── models/       # Pydantic 请求模型
    │   ├── routers/     # FastAPI 路由
    │   └── services/    # LLM 服务与 Prompt 构建
    ├── config.py
    └── requirements.txt
```

## API 路由

### 后端 API (8080)

| 方法 | 路径                      | 描述            |
| ---- | ------------------------- | --------------- |
| POST | /api/auth/login           | 用户登录        |
| GET  | /api/patient/{id}         | 获取患者360视图 |
| GET  | /api/timeline/{patientId} | 获取患者时间线  |
| GET  | /api/lab/{orderId}        | 获取检验详情    |
| GET  | /api/imaging/{orderId}    | 获取影像详情    |

### AI 服务 API (8000)

| 方法 | 路径             | 描述             |
| ---- | ---------------- | ---------------- |
| POST | /imaging/summary | 生成影像报告摘要 |
| POST | /lab/summary     | 生成检验报告摘要 |
| GET  | /health          | 健康检查         |

## 数据权限

系统通过 `DataPermissionInterceptor` 实现行级数据访问控制：

- 基于用户名和患者 ID 的权限校验
- 未经授权的访问返回 403 Forbidden
- 所有访问操作记录至 `AccessLog` 表

## 前端路由

| 路径                   | 页面         |
| ---------------------- | ------------ |
| /login                 | 登录页       |
| /dashboard             | 工作台仪表盘 |
| /patient360/:patientId | 患者360视图  |
| /timeline/:patientId   | 患者时间线   |
| /lab/:orderId          | 检验详情     |
| /imaging/:orderId      | 影像详情     |

## 环境变量

### AI 服务 (.env)

```env
OPENAI_API_KEY=your-api-key-here
OPENAI_BASE_URL=https://api.openai.com/v1
MODEL_NAME=gpt-3.5-turbo
```

## License

MIT
