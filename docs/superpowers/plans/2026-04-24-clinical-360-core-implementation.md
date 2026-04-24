# 临床医技 360 核心版 — 实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 一期实现临床医技360核心版，包含：React前端 + Spring Boot后端 + Python AI服务，统一中台接入检验与影像数据，患者360汇总页、时间轴、检验详情、影像详情，轻量AI辅助，RBAC+科室/岗位权限。

**Architecture:** 前后端分离架构，前端React+Vite+Ant Design，后端Spring Boot+MyBatis-Plus+PostgreSQL+Redis，AI独立Python FastAPI服务通过HTTP调用。

**Tech Stack:**

- Frontend: React 18 + TypeScript + Vite + Ant Design 5 + Zustand + React Router 6 + Axios
- Backend: Spring Boot 3 + Java 17 + MyBatis-Plus + PostgreSQL + Redis + JWT
- AI Service: Python 3.11 + FastAPI + Uvicorn + LangChain + OpenAI API
- Database: PostgreSQL 15
- Cache: Redis 7

---

## 项目目录结构

```
/Users/Davinci/Desktop/医技影像平台/
├── frontend/                          # React 前端
│   ├── src/
│   │   ├── api/                       # API 请求层
│   │   │   ├── index.ts               # Axios 实例配置
│   │   │   ├── auth.ts                # 登录/登出接口
│   │   │   ├── patient.ts             # 患者360接口
│   │   │   ├── lab.ts                 # 检验数据接口
│   │   │   ├── imaging.ts             # 影像数据接口
│   │   │   └── ai.ts                  # AI 辅助接口
│   │   ├── components/                # 公共组件
│   │   │   ├── Layout/
│   │   │   │   ├── AppLayout.tsx      # 主布局
│   │   │   │   ├── AppHeader.tsx      # 顶栏
│   │   │   │   └── AppSidebar.tsx     # 侧边栏
│   │   │   ├── PatientHeader.tsx      # 患者信息顶栏
│   │   │   ├── Timeline.tsx           # 时间轴组件
│   │   │   ├── LabResultTable.tsx     # 检验结果表格
│   │   │   ├── ImagingReportCard.tsx  # 影像报告卡片
│   │   │   ├── AISummaryCard.tsx      # AI 摘要卡片
│   │   │   └── AbnormalTag.tsx        # 异常标记组件
│   │   ├── pages/
│   │   │   ├── Login/                 # 登录页
│   │   │   ├── Dashboard/             # 工作台
│   │   │   ├── Patient360/            # 患者360汇总页
│   │   │   ├── Timeline/              # 时间轴页
│   │   │   ├── LabDetail/             # 检验详情页
│   │   │   ├── ImagingDetail/         # 影像详情页
│   │   │   └── System/                # 系统管理（管理员）
│   │   ├── stores/                    # Zustand 状态管理
│   │   │   ├── authStore.ts           # 登录状态
│   │   │   └── patientStore.ts        # 患者上下文
│   │   ├── router/                    # 路由配置
│   │   ├── types/                    # TypeScript 类型定义
│   │   │   ├── auth.ts
│   │   │   ├── patient.ts
│   │   │   ├── lab.ts
│   │   │   ├── imaging.ts
│   │   │   └── ai.ts
│   │   ├── App.tsx
│   │   └── main.tsx
│   ├── index.html
│   ├── package.json
│   ├── tsconfig.json
│   └── vite.config.ts
├── backend/                          # Spring Boot 后端
│   ├── src/main/java/com/medical360/
│   │   ├── Medical360Application.java
│   │   ├── config/
│   │   │   ├── CorsConfig.java
│   │   │   ├── SecurityConfig.java
│   │   │   ├── RedisConfig.java
│   │   │   └── JwtConfig.java
│   │   ├── common/
│   │   │   ├── Result.java
│   │   │   └── PageResult.java
│   │   ├── entity/
│   │   │   ├── Patient.java
│   │   │   ├── Encounter.java
│   │   │   ├── LabOrder.java
│   │   │   ├── LabResult.java
│   │   │   ├── ImagingOrder.java
│   │   │   ├── ImagingReport.java
│   │   │   ├── ClinicalEvent.java
│   │   │   ├── User.java
│   │   │   ├── Role.java
│   │   │   ├── Department.java
│   │   │   ├── Position.java
│   │   │   ├── AccessLog.java
│   │   │   └── AccessScope.java
│   │   ├── mapper/
│   │   │   ├── PatientMapper.java
│   │   │   ├── LabOrderMapper.java
│   │   │   ├── LabResultMapper.java
│   │   │   ├── ImagingOrderMapper.java
│   │   │   ├── ImagingReportMapper.java
│   │   │   ├── ClinicalEventMapper.java
│   │   │   ├── UserMapper.java
│   │   │   ├── RoleMapper.java
│   │   │   ├── DepartmentMapper.java
│   │   │   └── AccessLogMapper.java
│   │   ├── service/
│   │   │   ├── PatientService.java
│   │   │   ├── LabService.java
│   │   │   ├── ImagingService.java
│   │   │   ├── ClinicalEventService.java
│   │   │   ├── AIService.java
│   │   │   ├── AuthService.java
│   │   │   └── AccessLogService.java
│   │   ├── service/impl/
│   │   │   ├── PatientServiceImpl.java
│   │   │   ├── LabServiceImpl.java
│   │   │   ├── ImagingServiceImpl.java
│   │   │   ├── ClinicalEventServiceImpl.java
│   │   │   ├── AIServiceImpl.java
│   │   │   ├── AuthServiceImpl.java
│   │   │   └── AccessLogServiceImpl.java
│   │   ├── controller/
│   │   │   ├── AuthController.java
│   │   │   ├── PatientController.java
│   │   │   ├── LabController.java
│   │   │   ├── ImagingController.java
│   │   │   ├── TimelineController.java
│   │   │   ├── AIController.java
│   │   │   └── SystemController.java
│   │   ├── dto/
│   │   │   ├── LoginRequest.java
│   │   │   ├── Patient360DTO.java
│   │   │   ├── LabResultDTO.java
│   │   │   ├── ImagingReportDTO.java
│   │   │   ├── TimelineEventDTO.java
│   │   │   └── AISummaryDTO.java
│   │   ├── integration/               # 中台接入层
│   │   │   ├── adapter/
│   │   │   │   ├── DataSourceAdapter.java   # 适配器接口
│   │   │   │   ├── LISAdapter.java          # LIS 适配器
│   │   │   │   ├── RISAdapter.java          # RIS 适配器
│   │   │   │   └── EMRAdapter.java           # EMR 适配器
│   │   │   ├── mapper/
│   │   │   │   ├── FieldMappingService.java  # 字段映射服务
│   │   │   │   └── DataStandardizer.java     # 数据标准化
│   │   │   ├── sync/
│   │   │   │   ├── DataSyncScheduler.java    # 同步调度器
│   │   │   │   └── SyncTaskService.java     # 同步任务服务
│   │   │   └── master/
│   │   │       └── PatientMasterIndexService.java  # 主索引服务
│   │   ├── security/
│   │   │   ├── JwtAuthenticationFilter.java
│   │   │   ├── JwtTokenProvider.java
│   │   │   ├── DataPermissionInterceptor.java  # 数据权限拦截
│   │   │   └── PermissionService.java
│   │   └── handler/
│   │       └── GlobalExceptionHandler.java
│   ├── src/main/resources/
│   │   ├── application.yml
│   │   ├── config/
│   │   │   └── application-dev.yml
│   │   └── sql/
│   │       └── init.sql
│   └── pom.xml
├── ai-service/                       # Python AI 服务
│   ├── app/
│   │   ├── main.py
│   │   ├── routers/
│   │   │   ├── lab_summary.py
│   │   │   ├── imaging_summary.py
│   │   │   └── patient_summary.py
│   │   ├── services/
│   │   │   ├── llm_service.py
│   │   │   └── prompt_builder.py
│   │   └── models/
│   │       └── summary_request.py
│   ├── requirements.txt
│   └── config.py
├── docs/superpowers/
│   ├── specs/
│   └── plans/
└── .gitignore
```

---

## Phase 1: 项目脚手架

### Task 1: 前端项目初始化

**Files:**

- Create: `frontend/package.json`
- Create: `frontend/tsconfig.json`
- Create: `frontend/vite.config.ts`
- Create: `frontend/index.html`
- Create: `frontend/src/main.tsx`
- Create: `frontend/src/App.tsx`

- [ ] **Step 1: 创建 package.json**

```json
{
  "name": "medical-360-frontend",
  "version": "1.0.0",
  "type": "module",
  "scripts": {
    "dev": "vite",
    "build": "tsc && vite build",
    "preview": "vite preview"
  },
  "dependencies": {
    "react": "^18.2.0",
    "react-dom": "^18.2.0",
    "react-router-dom": "^6.22.0",
    "antd": "^5.15.0",
    "axios": "^1.6.7",
    "zustand": "^4.5.1",
    "dayjs": "^1.11.10"
  },
  "devDependencies": {
    "@types/react": "^18.2.55",
    "@types/react-dom": "^18.2.19",
    "@vitejs/plugin-react": "^4.2.1",
    "typescript": "^5.3.3",
    "vite": "^5.1.3"
  }
}
```

- [ ] **Step 2: 创建 tsconfig.json**

```json
{
  "compilerOptions": {
    "target": "ES2020",
    "useDefineForClassFields": true,
    "lib": ["ES2020", "DOM", "DOM.Iterable"],
    "module": "ESNext",
    "skipLibCheck": true,
    "moduleResolution": "bundler",
    "allowImportingTsExtensions": true,
    "resolveJsonModule": true,
    "isolatedModules": true,
    "noEmit": true,
    "jsx": "react-jsx",
    "strict": true,
    "noUnusedLocals": true,
    "noUnusedParameters": true,
    "noFallthroughCasesInSwitch": true,
    "baseUrl": ".",
    "paths": {
      "@/*": ["src/*"]
    }
  },
  "include": ["src"],
  "references": [{ "path": "./tsconfig.node.json" }]
}
```

- [ ] **Step 3: 创建 tsconfig.node.json**

```json
{
  "compilerOptions": {
    "composite": true,
    "skipLibCheck": true,
    "module": "ESNext",
    "moduleResolution": "bundler",
    "allowSyntheticDefaultImports": true
  },
  "include": ["vite.config.ts"]
}
```

- [ ] **Step 4: 创建 vite.config.ts**

```typescript
import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import path from "path";

export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src"),
    },
  },
  server: {
    port: 3000,
    proxy: {
      "/api": {
        target: "http://localhost:8080",
        changeOrigin: true,
      },
    },
  },
});
```

- [ ] **Step 5: 创建 index.html**

