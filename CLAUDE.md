# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

医技影像平台是一个临床医技360全景系统，用于整合患者的检验、影像等医技数据，提供统一的数据查看和 AI 辅助摘要功能。

## 目录结构

```
├── frontend/          # React 18 + TypeScript + Vite 前端
├── backend/           # Spring Boot 3.2.3 + Java 17 后端
├── ai-service/        # FastAPI + LangChain AI 摘要服务
└── docs/              # 文档（设计规格等）
```

## 开发命令

### 前端 (frontend/)

```bash
npm install          # 安装依赖
npm run dev          # 开发模式 (Vite dev server)
npm run build        # 生产构建 (TypeScript 编译 + Vite build)
npm run preview      # 预览生产构建
```

### 后端 (backend/)

```bash
mvn clean package    # 构建 JAR
mvn spring-boot:run  # 运行 Spring Boot 应用
mvn test             # 运行测试
```

### AI 服务 (ai-service/)

```bash
pip install -r requirements.txt  # 安装 Python 依赖
python -m app.main               # 运行 FastAPI 服务 (端口 8000)
```

## 技术栈

### 前端

- React 18 + TypeScript + Vite
- React Router 6 (路由)
- Ant Design 5 (UI 组件库)
- Zustand (状态管理)
- Axios (HTTP 客户端)
- 路径别名：`@/*` 指向 `src/*`

### 后端

- Spring Boot 3.2.3 (Java 17)
- Spring Security + JWT (认证授权)
- MyBatis-Plus 3.5.5 (ORM)
- PostgreSQL (数据库)
- Redis (缓存/会话)
- 数据权限拦截器 (`DataPermissionInterceptor`) 实现行级访问控制

### AI 服务

- FastAPI + Uvicorn
- LangChain + OpenAI GPT-3.5-turbo
- Pydantic v2 (数据验证)

## 核心架构

### 认证流程

1. 前端登录获取 JWT token，存储于 localStorage
2. 请求头携带 `Authorization: Bearer <token>`
3. 后端 `JwtAuthenticationFilter` 验证 token
4. `DataPermissionInterceptor` 在查询时注入数据权限条件

### API 路由结构

**后端 (8080端口)**

- `/api/auth/*` - 认证相关
- `/api/patient/*` - 患者360视图
- `/api/timeline/*` - 患者时间线
- `/api/lab/*` - 检验详情
- `/api/imaging/*` - 影像详情

**AI 服务 (8000端口)**

- `POST /imaging/summary` - 影像报告 AI 摘要
- `POST /lab/summary` - 检验结果 AI 摘要

### 前端路由

```
/login           # 登录页
/dashboard       # 工作台仪表盘
/patient360/:patientId   # 患者360视图
/timeline/:patientId     # 患者时间线
/lab/:orderId    # 检验详情
/imaging/:orderId # 影像详情
```

## 数据模型

### 后端核心实体

- `User` / `Role` - 用户角色体系
- `Patient` / `Encounter` - 患者与就诊
- `LabOrder` / `LabResult` - 检验医嘱与结果
- `ImagingOrder` / `ImagingReport` - 影像医嘱与报告
- `ClinicalEvent` - 临床事件时间线
- `AccessLog` - 访问审计日志

### AI 服务模型

- `LabSummaryRequest` - 检验摘要请求
- `ImagingSummaryRequest` - 影像摘要请求

## 环境配置

- 前端：`.env.example` → 复制为 `.env.local`
- AI 服务：`.env.example` → 复制为 `.env`，配置 `OPENAI_API_KEY`
- 后端：Spring Boot 配置 via `application.yml` 或环境变量

## 注意事项

- 前端 API 请求通过代理 `/api` 转发至后端 (Vite proxy 配置)
- 后端 CORS 配置允许前端 origin 访问
- AI 服务无认证保护，部署时需注意网络安全
- 路径别名 `@/*` 在 `tsconfig.json` 和 `vite.config.ts` 中配置一致
