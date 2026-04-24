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
CREATE INDEX idx_encounter_patient ON encounter(patient_id);
CREATE INDEX idx_lab_order_patient ON lab_order(patient_id);
CREATE INDEX idx_lab_order_encounter ON lab_order(encounter_id);
CREATE INDEX idx_imaging_order_patient ON imaging_order(patient_id);
CREATE INDEX idx_imaging_order_encounter ON imaging_order(encounter_id);
CREATE INDEX idx_clinical_event_patient ON clinical_event(patient_id);
CREATE INDEX idx_clinical_event_reference ON clinical_event(reference_id);
CREATE INDEX idx_clinical_event_datetime ON clinical_event(event_datetime DESC);
CREATE INDEX idx_access_log_user ON access_log(user_id);
CREATE INDEX idx_access_log_patient ON access_log(patient_id);

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