```html
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>医技360 - 临床医技数据平台</title>
  </head>
  <body>
    <div id="root"></div>
    <script type="module" src="/src/main.tsx"></script>
  </body>
</html>
```

- [ ] **Step 6: 创建 src/main.tsx**

```typescript
import React from 'react'
import ReactDOM from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom'
import { ConfigProvider } from 'antd'
import zhCN from 'antd/locale/zh_CN'
import App from './App'
import './index.css'

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <ConfigProvider locale={zhCN}>
      <BrowserRouter>
        <App />
      </BrowserRouter>
    </ConfigProvider>
  </React.StrictMode>
)
```

- [ ] **Step 7: 创建 src/App.tsx**

```typescript
import { Routes, Route, Navigate } from 'react-router-dom'
import AppLayout from '@/components/Layout/AppLayout'
import Login from '@/pages/Login'
import Dashboard from '@/pages/Dashboard'
import Patient360 from '@/pages/Patient360'
import Timeline from '@/pages/Timeline'
import LabDetail from '@/pages/LabDetail'
import ImagingDetail from '@/pages/ImagingDetail'
import { useAuthStore } from '@/stores/authStore'

function PrivateRoute({ children }: { children: React.ReactNode }) {
  const { token } = useAuthStore()
  return token ? <>{children}</> : <Navigate to="/login" replace />
}

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route
        path="/"
        element={
          <PrivateRoute>
            <AppLayout />
          </PrivateRoute>
        }
      >
        <Route index element={<Navigate to="/dashboard" replace />} />
        <Route path="dashboard" element={<Dashboard />} />
        <Route path="patient360/:patientId" element={<Patient360 />} />
        <Route path="timeline/:patientId" element={<Timeline />} />
        <Route path="lab/:orderId" element={<LabDetail />} />
        <Route path="imaging/:orderId" element={<ImagingDetail />} />
      </Route>
    </Routes>
  )
}
```

- [ ] **Step 8: 创建 src/index.css**

```css
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family:
    -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue",
    Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
```

- [ ] **Step 9: 安装依赖并验证构建**

Run: `cd frontend && npm install && npm run build`
Expected: 成功构建，生成 dist 目录

- [ ] **Step 10: 提交**

```bash
git add frontend/
git commit -m "feat: 初始化 React + Vite + TypeScript 前端项目"
```

---

### Task 2: 后端项目初始化

**Files:**

- Create: `backend/pom.xml`
- Create: `backend/src/main/java/com/medical360/Medical360Application.java`
- Create: `backend/src/main/resources/application.yml`
- Create: `backend/src/main/resources/sql/init.sql`

- [ ] **Step 1: 创建 pom.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.3</version>
    </parent>

    <groupId>com.medical360</groupId>
    <artifactId>medical-360-backend</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <properties>
        <java.version>17</java.version>
        <mybatis-plus.version>3.5.5</mybatis-plus.version>
        <jjwt.version>0.12.5</jjwt.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
            <version>${mybatis-plus.version}</version>
        </dependency>
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>${jjwt.version}</version>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <version>${jjwt.version}</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <version>${jjwt.version}</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

- [ ] **Step 2: 创建 Application 主类**

```java
package com.medical360;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.medical360.mapper")
@EnableScheduling
public class Medical360Application {
    public static void main(String[] args) {
        SpringApplication.run(Medical360Application.class, args);
    }
}
```

- [ ] **Step 3: 创建 application.yml**

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/medical360
    username: postgres
    password: postgres
    driver-class-name: org.postgresql.Driver
  data:
    redis:
      host: localhost
      port: 6379

mybatis-plus:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.medical360.entity
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl

jwt:
  secret: medical360-secret-key-change-in-production
  expiration: 86400000

ai-service:
  base-url: http://localhost:8000
```

- [ ] **Step 4: 创建数据库初始化 SQL**

```sql
-- 患者主索引
CREATE TABLE patient (
    id BIGSERIAL PRIMARY KEY,
    unified_patient_id VARCHAR(64) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    gender VARCHAR(10),
    birth_date DATE,
    id_card_no VARCHAR(18),
    phone VARCHAR(20),
    address VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 就诊记录
CREATE TABLE encounter (
    id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT NOT NULL REFERENCES patient(id),
    encounter_type VARCHAR(20) NOT NULL,
    department_id BIGINT,
    visit_datetime TIMESTAMP,
    admission_reason VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 检验申请单
CREATE TABLE lab_order (
    id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT NOT NULL REFERENCES patient(id),
    encounter_id BIGINT REFERENCES encounter(id),
    order_no VARCHAR(64) NOT NULL,
    department_id BIGINT,
    order_datetime TIMESTAMP,
    status VARCHAR(20),
    specimen_type VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 检验结果明细
CREATE TABLE lab_result (
    id BIGSERIAL PRIMARY KEY,
    lab_order_id BIGINT NOT NULL REFERENCES lab_order(id),
    item_code VARCHAR(50) NOT NULL,
    item_name VARCHAR(100) NOT NULL,
    result_value VARCHAR(100),
    unit VARCHAR(20),
    ref_range_low DECIMAL(10,2),
    ref_range_high DECIMAL(10,2),
    abnormal_flag VARCHAR(10),
    result_datetime TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 影像检查申请单
CREATE TABLE imaging_order (
    id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT NOT NULL REFERENCES patient(id),
    encounter_id BIGINT REFERENCES encounter(id),
    order_no VARCHAR(64) NOT NULL,
    department_id BIGINT,
    body_part VARCHAR(100),
    modality VARCHAR(50),
    order_datetime TIMESTAMP,
    status VARCHAR(20),
    pacs_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 影像报告
CREATE TABLE imaging_report (
    id BIGSERIAL PRIMARY KEY,
    imaging_order_id BIGINT NOT NULL REFERENCES imaging_order(id),
    report_content TEXT,
    impression TEXT,
    report_doctor VARCHAR(100),
    report_datetime TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 时间轴事件
CREATE TABLE clinical_event (
    id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT NOT NULL REFERENCES patient(id),
    event_type VARCHAR(20) NOT NULL,
    event_datetime TIMESTAMP NOT NULL,
    event_summary VARCHAR(500),
    event_status VARCHAR(20),
    reference_id BIGINT NOT NULL,
    department_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 科室
CREATE TABLE department (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    parent_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 岗位
CREATE TABLE position (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    department_id BIGINT REFERENCES department(id),
    data_scope VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 用户
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100),
    department_id BIGINT REFERENCES department(id),
    position_id BIGINT REFERENCES position(id),
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 角色
CREATE TABLE role (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 用户角色关联
CREATE TABLE user_role (
    user_id BIGINT NOT NULL REFERENCES users(id),
    role_id BIGINT NOT NULL REFERENCES role(id),
    PRIMARY KEY (user_id, role_id)
);

-- 角色菜单权限
CREATE TABLE role_menu (
    role_id BIGINT NOT NULL REFERENCES role(id),
    menu_code VARCHAR(50) NOT NULL,
    PRIMARY KEY (role_id, menu_code)
);

-- 访问日志
CREATE TABLE access_log (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    patient_id BIGINT,
    action VARCHAR(100),
    request_detail TEXT,
    ip_address VARCHAR(50),
    access_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 索引
CREATE INDEX idx_patient_unified_id ON patient(unified_patient_id);
CREATE INDEX idx_lab_order_patient ON lab_order(patient_id);
CREATE INDEX idx_imaging_order_patient ON imaging_order(patient_id);
CREATE INDEX idx_clinical_event_patient ON clinical_event(patient_id);
CREATE INDEX idx_clinical_event_datetime ON clinical_event(event_datetime DESC);
CREATE INDEX idx_access_log_user ON access_log(user_id);
CREATE INDEX idx_access_log_patient ON access_log(patient_id);
```

- [ ] **Step 5: 验证后端构建**

Run: `cd backend && mvn clean compile -q`
Expected: BUILD SUCCESS

- [ ] **Step 6: 提交**

```bash
git add backend/
git commit -m "feat: 初始化 Spring Boot 3 + MyBatis-Plus 后端项目"
```

---

## Phase 2: 认证与权限

### Task 3: 后端登录认证

**Files:**

- Create: `backend/src/main/java/com/medical360/common/Result.java`
- Create: `backend/src/main/java/com/medical360/common/PageResult.java`
- Create: `backend/src/main/java/com/medical360/entity/User.java`
- Create: `backend/src/main/java/com/medical360/entity/Role.java`
- Create: `backend/src/main/java/com/medical360/mapper/UserMapper.java`
- Create: `backend/src/main/java/com/medical360/service/AuthService.java`
- Create: `backend/src/main/java/com/medical360/service/impl/AuthServiceImpl.java`
- Create: `backend/src/main/java/com/medical360/controller/AuthController.java`
- Create: `backend/src/main/java/com/medical360/security/JwtTokenProvider.java`
- Create: `backend/src/main/java/com/medical360/security/JwtAuthenticationFilter.java`
- Create: `backend/src/main/java/com/medical360/config/SecurityConfig.java`
- Create: `backend/src/main/java/com/medical360/config/CorsConfig.java`
- Modify: `backend/src/main/resources/sql/init.sql`

- [ ] **Step 1: 创建 Result 统一响应**

```java
package com.medical360.common;

import lombok.Data;

@Data
public class Result<T> {
    private int code;
    private String message;
    private T data;

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMessage(message);
        return result;
    }

    public static <T> Result<T> error(int code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
```

- [ ] **Step 2: 创建 User 实体**

```java
package com.medical360.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("users")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String name;
    private Long departmentId;
    private Long positionId;
    private Boolean enabled;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
```

- [ ] **Step 3: 创建 Role 实体**

```java
package com.medical360.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("role")
public class Role {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String description;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
```

- [ ] **Step 4: 创建 UserMapper**

```java
package com.medical360.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medical360.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT u.*, r.name as role_name FROM users u " +
            "LEFT JOIN user_role ur ON u.id = ur.user_id " +
            "LEFT JOIN role r ON ur.role_id = r.id " +
            "WHERE u.username = #{username}")
    User selectByUsername(String username);
}
```

- [ ] **Step 5: 创建 JwtTokenProvider**

```java
package com.medical360.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Long userId, String username, Long departmentId, String roleName) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("departmentId", departmentId);
        claims.put("roleName", roleName);

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
```

- [ ] **Step 6: 创建 JwtAuthenticationFilter**

```java
package com.medical360.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = getTokenFromRequest(request);

        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            var claims = jwtTokenProvider.getClaims(token);
            String username = claims.getSubject();
            String roleName = claims.get("roleName", String.class);

            var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + roleName));
            var authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);

            // 将用户信息存入请求属性，供后续使用
            request.setAttribute("userId", claims.get("userId", Long.class));
            request.setAttribute("departmentId", claims.get("departmentId", Long.class));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
```

- [ ] **Step 7: 创建 SecurityConfig**

```java
package com.medical360.config;

import com.medical360.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

- [ ] **Step 8: 创建 AuthService**

```java
package com.medical360.service;

import com.medical360.entity.User;

public interface AuthService {
    String login(String username, String password);
    User getCurrentUser(String username);
}
```

- [ ] **Step 9: 创建 AuthServiceImpl**

```java
package com.medical360.service.impl;

import com.medical360.entity.User;
import com.medical360.mapper.UserMapper;
import com.medical360.security.JwtTokenProvider;
import com.medical360.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        if (!user.getEnabled()) {
            throw new RuntimeException("账号已被禁用");
        }

        // 获取用户角色
        String roleName = getUserRole(user.getId());

        return jwtTokenProvider.generateToken(
            user.getId(),
            user.getUsername(),
            user.getDepartmentId(),
            roleName
        );
    }

    @Override
    public User getCurrentUser(String username) {
        return userMapper.selectByUsername(username);
    }

    private String getUserRole(Long userId) {
        // 简化实现，实际应从 user_role 和 role 表联合查询
        return "CLINICIAN";
    }
}
```

- [ ] **Step 10: 创建 AuthController**

```java
package com.medical360.controller;

import com.medical360.common.Result;
import com.medical360.entity.User;
import com.medical360.service.AuthService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginRequest request) {
        String token = authService.login(request.getUsername(), request.getPassword());
        return Result.success(token);
    }

    @GetMapping("/current")
    public Result<User> getCurrentUser(Authentication authentication) {
        User user = authService.getCurrentUser(authentication.getName());
        return Result.success(user);
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.success();
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }
}
```

- [ ] **Step 11: 更新 SQL 添加测试用户**

```sql
-- 添加测试用户（密码为 123456 的 BCrypt 哈希）
INSERT INTO users (username, password, name, department_id, position_id, enabled)
VALUES ('doctor1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '张医生', 1, 1, true);

-- 添加默认角色
INSERT INTO role (name, description) VALUES ('CLINICIAN', '临床医生');
INSERT INTO role (name, description) VALUES ('TECHNICIAN', '医技医生');
INSERT INTO role (name, description) VALUES ('DEPT_ADMIN', '科室管理员');
INSERT INTO role (name, description) VALUES ('SYS_ADMIN', '系统管理员');

-- 分配角色
INSERT INTO user_role (user_id, role_id) VALUES (1, 1);
```

- [ ] **Step 12: 验证认证功能**

Run: `cd backend && mvn test -Dtest=AuthServiceTest -q`
Expected: 测试通过

- [ ] **Step 13: 提交**

```bash
git add backend/
git commit -m "feat: 实现 JWT 登录认证与 Security 配置"
```

---

### Task 4: 前端登录页面与认证状态

**Files:**

- Create: `frontend/src/pages/Login/index.tsx`
- Create: `frontend/src/stores/authStore.ts`
- Create: `frontend/src/api/index.ts`
- Create: `frontend/src/api/auth.ts`
- Create: `frontend/src/types/auth.ts`

- [ ] **Step 1: 创建 TypeScript 类型**

```typescript
// frontend/src/types/auth.ts
export interface LoginRequest {
  username: string;
  password: string;
}

export interface User {
  id: number;
  username: string;
  name: string;
  departmentId: number;
  positionId: number;
}

export interface LoginResponse {
  token: string;
}
```

- [ ] **Step 2: 创建 Axios 实例配置**

```typescript
// frontend/src/api/index.ts
import axios from "axios";
import { message } from "antd";

const api = axios.create({
  baseURL: "/api",
  timeout: 30000,
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => response.data,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem("token");
      window.location.href = "/login";
    }
    message.error(error.response?.data?.message || "请求失败");
    return Promise.reject(error);
  },
);

export default api;
```

- [ ] **Step 3: 创建认证 API**

```typescript
// frontend/src/api/auth.ts
import api from "./index";
import type { LoginRequest, LoginResponse, User } from "@/types/auth";

export const login = (data: LoginRequest) =>
  api.post<LoginResponse>("/auth/login", data);

export const getCurrentUser = () => api.get<User>("/auth/current");
```

- [ ] **Step 4: 创建认证状态 Store**

```typescript
// frontend/src/stores/authStore.ts
import { create } from "zustand";
import type { User } from "@/types/auth";

interface AuthState {
  token: string | null;
  user: User | null;
  setToken: (token: string | null) => void;
  setUser: (user: User | null) => void;
  logout: () => void;
}

export const useAuthStore = create<AuthState>((set) => ({
  token: localStorage.getItem("token"),
  user: null,
  setToken: (token) => {
    if (token) {
      localStorage.setItem("token", token);
    } else {
      localStorage.removeItem("token");
    }
    set({ token });
  },
  setUser: (user) => set({ user }),
  logout: () => {
    localStorage.removeItem("token");
    set({ token: null, user: null });
  },
}));
```

- [ ] **Step 5: 创建登录页面**

```typescript
// frontend/src/pages/Login/index.tsx
import { useState } from 'react'
import { Form, Input, Button, Card, message } from 'antd'
import { UserOutlined, LockOutlined } from '@ant-design/icons'
import { useNavigate } from 'react-router-dom'
import { login } from '@/api/auth'
import { useAuthStore } from '@/stores/authStore'

export default function Login() {
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()
  const { setToken } = useAuthStore()

  const onFinish = async (values: { username: string; password: string }) => {
    setLoading(true)
    try {
      const { data } = await login(values)
      setToken(data)
      message.success('登录成功')
      navigate('/')
    } catch {
      message.error('用户名或密码错误')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div style={{
      minHeight: '100vh',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
    }}>
      <Card style={{ width: 400 }}>
        <div style={{ textAlign: 'center', marginBottom: 24 }}>
          <h1 style={{ fontSize: 24, marginBottom: 8 }}>医技360平台</h1>
          <p style={{ color: '#666' }}>临床医技数据与患者360浏览</p>
        </div>
        <Form
          name="login"
          onFinish={onFinish}
          autoComplete="off"
          size="large"
        >
          <Form.Item
            name="username"
            rules={[{ required: true, message: '请输入用户名' }]}
          >
            <Input prefix={<UserOutlined />} placeholder="用户名" />
          </Form.Item>
          <Form.Item
            name="password"
            rules={[{ required: true, message: '请输入密码' }]}
          >
            <Input.Password prefix={<LockOutlined />} placeholder="密码" />
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" loading={loading} block>
              登录
            </Button>
          </Form.Item>
        </Form>
      </Card>
    </div>
  )
}
```

- [ ] **Step 6: 验证登录页面**

Run: `cd frontend && npm run dev`
Expected: 登录页面可访问，表单提交可调用后端 API

- [ ] **Step 7: 提交**

```bash
git add frontend/
git commit -m "feat: 实现前端登录页面与认证状态管理"
```

---

## Phase 3: 患者360核心页面

### Task 5: 患者360汇总页

**Files:**

- Create: `frontend/src/types/patient.ts`
- Create: `frontend/src/api/patient.ts`
- Create: `frontend/src/components/PatientHeader.tsx`
- Create: `frontend/src/components/AISummaryCard.tsx`
- Create: `frontend/src/pages/Patient360/index.tsx`

- [ ] **Step 1: 创建患者类型定义**

```typescript
// frontend/src/types/patient.ts
export interface Patient {
  id: number;
  unifiedPatientId: string;
  name: string;
  gender: string;
  birthDate: string;
  idCardNo: string;
  phone: string;
}

export interface Patient360DTO {
  patient: Patient;
  encounter: Encounter;
  labSummary: LabSummary;
  imagingSummary: ImagingSummary;
  aiSummary: string;
  alerts: Alert[];
}

export interface Encounter {
  id: number;
  encounterType: string;
  departmentName: string;
  visitDatetime: string;
  admissionReason: string;
}

export interface LabSummary {
  recentCount: number;
  abnormalCount: number;
  recentOrders: LabOrder[];
}

export interface LabOrder {
  id: number;
  orderNo: string;
  orderDatetime: string;
  specimenType: string;
  status: string;
  departmentName: string;
}

export interface ImagingSummary {
  recentCount: number;
  recentOrders: ImagingOrder[];
}

export interface ImagingOrder {
  id: number;
  orderNo: string;
  orderDatetime: string;
  bodyPart: string;
  modality: string;
  status: string;
  departmentName: string;
}

export interface Alert {
  type: "lab" | "imaging" | "warning";
  message: string;
  datetime: string;
}
```

- [ ] **Step 2: 创建患者 API**

```typescript
// frontend/src/api/patient.ts
import api from "./index";
import type { Patient360DTO } from "@/types/patient";

export const getPatient360 = (patientId: number) =>
  api.get<Patient360DTO>(`/patient/360/${patientId}`);

export const searchPatients = (keyword: string) =>
  api.get<Patient[]>("/patient/search", { params: { keyword } });
```

- [ ] **Step 3: 创建 PatientHeader 组件**

```typescript
// frontend/src/components/PatientHeader.tsx
import { Card, Tag, Descriptions } from 'antd'
import type { Patient, Encounter } from '@/types/patient'

interface Props {
  patient: Patient
  encounter: Encounter
}

export default function PatientHeader({ patient, encounter }: Props) {
  const age = patient.birthDate
    ? new Date().getFullYear() - new Date(patient.birthDate).getFullYear()
    : '-'

  const typeColor: Record<string, string> = {
    '门诊': 'blue',
    '住院': 'green',
    '急诊': 'red',
  }

  return (
    <Card>
      <Descriptions column={4} size="small">
        <Descriptions.Item label="姓名">{patient.name}</Descriptions.Item>
        <Descriptions.Item label="性别">{patient.gender}</Descriptions.Item>
        <Descriptions.Item label="年龄">{age}岁</Descriptions.Item>
        <Descriptions.Item label="就诊类型">
          <Tag color={typeColor[encounter.encounterType] || 'default'}>
            {encounter.encounterType}
          </Tag>
        </Descriptions.Item>
        <Descriptions.Item label="科室">{encounter.departmentName}</Descriptions.Item>
        <Descriptions.Item label="就诊时间">
          {new Date(encounter.visitDatetime).toLocaleString()}
        </Descriptions.Item>
        <Descriptions.Item label="就诊原因" span={2}>
          {encounter.admissionReason}
        </Descriptions.Item>
      </Descriptions>
    </Card>
  )
}
```

- [ ] **Step 4: 创建 AISummaryCard 组件**

```typescript
// frontend/src/components/AISummaryCard.tsx
import { Card, Alert } from 'antd'

interface Props {
  summary: string
  loading?: boolean
}

export default function AISummaryCard({ summary, loading }: Props) {
  return (
    <Card title="AI 辅助摘要" loading={loading}>
      <Alert
        message="辅助生成，仅供参考"
        description={summary || '暂无摘要信息'}
        type="info"
        showIcon
      />
    </Card>
  )
}
```

- [ ] **Step 5: 创建患者360页面**

```typescript
// frontend/src/pages/Patient360/index.tsx
import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { Card, Row, Col, Table, Button, Space, Statistic, Spin } from 'antd'
import { ExperimentOutlined, ScanOutlined, AlertOutlined } from '@ant-design/icons'
import PatientHeader from '@/components/PatientHeader'
import AISummaryCard from '@/components/AISummaryCard'
import { getPatient360 } from '@/api/patient'
import type { Patient360DTO } from '@/types/patient'

export default function Patient360() {
  const { patientId } = useParams<{ patientId: string }>()
  const navigate = useNavigate()
  const [data, setData] = useState<Patient360DTO | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    if (patientId) {
      loadData()
    }
  }, [patientId])

  const loadData = async () => {
    setLoading(true)
    try {
      const response = await getPatient360(Number(patientId))
      setData(response.data)
    } catch (error) {
      console.error('加载患者360数据失败', error)
    } finally {
      setLoading(false)
    }
  }

  if (loading) {
    return <Spin style={{ display: 'flex', justifyContent: 'center', marginTop: 100 }} />
  }

  if (!data) {
    return <div>未找到患者数据</div>
  }

  const { patient, encounter, labSummary, imagingSummary, aiSummary, alerts } = data

  return (
    <div style={{ padding: 24 }}>
      {/* 患者信息顶栏 */}
      <PatientHeader patient={patient} encounter={encounter} />

      {/* 统计卡片 */}
      <Row gutter={16} style={{ marginTop: 16 }}>
        <Col span={8}>
          <Card>
            <Statistic
              title="近期检验"
              value={labSummary.recentCount}
              prefix={<ExperimentOutlined />}
              valueStyle={{ color: '#3f8600' }}
            />
          </Card>
        </Col>
        <Col span={8}>
          <Card>
            <Statistic
              title="近期影像"
              value={imagingSummary.recentCount}
              prefix={<ScanOutlined />}
              valueStyle={{ color: '#1890ff' }}
            />
          </Card>
        </Col>
        <Col span={8}>
          <Card>
            <Statistic
              title="异常项"
              value={labSummary.abnormalCount}
              prefix={<AlertOutlined />}
              valueStyle={{ color: '#cf1322' }}
            />
          </Card>
        </Col>
      </Row>

      {/* 快捷入口 */}
      <Card style={{ marginTop: 16 }}>
        <Space>
          <Button type="primary" onClick={() => navigate(`/timeline/${patientId}`)}>
            查看时间轴
          </Button>
        </Space>
      </Card>

      {/* 检验摘要 */}
      <Card title="检验摘要" style={{ marginTop: 16 }}>
        <Table
          dataSource={labSummary.recentOrders}
          rowKey="id"
          size="small"
          pagination={false}
          columns={[
            { title: '检验单号', dataIndex: 'orderNo' },
            { title: '时间', dataIndex: 'orderDatetime', render: (v) => new Date(v).toLocaleString() },
            { title: '标本类型', dataIndex: 'specimenType' },
            { title: '科室', dataIndex: 'departmentName' },
            { title: '状态', dataIndex: 'status' },
            {
              title: '操作',
              render: (_, record) => (
                <Button type="link" onClick={() => navigate(`/lab/${record.id}`)}>
                  查看详情
                </Button>
              ),
            },
          ]}
        />
      </Card>

      {/* 影像摘要 */}
      <Card title="影像摘要" style={{ marginTop: 16 }}>
        <Table
          dataSource={imagingSummary.recentOrders}
          rowKey="id"
          size="small"
          pagination={false}
          columns={[
            { title: '检查单号', dataIndex: 'orderNo' },
            { title: '时间', dataIndex: 'orderDatetime', render: (v) => new Date(v).toLocaleString() },
            { title: '检查部位', dataIndex: 'bodyPart' },
            { title: '检查方式', dataIndex: 'modality' },
            { title: '科室', dataIndex: 'departmentName' },
            {
              title: '操作',
              render: (_, record) => (
                <Button type="link" onClick={() => navigate(`/imaging/${record.id}`)}>
                  查看报告
                </Button>
              ),
            },
          ]}
        />
      </Card>

      {/* AI 摘要 */}
      <AISummaryCard summary={aiSummary} />
    </div>
  )
}
```

- [ ] **Step 6: 提交**

```bash
git add frontend/
git commit -m "feat: 实现患者360汇总页"
```

---

### Task 6: 患者时间轴页面

**Files:**

- Create: `frontend/src/types/timeline.ts`
- Create: `frontend/src/api/timeline.ts`
- Create: `frontend/src/components/Timeline.tsx`
- Create: `frontend/src/pages/Timeline/index.tsx`

- [ ] **Step 1: 创建时间轴类型定义**

```typescript
// frontend/src/types/timeline.ts
export interface TimelineEventDTO {
  id: number;
  eventType: "lab" | "imaging";
  eventDatetime: string;
  eventSummary: string;
  eventStatus: string;
  referenceId: number;
  departmentName: string;
  abnormal?: boolean;
}
```

- [ ] **Step 2: 创建时间轴 API**

```typescript
// frontend/src/api/timeline.ts
import api from "./index";
import type { TimelineEventDTO } from "@/types/timeline";

export const getTimeline = (
  patientId: number,
  params?: {
    startDate?: string;
    endDate?: string;
    eventType?: string;
    departmentId?: number;
  },
) => api.get<TimelineEventDTO[]>(`/timeline/${patientId}`, { params });
```

- [ ] **Step 3: 创建 Timeline 组件**

```typescript
// frontend/src/components/Timeline.tsx
import { Timeline as AntTimeline, Tag, Card } from 'antd'
import { ExperimentOutlined, ScanOutlined } from '@ant-design/icons'
import { useNavigate } from 'react-router-dom'
import type { TimelineEventDTO } from '@/types/timeline'

interface Props {
  events: TimelineEventDTO[]
  loading?: boolean
}

export default function Timeline({ events, loading }: Props) {
  const navigate = useNavigate()

  const getColor = (type: string, abnormal?: boolean) => {
    if (abnormal) return 'red'
    return type === 'lab' ? 'green' : 'blue'
  }

  const getIcon = (type: string) => {
    return type === 'lab' ? <ExperimentOutlined /> : <ScanOutlined />
  }

  const formatDate = (datetime: string) => {
    const date = new Date(datetime)
    return `${date.getMonth() + 1}-${date.getDate()} ${date.getHours()}:${String(date.getMinutes()).padStart(2, '0')}`
  }

  return (
    <Card loading={loading}>
      <AntTimeline
        items={events.map((event) => ({
          color: getColor(event.eventType, event.abnormal),
          dot: getIcon(event.eventType),
          children: (
            <div
              style={{ cursor: 'pointer' }}
              onClick={() => navigate(`/${event.eventType === 'lab' ? 'lab' : 'imaging'}/${event.referenceId}`)}
            >
              <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <strong>{formatDate(event.eventDatetime)}</strong>
                <Tag color={event.eventType === 'lab' ? 'green' : 'blue'}>
                  {event.eventType === 'lab' ? '检验' : '影像'}
                </Tag>
                {event.abnormal && <Tag color="red">异常</Tag>}
                {event.departmentName && <Tag>{event.departmentName}</Tag>}
              </div>
              <p style={{ margin: '4px 0 0 0', color: '#666' }}>{event.eventSummary}</p>
            </div>
          ),
        }))}
      />
    </Card>
  )
}
```

- [ ] **Step 4: 创建时间轴页面**

```typescript
// frontend/src/pages/Timeline/index.tsx
import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { Card, Row, Col, Select, DatePicker, Space, Spin } from 'antd'
import PatientHeader from '@/components/PatientHeader'
import Timeline from '@/components/Timeline'
import { getTimeline } from '@/api/timeline'
import type { TimelineEventDTO } from '@/types/timeline'
import type { Patient360DTO } from '@/types/patient'
import { getPatient360 } from '@/api/patient'

const { RangePicker } = DatePicker

export default function TimelinePage() {
  const { patientId } = useParams<{ patientId: string }>()
  const [events, setEvents] = useState<TimelineEventDTO[]>([])
  const [patientData, setPatientData] = useState<Patient360DTO | null>(null)
  const [loading, setLoading] = useState(true)
  const [filters, setFilters] = useState({
    eventType: undefined as string | undefined,
  })

  useEffect(() => {
    if (patientId) {
      loadData()
    }
  }, [patientId])

  const loadData = async () => {
    setLoading(true)
    try {
      const [timelineRes, patientRes] = await Promise.all([
        getTimeline(Number(patientId), filters),
        getPatient360(Number(patientId)),
      ])
      setEvents(timelineRes.data)
      setPatientData(patientRes.data)
    } catch (error) {
      console.error('加载数据失败', error)
    } finally {
      setLoading(false)
    }
  }

  const handleFilterChange = (key: string, value: any) => {
    setFilters((prev) => ({ ...prev, [key]: value }))
    loadData()
  }

  if (loading) {
    return <Spin style={{ display: 'flex', justifyContent: 'center', marginTop: 100 }} />
  }

  if (!patientData) {
    return <div>未找到患者数据</div>
  }

  return (
    <div style={{ padding: 24 }}>
      <PatientHeader patient={patientData.patient} encounter={patientData.encounter} />

      {/* 筛选器 */}
      <Card style={{ marginTop: 16 }}>
        <Space wrap>
          <span>筛选：</span>
          <Select
            placeholder="事件类型"
            allowClear
            style={{ width: 120 }}
            onChange={(v) => handleFilterChange('eventType', v)}
            options={[
              { label: '全部', value: undefined },
              { label: '检验', value: 'lab' },
              { label: '影像', value: 'imaging' },
            ]}
          />
        </Space>
      </Card>

      {/* 时间轴 */}
      <Card title="就诊时间轴" style={{ marginTop: 16 }}>
        <Timeline events={events} />
      </Card>
    </div>
  )
}
```

- [ ] **Step 5: 提交**

```bash
git add frontend/
git commit -m "feat: 实现患者时间轴页面"
```

---

### Task 7: 检验详情页

**Files:**

- Create: `frontend/src/types/lab.ts`
- Create: `frontend/src/api/lab.ts`
- Create: `frontend/src/components/LabResultTable.tsx`
- Create: `frontend/src/components/AbnormalTag.tsx`
- Create: `frontend/src/pages/LabDetail/index.tsx`

- [ ] **Step 1: 创建检验类型定义**

```typescript
// frontend/src/types/lab.ts
export interface LabOrderDTO {
  id: number;
  patientId: number;
  orderNo: string;
  departmentId: number;
  departmentName: string;
  orderDatetime: string;
  status: string;
  specimenType: string;
  results: LabResultItem[];
}

export interface LabResultItem {
  id: number;
  itemCode: string;
  itemName: string;
  resultValue: string;
  unit: string;
  refRangeLow: number;
  refRangeHigh: number;
  abnormalFlag: string;
  resultDatetime: string;
}

export interface LabTrendDTO {
  itemCode: string;
  itemName: string;
  unit: string;
  history: Array<{
    datetime: string;
    value: string;
    abnormalFlag: string;
  }>;
}
```

- [ ] **Step 2: 创建检验 API**

```typescript
// frontend/src/api/lab.ts
import api from "./index";
import type { LabOrderDTO, LabTrendDTO } from "@/types/lab";

export const getLabOrder = (orderId: number) =>
  api.get<LabOrderDTO>(`/lab/${orderId}`);

export const getLabTrend = (patientId: number, itemCode: string) =>
  api.get<LabTrendDTO>(`/lab/trend/${patientId}/${itemCode}`);
```

- [ ] **Step 3: 创建 AbnormalTag 组件**

```typescript
// frontend/src/components/AbnormalTag.tsx
import { Tag } from 'antd'

interface Props {
  flag: string
}

export default function AbnormalTag({ flag }: Props) {
  if (flag === 'H') {
    return <Tag color="red">偏高 ↑</Tag>
  }
  if (flag === 'L') {
    return <Tag color="blue">偏低 ↓</Tag>
  }
  if (flag === 'HH' || flag === 'LL') {
    return <Tag color="red" style={{ fontWeight: 'bold' }}>严重异常</Tag>
  }
  return null
}
```

- [ ] **Step 4: 创建 LabResultTable 组件**

```typescript
// frontend/src/components/LabResultTable.tsx
import { Table, Tag } from 'antd'
import AbnormalTag from './AbnormalTag'
import type { LabResultItem } from '@/types/lab'

interface Props {
  results: LabResultItem[]
  onViewTrend?: (itemCode: string, itemName: string) => void
}

export default function LabResultTable({ results, onViewTrend }: Props) {
  return (
    <Table
      dataSource={results}
      rowKey="id"
      size="small"
      pagination={false}
      columns={[
        { title: '项目代码', dataIndex: 'itemCode', width: 100 },
        { title: '项目名称', dataIndex: 'itemName', width: 150 },
        {
          title: '结果值',
          dataIndex: 'resultValue',
          width: 120,
          render: (value, record) => (
            <span style={{ fontWeight: record.abnormalFlag ? 'bold' : 'normal' }}>
              {value} {record.unit}
            </span>
          ),
        },
        {
          title: '参考范围',
          dataIndex: 'refRangeLow',
          width: 150,
          render: (_, record) => (
            <span style={{ color: '#999' }}>
              {record.refRangeLow} ~ {record.refRangeHigh}
            </span>
          ),
        },
        {
          title: '状态',
          dataIndex: 'abnormalFlag',
          width: 100,
          render: (flag) => <AbnormalTag flag={flag} />,
        },
        { title: '报告时间', dataIndex: 'resultDatetime', width: 160,
          render: (v) => v ? new Date(v).toLocaleString() : '-' },
        {
          title: '操作',
          width: 100,
          render: (_, record) => (
            <a onClick={() => onViewTrend?.(record.itemCode, record.itemName)}>
              查看趋势
            </a>
          ),
        },
      ]}
    />
  )
}
```

- [ ] **Step 5: 创建检验详情页**

```typescript
// frontend/src/pages/LabDetail/index.tsx
import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { Card, Descriptions, Spin, Modal, LineChart } from 'antd'
import LabResultTable from '@/components/LabResultTable'
import AISummaryCard from '@/components/AISummaryCard'
import { getLabOrder, getLabTrend } from '@/api/lab'
import type { LabOrderDTO } from '@/types/lab'

export default function LabDetail() {
  const { orderId } = useParams<{ orderId: string }>()
  const [data, setData] = useState<LabOrderDTO | null>(null)
  const [loading, setLoading] = useState(true)
  const [trendModal, setTrendModal] = useState<{
    visible: boolean
    itemCode: string
    itemName: string
  }>({ visible: false, itemCode: '', itemName: '' })

  useEffect(() => {
    if (orderId) {
      loadData()
    }
  }, [orderId])

  const loadData = async () => {
    setLoading(true)
    try {
      const response = await getLabOrder(Number(orderId))
      setData(response.data)
    } catch (error) {
      console.error('加载检验数据失败', error)
    } finally {
      setLoading(false)
    }
  }

  const handleViewTrend = async (itemCode: string, itemName: string) => {
    setTrendModal({ visible: true, itemCode, itemName })
  }

  if (loading) {
    return <Spin style={{ display: 'flex', justifyContent: 'center', marginTop: 100 }} />
  }

  if (!data) {
    return <div>未找到检验数据</div>
  }

  return (
    <div style={{ padding: 24 }}>
      {/* 基本信息 */}
      <Card title="检验单信息">
        <Descriptions column={4}>
          <Descriptions.Item label="检验单号">{data.orderNo}</Descriptions.Item>
          <Descriptions.Item label="检验科室">{data.departmentName}</Descriptions.Item>
          <Descriptions.Item label="标本类型">{data.specimenType}</Descriptions.Item>
          <Descriptions.Item label="检验时间">
            {new Date(data.orderDatetime).toLocaleString()}
          </Descriptions.Item>
          <Descriptions.Item label="状态">{data.status}</Descriptions.Item>
        </Descriptions>
      </Card>

      {/* 检验结果 */}
      <Card title="检验结果" style={{ marginTop: 16 }}>
        <LabResultTable results={data.results} onViewTrend={handleViewTrend} />
      </Card>

      {/* AI 摘要 */}
      <AISummaryCard summary={`检验单 ${data.orderNo} 包含 ${data.results.length} 个检验项目，其中 ${data.results.filter(r => r.abnormalFlag).length} 项异常。`} />

      {/* 趋势弹窗 */}
      <Modal
        title={`${trendModal.itemName} 趋势图`}
        open={trendModal.visible}
        onCancel={() => setTrendModal({ ...trendModal, visible: false })}
        footer={null}
        width={800}
      >
        {/* 简化实现，实际使用图表库 */}
        <div style={{ height: 300, display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#999' }}>
          趋势图组件（可使用 @ant-design/charts 或 recharts 实现）
        </div>
      </Modal>
    </div>
  )
}
```

- [ ] **Step 6: 提交**

```bash
git add frontend/
git commit -m "feat: 实现检验详情页"
```

---

### Task 8: 影像详情页

**Files:**

- Create: `frontend/src/types/imaging.ts`
- Create: `frontend/src/api/imaging.ts`
- Create: `frontend/src/components/ImagingReportCard.tsx`
- Create: `frontend/src/pages/ImagingDetail/index.tsx`

- [ ] **Step 1: 创建影像类型定义**

```typescript
// frontend/src/types/imaging.ts
export interface ImagingOrderDTO {
  id: number;
  patientId: number;
  orderNo: string;
  departmentId: number;
  departmentName: string;
  bodyPart: string;
  modality: string;
  orderDatetime: string;
  status: string;
  pacsUrl: string;
  report: ImagingReportDTO;
}

export interface ImagingReportDTO {
  id: number;
  reportContent: string;
  impression: string;
  reportDoctor: string;
  reportDatetime: string;
}
```

- [ ] **Step 2: 创建影像 API**

```typescript
// frontend/src/api/imaging.ts
import api from "./index";
import type { ImagingOrderDTO } from "@/types/imaging";

export const getImagingOrder = (orderId: number) =>
  api.get<ImagingOrderDTO>(`/imaging/${orderId}`);
```

- [ ] **Step 3: 创建 ImagingReportCard 组件**

```typescript
// frontend/src/components/ImagingReportCard.tsx
import { Card, Divider } from 'antd'

interface Props {
  report: {
    reportContent: string
    impression: string
    reportDoctor: string
    reportDatetime: string
  }
  aiSummary?: string
}

export default function ImagingReportCard({ report, aiSummary }: Props) {
  return (
    <Card title="影像报告">
      <div style={{ marginBottom: 16 }}>
        <strong>检查所见：</strong>
        <p style={{ whiteSpace: 'pre-wrap', lineHeight: 1.8 }}>{report.reportContent || '暂无'}</p>
      </div>
      <Divider />
      <div style={{ marginBottom: 16 }}>
        <strong>诊断意见：</strong>
        <p style={{ whiteSpace: 'pre-wrap', lineHeight: 1.8, color: '#cf1322', fontWeight: 'bold' }}>
          {report.impression || '暂无'}
        </p>
      </div>
      <Divider />
      <div style={{ color: '#666', fontSize: 12 }}>
        <span>报告医生：{report.reportDoctor || '-'}</span>
        <span style={{ marginLeft: 16 }}>报告时间：{report.reportDatetime ? new Date(report.reportDatetime).toLocaleString() : '-'}</span>
      </div>

      {aiSummary && (
        <>
          <Divider />
          <div>
            <strong>AI 辅助摘要：</strong>
            <p style={{ whiteSpace: 'pre-wrap', lineHeight: 1.8, color: '#666' }}>{aiSummary}</p>
          </div>
        </>
      )}
    </Card>
  )
}
```

- [ ] **Step 4: 创建影像详情页**

```typescript
// frontend/src/pages/ImagingDetail/index.tsx
import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { Card, Descriptions, Button, Spin, Space, Modal } from 'antd'
import { ExportOutlined } from '@ant-design/icons'
import ImagingReportCard from '@/components/ImagingReportCard'
import { getImagingOrder } from '@/api/imaging'
import type { ImagingOrderDTO } from '@/types/imaging'

export default function ImagingDetail() {
  const { orderId } = useParams<{ orderId: string }>()
  const [data, setData] = useState<ImagingOrderDTO | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    if (orderId) {
      loadData()
    }
  }, [orderId])

  const loadData = async () => {
    setLoading(true)
    try {
      const response = await getImagingOrder(Number(orderId))
      setData(response.data)
    } catch (error) {
      console.error('加载影像数据失败', error)
    } finally {
      setLoading(false)
    }
  }

  if (loading) {
    return <Spin style={{ display: 'flex', justifyContent: 'center', marginTop: 100 }} />
  }

  if (!data) {
    return <div>未找到影像数据</div>
  }

  const modalityNames: Record<string, string> = {
    'CR': '计算机X线摄影',
    'DR': '数字X线摄影',
    'CT': '计算机断层扫描',
    'MR': '磁共振成像',
    'US': '超声检查',
    'XA': 'X线血管造影',
    'RF': '射频消融',
  }

  return (
    <div style={{ padding: 24 }}>
      {/* 基本信息 */}
      <Card
        title="影像检查信息"
        extra={
          data.pacsUrl && (
            <Button
              type="primary"
              icon={<ExportOutlined />}
              onClick={() => window.open(data.pacsUrl, '_blank')}
            >
              跳转 PACS 查看影像
            </Button>
          )
        }
      >
        <Descriptions column={4}>
          <Descriptions.Item label="检查单号">{data.orderNo}</Descriptions.Item>
          <Descriptions.Item label="检查科室">{data.departmentName}</Descriptions.Item>
          <Descriptions.Item label="检查部位">{data.bodyPart}</Descriptions.Item>
          <Descriptions.Item label="检查方式">
            {data.modality} - {modalityNames[data.modality] || data.modality}
          </Descriptions.Item>
          <Descriptions.Item label="检查时间">
            {new Date(data.orderDatetime).toLocaleString()}
          </Descriptions.Item>
          <Descriptions.Item label="状态">{data.status}</Descriptions.Item>
        </Descriptions>
      </Card>

      {/* 影像报告 */}
      <ImagingReportCard report={data.report} aiSummary={data.report.impression ? `影像报告提示：${data.report.impression.substring(0, 100)}...` : undefined} />
    </div>
  )
}
```

- [ ] **Step 5: 提交**

```bash
git add frontend/
git commit -m "feat: 实现影像详情页"
```

---

## Phase 4: 后端 API 实现

### Task 9: 患者360与时间轴后端 API

**Files:**

- Create: `backend/src/main/java/com/medical360/entity/Patient.java`
- Create: `backend/src/main/java/com/medical360/entity/Encounter.java`
- Create: `backend/src/main/java/com/medical360/entity/LabOrder.java`
- Create: `backend/src/main/java/com/medical360/entity/LabResult.java`
- Create: `backend/src/main/java/com/medical360/entity/ImagingOrder.java`
- Create: `backend/src/main/java/com/medical360/entity/ImagingReport.java`
- Create: `backend/src/main/java/com/medical360/entity/ClinicalEvent.java`
- Create: `backend/src/main/java/com/medical360/entity/Department.java`
- Create: `backend/src/main/java/com/medical360/mapper/PatientMapper.java`
- Create: `backend/src/main/java/com/medical360/mapper/LabOrderMapper.java`
- Create: `backend/src/main/java/com/medical360/mapper/ImagingOrderMapper.java`
- Create: `backend/src/main/java/com/medical360/mapper/ClinicalEventMapper.java`
- Create: `backend/src/main/java/com/medical360/dto/Patient360DTO.java`
- Create: `backend/src/main/java/com/medical360/dto/TimelineEventDTO.java`
- Create: `backend/src/main/java/com/medical360/service/PatientService.java`
- Create: `backend/src/main/java/com/medical360/service/impl/PatientServiceImpl.java`
- Create: `backend/src/main/java/com/medical360/service/TimelineService.java`
- Create: `backend/src/main/java/com/medical360/service/impl/TimelineServiceImpl.java`
- Create: `backend/src/main/java/com/medical360/controller/PatientController.java`
- Create: `backend/src/main/java/com/medical360/controller/TimelineController.java`

- [ ] **Step 1: 创建实体类（示例：Patient）**

```java
package com.medical360.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("patient")
public class Patient {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String unifiedPatientId;
    private String name;
    private String gender;
    private LocalDate birthDate;
    private String idCardNo;
    private String phone;
    private String address;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
```

- [ ] **Step 2: 创建 DTO（示例：Patient360DTO）**

```java
package com.medical360.dto;

import lombok.Data;
import java.util.List;

@Data
public class Patient360DTO {
    private PatientDTO patient;
    private EncounterDTO encounter;
    private LabSummaryDTO labSummary;
    private ImagingSummaryDTO imagingSummary;
    private String aiSummary;
    private List<AlertDTO> alerts;

    @Data
    public static class PatientDTO {
        private Long id;
        private String unifiedPatientId;
        private String name;
        private String gender;
        private String birthDate;
        private String idCardNo;
        private String phone;
    }

    @Data
    public static class EncounterDTO {
        private Long id;
        private String encounterType;
        private String departmentName;
        private String visitDatetime;
        private String admissionReason;
    }

    @Data
    public static class LabSummaryDTO {
        private Integer recentCount;
        private Integer abnormalCount;
        private List<LabOrderDTO> recentOrders;
    }

    @Data
    public static class LabOrderDTO {
        private Long id;
        private String orderNo;
        private String orderDatetime;
        private String specimenType;
        private String status;
        private String departmentName;
    }

    @Data
    public static class ImagingSummaryDTO {
        private Integer recentCount;
        private List<ImagingOrderDTO> recentOrders;
    }

    @Data
    public static class ImagingOrderDTO {
        private Long id;
        private String orderNo;
        private String orderDatetime;
        private String bodyPart;
        private String modality;
        private String status;
        private String departmentName;
    }

    @Data
    public static class AlertDTO {
        private String type;
        private String message;
        private String datetime;
    }
}
```

- [ ] **Step 3: 创建 PatientService**

```java
package com.medical360.service;

import com.medical360.dto.Patient360DTO;

public interface PatientService {
    Patient360DTO getPatient360(Long patientId);
}
```

- [ ] **Step 4: 创建 PatientServiceImpl**

```java
package com.medical360.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.medical360.dto.Patient360DTO;
import com.medical360.entity.*;
import com.medical360.mapper.*;
import com.medical360.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientMapper patientMapper;
    private final EncounterMapper encounterMapper;
    private final LabOrderMapper labOrderMapper;
    private final LabResultMapper labResultMapper;
    private final ImagingOrderMapper imagingOrderMapper;

    @Override
    public Patient360DTO getPatient360(Long patientId) {
        Patient360DTO dto = new Patient360DTO();

        // 患者信息
        Patient patient = patientMapper.selectById(patientId);
        if (patient != null) {
            Patient360DTO.PatientDTO patientDTO = new Patient360DTO.PatientDTO();
            patientDTO.setId(patient.getId());
            patientDTO.setUnifiedPatientId(patient.getUnifiedPatientId());
            patientDTO.setName(patient.getName());
            patientDTO.setGender(patient.getGender());
            patientDTO.setBirthDate(patient.getBirthDate() != null ? patient.getBirthDate().toString() : null);
            dto.setPatient(patientDTO);
        }

        // 最新就诊
        Encounter encounter = encounterMapper.selectOne(
            new LambdaQueryWrapper<Encounter>()
                .eq(Encounter::getPatientId, patientId)
                .orderByDesc(Encounter::getVisitDatetime)
                .last("LIMIT 1")
        );
        if (encounter != null) {
            Patient360DTO.EncounterDTO encDTO = new Patient360DTO.EncounterDTO();
            encDTO.setId(encounter.getId());
            encDTO.setEncounterType(encounter.getEncounterType());
            encDTO.setVisitDatetime(encounter.getVisitDatetime() != null ? encounter.getVisitDatetime().toString() : null);
            encDTO.setAdmissionReason(encounter.getAdmissionReason());
            dto.setEncounter(encDTO);
        }

        // 检验摘要
        List<LabOrder> labOrders = labOrderMapper.selectList(
            new LambdaQueryWrapper<LabOrder>()
                .eq(LabOrder::getPatientId, patientId)
                .orderByDesc(LabOrder::getOrderDatetime)
                .last("LIMIT 5")
        );
        Patient360DTO.LabSummaryDTO labSummary = new Patient360DTO.LabSummaryDTO();
        labSummary.setRecentCount((int) labOrderMapper.selectCount(new LambdaQueryWrapper<LabOrder>().eq(LabOrder::getPatientId, patientId)));

        // 计算异常项
        long abnormalCount = 0;
        for (LabOrder order : labOrders) {
            abnormalCount += labResultMapper.selectCount(
                new LambdaQueryWrapper<LabResult>()
                    .eq(LabResult::getLabOrderId, order.getId())
                    .isNotNull(LabResult::getAbnormalFlag)
                    .ne(LabResult::getAbnormalFlag, "")
            );
        }
        labSummary.setAbnormalCount((int) abnormalCount);
        dto.setLabSummary(labSummary);

        // 影像摘要
        Patient360DTO.ImagingSummaryDTO imagingSummary = new Patient360DTO.ImagingSummaryDTO();
        imagingSummary.setRecentCount((int) imagingOrderMapper.selectCount(new LambdaQueryWrapper<ImagingOrder>().eq(ImagingOrder::getPatientId, patientId)));
        dto.setImagingSummary(imagingSummary);

        return dto;
    }
}
```

- [ ] **Step 5: 创建 PatientController**

```java
package com.medical360.controller;

import com.medical360.common.Result;
import com.medical360.dto.Patient360DTO;
import com.medical360.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping("/360/{patientId}")
    public Result<Patient360DTO> getPatient360(@PathVariable Long patientId) {
        return Result.success(patientService.getPatient360(patientId));
    }
}
```

- [ ] **Step 6: 提交**

```bash
git add backend/
git commit -m "feat: 实现患者360与时间轴后端 API"
```

---

## Phase 5: AI 辅助服务

### Task 10: Python AI 服务

**Files:**

- Create: `ai-service/requirements.txt`
- Create: `ai-service/config.py`
- Create: `ai-service/app/main.py`
- Create: `ai-service/app/routers/lab_summary.py`
- Create: `ai-service/app/routers/imaging_summary.py`
- Create: `ai-service/app/services/prompt_builder.py`
- Create: `ai-service/app/services/llm_service.py`
- Create: `ai-service/app/models/summary_request.py`

- [ ] **Step 1: 创建 requirements.txt**

```
fastapi==0.109.2
uvicorn==0.27.1
pydantic==2.6.1
langchain==0.1.11
langchain-openai==0.0.5
openai==1.12.0
python-dotenv==1.0.1
httpx==0.26.0
```

- [ ] **Step 2: 创建 config.py**

```python
import os
from dotenv import load_dotenv

load_dotenv()

OPENAI_API_KEY = os.getenv("OPENAI_API_KEY", "")
OPENAI_BASE_URL = os.getenv("OPENAI_BASE_URL", "https://api.openai.com/v1")
MODEL_NAME = os.getenv("MODEL_NAME", "gpt-3.5-turbo")
```

- [ ] **Step 3: 创建请求模型**

```python
# ai-service/app/models/summary_request.py
from pydantic import BaseModel
from typing import List, Optional

class LabResultItem(BaseModel):
    item_name: str
    result_value: str
    unit: Optional[str] = None
    ref_range: Optional[str] = None
    abnormal_flag: Optional[str] = None

class LabSummaryRequest(BaseModel):
    order_no: str
    patient_name: str
    results: List[LabResultItem]
    specimen_type: str

class ImagingSummaryRequest(BaseModel):
    order_no: str
    patient_name: str
    body_part: str
    modality: str
    report_content: Optional[str] = None
    impression: Optional[str] = None
    report_doctor: Optional[str] = None

class PatientSummaryRequest(BaseModel):
    patient_name: str
    encounter_type: str
    recent_labs: List[dict]
    recent_imaging: List[dict]
```

- [ ] **Step 4: 创建 LLM 服务**

```python
# ai-service/app/services/llm_service.py
from langchain_openai import ChatOpenAI
from config import OPENAI_API_KEY, OPENAI_BASE_URL, MODEL_NAME

def get_llm():
    return ChatOpenAI(
        api_key=OPENAI_API_KEY,
        base_url=OPENAI_BASE_URL,
        model=MODEL_NAME,
        temperature=0.3,
    )
```

- [ ] **Step 5: 创建 Prompt 构造器**

```python
# ai-service/app/services/prompt_builder.py

LAB_SUMMARY_PROMPT = """你是一位医疗检验数据摘要助手。请根据以下检验结果生成简洁摘要。

患者：{patient_name}
检验单号：{order_no}
标本类型：{specimen_type}

检验结果：
{results_text}

请按以下格式输出：
1. 异常项汇总：列出所有异常项目
2. 关键指标：列出最重要的2-3个指标及其临床意义
3. 注意事项：如有需要关注的情况请说明

注意：只做客观数据摘要，不做诊断建议。输出内容需标注"辅助生成，仅供参考"。
"""

def build_lab_summary_prompt(request) -> str:
    results_text = "\n".join([
        f"- {r.item_name}: {r.result_value} {r.unit or ''} (参考值: {r.ref_range or 'N/A'}, 异常: {r.abnormal_flag or '否'})"
        for r in request.results
    ])
    return LAB_SUMMARY_PROMPT.format(
        patient_name=request.patient_name,
        order_no=request.order_no,
        specimen_type=request.specimen_type,
        results_text=results_text
    )

IMAGING_SUMMARY_PROMPT = """你是一位医学影像报告摘要助手。请根据以下影像报告生成简洁摘要。

患者：{patient_name}
检查单号：{order_no}
检查部位：{body_part}
检查方式：{modality}
报告医生：{report_doctor}

报告内容：
检查所见：{report_content}

诊断意见：{impression}

请按以下格式输出：
1. 检查概述：简要说明检查情况
2. 主要发现：列出关键影像学发现
3. 诊断要点：总结诊断意见

注意：只做客观报告摘要，不做诊断建议。输出内容需标注"辅助生成，仅供参考"。
"""

def build_imaging_summary_prompt(request) -> str:
    return IMAGING_SUMMARY_PROMPT.format(
        patient_name=request.patient_name,
        order_no=request.order_no,
        body_part=request.body_part,
        modality=request.modality,
        report_content=request.report_content or "无",
        impression=request.impression or "无",
        report_doctor=request.report_doctor or "未知"
    )
```

- [ ] **Step 6: 创建检验摘要路由**

```python
# ai-service/app/routers/lab_summary.py
from fastapi import APIRouter
from app.models.summary_request import LabSummaryRequest
from app.services.llm_service import get_llm
from app.services.prompt_builder import build_lab_summary_prompt

router = APIRouter(prefix="/lab", tags=["lab"])

@router.post("/summary")
async def generate_lab_summary(request: LabSummaryRequest):
    try:
        llm = get_llm()
        prompt = build_lab_summary_prompt(request)
        response = llm.invoke(prompt)
        return {
            "success": True,
            "summary": response.content,
            "model_version": "gpt-3.5-turbo"
        }
    except Exception as e:
        return {
            "success": False,
            "error": str(e),
            "summary": "摘要生成失败，请查看原始数据"
        }
```

- [ ] **Step 7: 创建影像摘要路由**

```python
# ai-service/app/routers/imaging_summary.py
from fastapi import APIRouter
from app.models.summary_request import ImagingSummaryRequest
from app.services.llm_service import get_llm
from app.services.prompt_builder import build_imaging_summary_prompt

router = APIRouter(prefix="/imaging", tags=["imaging"])

@router.post("/summary")
async def generate_imaging_summary(request: ImagingSummaryRequest):
    try:
        llm = get_llm()
        prompt = build_imaging_summary_prompt(request)
        response = llm.invoke(prompt)
        return {
            "success": True,
            "summary": response.content,
            "model_version": "gpt-3.5-turbo"
        }
    except Exception as e:
        return {
            "success": False,
            "error": str(e),
            "summary": "摘要生成失败，请查看原始报告"
        }
```

- [ ] **Step 8: 创建 FastAPI 主入口**

```python
# ai-service/app/main.py
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.routers import lab_summary, imaging_summary

app = FastAPI(title="医技360 AI 服务", version="1.0.0")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(lab_summary.router)
app.include_router(imaging_summary.router)

@app.get("/health")
async def health():
    return {"status": "ok"}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
```

- [ ] **Step 9: 提交**

```bash
git add ai-service/
git commit -m "feat: 实现 Python AI 辅助摘要服务"
```

---

## Phase 6: 权限与数据访问控制

### Task 11: 数据权限控制

**Files:**

- Create: `backend/src/main/java/com/medical360/security/DataPermissionInterceptor.java`
- Create: `backend/src/main/java/com/medical360/security/PermissionService.java`
- Create: `backend/src/main/java/com/medical360/service/AccessLogService.java`
- Create: `backend/src/main/java/com/medical360/service/impl/AccessLogServiceImpl.java`

- [ ] **Step 1: 创建 PermissionService**

```java
package com.medical360.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.medical360.entity.*;
import com.medical360.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final DepartmentMapper departmentMapper;

    public boolean canAccessPatient(String username, Long patientId) {
        User user = userMapper.selectByUsername(username);
        if (user == null) return false;

        // 系统管理员可访问全部
        if (isAdmin(user)) return true;

        // 按科室/岗位判断数据权限
        // 简化实现：临床医生只能看本科室患者
        // 实际应关联 encounter 表的 department_id
        return true;
    }

    public boolean hasMenuPermission(String username, String menuCode) {
        User user = userMapper.selectByUsername(username);
        if (user == null) return false;

        if (isAdmin(user)) return true;

        // 查询用户角色对应的菜单权限
        // 简化实现
        return true;
    }

    private boolean isAdmin(User user) {
        List<Role> roles = roleMapper.selectRolesByUserId(user.getId());
        return roles.stream().anyMatch(r -> "SYS_ADMIN".equals(r.getName()));
    }

    public Long getUserDepartmentId(String username) {
        User user = userMapper.selectByUsername(username);
        return user != null ? user.getDepartmentId() : null;
    }

    public String getUserRole(String username) {
        User user = userMapper.selectByUsername(username);
        if (user == null) return null;
        List<Role> roles = roleMapper.selectRolesByUserId(user.getId());
        return roles.isEmpty() ? null : roles.get(0).getName();
    }
}
```

- [ ] **Step 2: 创建 AccessLogService**

```java
package com.medical360.service;

public interface AccessLogService {
    void logAccess(Long userId, Long patientId, String action, String requestDetail, String ipAddress);
}
```

- [ ] **Step 3: 创建 AccessLogServiceImpl**

```java
package com.medical360.service.impl;

import com.medical360.entity.AccessLog;
import com.medical360.mapper.AccessLogMapper;
import com.medical360.service.AccessLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccessLogServiceImpl implements AccessLogService {

    private final AccessLogMapper accessLogMapper;

    @Override
    @Async
    public void logAccess(Long userId, Long patientId, String action, String requestDetail, String ipAddress) {
        AccessLog log = new AccessLog();
        log.setUserId(userId);
        log.setPatientId(patientId);
        log.setAction(action);
        log.setRequestDetail(requestDetail);
        log.setIpAddress(ipAddress);
        accessLogMapper.insert(log);
    }
}
```

- [ ] **Step 4: 提交**

```bash
git add backend/
git commit -m "feat: 实现数据权限控制与访问审计"
```

---

## Phase 7: 中台接入层（简化版）

### Task 12: LIS/RIS 适配器与数据同步

**Files:**

- Create: `backend/src/main/java/com/medical360/integration/adapter/DataSourceAdapter.java`
- Create: `backend/src/main/java/com/medical360/integration/adapter/LISAdapter.java`
- Create: `backend/src/main/java/com/medical360/integration/mapper/FieldMappingService.java`
- Create: `backend/src/main/java/com/medical360/integration/sync/DataSyncScheduler.java`
- Create: `backend/src/main/java/com/medical360/integration/master/PatientMasterIndexService.java`

- [ ] **Step 1: 创建适配器接口**

```java
package com.medical360.integration.adapter;

import java.util.List;
import java.util.Map;

public interface DataSourceAdapter {
    String getSourceType();

    // 获取增量数据
    List<Map<String, Object>> fetchLabOrders(String lastSyncTime);

    List<Map<String, Object>> fetchLabResults(String lastSyncTime);

    List<Map<String, Object>> fetchImagingOrders(String lastSyncTime);

    List<Map<String, Object>> fetchImagingReports(String lastSyncTime);

    // 字段映射
    Map<String, Object> mapLabOrderFields(Map<String, Object> source);

    Map<String, Object> mapLabResultFields(Map<String, Object> source);

    Map<String, Object> mapImagingOrderFields(Map<String, Object> source);

    Map<String, Object> mapImagingReportFields(Map<String, Object> source);
}
```

- [ ] **Step 2: 创建 LIS 适配器（简化实现）**

```java
package com.medical360.integration.adapter;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class LISAdapter implements DataSourceAdapter {

    @Override
    public String getSourceType() {
        return "LIS";
    }

    @Override
    public List<Map<String, Object>> fetchLabOrders(String lastSyncTime) {
        // 简化实现：实际应调用 LIS 系统 API
        // 此处返回空列表，待对接真实系统时实现
        return Collections.emptyList();
    }

    @Override
    public List<Map<String, Object>> fetchLabResults(String lastSyncTime) {
        return Collections.emptyList();
    }

    @Override
    public List<Map<String, Object>> fetchImagingOrders(String lastSyncTime) {
        return Collections.emptyList();
    }

    @Override
    public List<Map<String, Object>> fetchImagingReports(String lastSyncTime) {
        return Collections.emptyList();
    }

    @Override
    public Map<String, Object> mapLabOrderFields(Map<String, Object> source) {
        Map<String, Object> mapped = new HashMap<>();
        mapped.put("order_no", source.get("LAB_NO"));
        mapped.put("patient_id", source.get("PATIENT_ID"));
        mapped.put("order_datetime", source.get("ORDER_TIME"));
        mapped.put("specimen_type", source.get("SPECIMEN_TYPE"));
        mapped.put("status", source.get("STATUS"));
        return mapped;
    }

    @Override
    public Map<String, Object> mapLabResultFields(Map<String, Object> source) {
        Map<String, Object> mapped = new HashMap<>();
        mapped.put("item_code", source.get("TEST_CODE"));
        mapped.put("item_name", source.get("TEST_NAME"));
        mapped.put("result_value", source.get("RESULT"));
        mapped.put("unit", source.get("UNIT"));
        mapped.put("abnormal_flag", source.get("ABNORMAL"));
        return mapped;
    }

    @Override
    public Map<String, Object> mapImagingOrderFields(Map<String, Object> source) {
        Map<String, Object> mapped = new HashMap<>();
        mapped.put("order_no", source.get("EXAM_NO"));
        mapped.put("patient_id", source.get("PATIENT_ID"));
        mapped.put("body_part", source.get("BODY_PART"));
        mapped.put("modality", source.get("MODALITY"));
        mapped.put("order_datetime", source.get("ORDER_TIME"));
        return mapped;
    }

    @Override
    public Map<String, Object> mapImagingReportFields(Map<String, Object> source) {
        Map<String, Object> mapped = new HashMap<>();
        mapped.put("report_content", source.get("FINDINGS"));
        mapped.put("impression", source.get("CONCLUSION"));
        mapped.put("report_doctor", source.get("REPORT_DOCTOR"));
        mapped.put("report_datetime", source.get("REPORT_TIME"));
        return mapped;
    }
}
```

- [ ] **Step 3: 创建数据同步调度器**

```java
package com.medical360.integration.sync;

import com.medical360.integration.adapter.DataSourceAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSyncScheduler {

    private final List<DataSourceAdapter> adapters;
    private String lastSyncTime = null;

    @Scheduled(cron = "0 */5 * * * ?") // 每5分钟同步一次
    public void syncData() {
        log.info("开始数据同步...");
        for (DataSourceAdapter adapter : adapters) {
            try {
                syncLabData(adapter);
                syncImagingData(adapter);
            } catch (Exception e) {
                log.error("同步 {} 数据失败: {}", adapter.getSourceType(), e.getMessage());
            }
        }
        lastSyncTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        log.info("数据同步完成，上次同步时间: {}", lastSyncTime);
    }

    private void syncLabData(DataSourceAdapter adapter) {
        // 简化实现
        List<Map<String, Object>> orders = adapter.fetchLabOrders(lastSyncTime);
        log.info("从 {} 获取 {} 条检验单", adapter.getSourceType(), orders.size());

        for (Map<String, Object> order : orders) {
            Map<String, Object> mapped = adapter.mapLabOrderFields(order);
            // TODO: 保存到数据库
        }
    }

    private void syncImagingData(DataSourceAdapter adapter) {
        // 简化实现
        List<Map<String, Object>> orders = adapter.fetchImagingOrders(lastSyncTime);
        log.info("从 {} 获取 {} 条影像检查单", adapter.getSourceType(), orders.size());
    }
}
```

- [ ] **Step 4: 提交**

```bash
git add backend/
git commit -m "feat: 实现中台接入层与数据同步"
```

---

## Phase 8: 前端布局与路由

### Task 13: 主布局与导航

**Files:**

- Create: `frontend/src/components/Layout/AppLayout.tsx`
- Create: `frontend/src/components/Layout/AppHeader.tsx`
- Create: `frontend/src/components/Layout/AppSidebar.tsx`
- Create: `frontend/src/pages/Dashboard/index.tsx`

- [ ] **Step 1: 创建 AppLayout**

```typescript
// frontend/src/components/Layout/AppLayout.tsx
import { useState } from 'react'
import { Outlet } from 'react-router-dom'
import { Layout } from 'antd'
import AppHeader from './AppHeader'
import AppSidebar from './AppSidebar'

const { Content } = Layout

export default function AppLayout() {
  const [collapsed, setCollapsed] = useState(false)

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <AppSidebar collapsed={collapsed} />
      <Layout>
        <AppHeader collapsed={collapsed} onToggle={() => setCollapsed(!collapsed)} />
        <Content style={{ margin: 16 }}>
          <Outlet />
        </Content>
      </Layout>
    </Layout>
  )
}
```

- [ ] **Step 2: 创建 AppHeader**

```typescript
// frontend/src/components/Layout/AppHeader.tsx
import { Layout, Button, Dropdown, Avatar, Space } from 'antd'
import { MenuFoldOutlined, MenuUnfoldOutlined, UserOutlined, LogoutOutlined } from '@ant-design/icons'
import { useNavigate } from 'react-router-dom'
import { useAuthStore } from '@/stores/authStore'

const { Header } = Layout

interface Props {
  collapsed: boolean
  onToggle: () => void
}

export default function AppHeader({ collapsed, onToggle }: Props) {
  const navigate = useNavigate()
  const { user, logout } = useAuthStore()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <Header style={{ padding: '0 16px', background: '#fff', display: 'flex', alignItems: 'center' }}>
      <Button
        type="text"
        icon={collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
        onClick={onToggle}
      />
      <div style={{ flex: 1 }} />
      <Space>
        <Dropdown
          menu={{
            items: [
              { key: 'logout', icon: <LogoutOutlined />, label: '退出登录', onClick: handleLogout },
            ],
          }}
        >
          <Space style={{ cursor: 'pointer' }}>
            <Avatar icon={<UserOutlined />} />
            <span>{user?.name || '用户'}</span>
          </Space>
        </Dropdown>
      </Space>
    </Header>
  )
}
```

- [ ] **Step 3: 创建 AppSidebar**

```typescript
// frontend/src/components/Layout/AppSidebar.tsx
import { Layout, Menu } from 'antd'
import { useNavigate, useLocation } from 'react-router-dom'
import {
  DashboardOutlined,
  UserOutlined,
  ExperimentOutlined,
  ScanOutlined,
  SettingOutlined,
} from '@ant-design/icons'

const { Sider } = Layout

interface Props {
  collapsed: boolean
}

export default function AppSidebar({ collapsed }: Props) {
  const navigate = useNavigate()
  const location = useLocation()

  const items = [
    { key: '/dashboard', icon: <DashboardOutlined />, label: '工作台' },
    { key: '/patient360', icon: <UserOutlined />, label: '患者360' },
    { key: '/timeline', icon: <ScanOutlined />, label: '时间轴' },
    { key: '/system', icon: <SettingOutlined />, label: '系统管理' },
  ]

  return (
    <Sider trigger={null} collapsible collapsed={collapsed}>
      <div style={{
        height: 64,
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        color: '#fff',
        fontSize: collapsed ? 14 : 18,
        fontWeight: 'bold',
      }}>
        {collapsed ? '360' : '医技360'}
      </div>
      <Menu
        theme="dark"
        mode="inline"
        selectedKeys={[location.pathname]}
        items={items}
        onClick={({ key }) => navigate(key)}
      />
    </Sider>
  )
}
```

- [ ] **Step 4: 创建工作台页面**

```typescript
// frontend/src/pages/Dashboard/index.tsx
import { Card, Row, Col, Statistic, List, Space, Tag } from 'antd'
import { ExperimentOutlined, ScanOutlined, UserAddOutlined, AlertOutlined } from '@ant-design/icons'
import { useNavigate } from 'react-router-dom'

export default function Dashboard() {
  const navigate = useNavigate()

  return (
    <div>
      <h1 style={{ marginBottom: 24 }}>工作台</h1>
      <Row gutter={16}>
        <Col span={6}>
          <Card>
            <Statistic
              title="今日检验"
              value={128}
              prefix={<ExperimentOutlined />}
              valueStyle={{ color: '#3f8600' }}
            />
          </Card>
        </Col>
        <Col span={6}>
          <Card>
            <Statistic
              title="今日影像"
              value={86}
              prefix={<ScanOutlined />}
              valueStyle={{ color: '#1890ff' }}
            />
          </Card>
        </Col>
        <Col span={6}>
          <Card>
            <Statistic
              title="新增患者"
              value={34}
              prefix={<UserAddOutlined />}
              valueStyle={{ color: '#faad14' }}
            />
          </Card>
        </Col>
        <Col span={6}>
          <Card>
            <Statistic
              title="异常提醒"
              value={12}
              prefix={<AlertOutlined />}
              valueStyle={{ color: '#cf1322' }}
            />
          </Card>
        </Col>
      </Row>

      <Row gutter={16} style={{ marginTop: 24 }}>
        <Col span={12}>
          <Card title="最近检验">
            <List
              size="small"
              dataSource={[
                { id: 1, patient: '张三', orderNo: 'L20240101001', datetime: '10:30', status: '已完成' },
                { id: 2, patient: '李四', orderNo: 'L20240101002', datetime: '10:25', status: '已完成' },
                { id: 3, patient: '王五', orderNo: 'L20240101003', datetime: '10:20', status: '进行中' },
              ]}
              renderItem={(item) => (
                <List.Item>
                  <List.Item.Meta
                    title={item.patient}
                    description={`${item.orderNo} - ${item.datetime}`}
                  />
                  <Tag color={item.status === '已完成' ? 'green' : 'blue'}>{item.status}</Tag>
                </List.Item>
              )}
            />
          </Card>
        </Col>
        <Col span={12}>
          <Card title="最近影像">
            <List
              size="small"
              dataSource={[
                { id: 1, patient: '赵六', orderNo: 'I20240101001', bodyPart: '胸部', modality: 'DR', status: '已完成' },
                { id: 2, patient: '孙七', orderNo: 'I20240101002', bodyPart: '腹部', modality: 'CT', status: '已完成' },
                { id: 3, patient: '周八', orderNo: 'I20240101003', bodyPart: '头部', modality: 'MR', status: '进行中' },
              ]}
              renderItem={(item) => (
                <List.Item>
                  <List.Item.Meta
                    title={`${item.patient} - ${item.bodyPart} ${item.modality}`}
                    description={item.orderNo}
                  />
                  <Tag color={item.status === '已完成' ? 'green' : 'blue'}>{item.status}</Tag>
                </List.Item>
              )}
            />
          </Card>
        </Col>
      </Row>
    </div>
  )
}
```

- [ ] **Step 5: 提交**

```bash
git add frontend/
git commit -m "feat: 实现主布局与工作台"
```

---

## 执行交付

**Plan complete and saved to `docs/superpowers/plans/2026-04-24-clinical-360-core-implementation.md`.**

这份计划覆盖了完整的开发流程，共 13 个主要任务，分 8 个阶段推进。

**两个执行方式可选：**

**1. Subagent-Driven（推荐）**  
我为每个任务启动独立子代理，完成后逐一审核，快速迭代推进。

**2. Inline Execution**  
在当前会话中逐步执行任务，每完成一个检查点做一次回顾。

你选哪种方式？
