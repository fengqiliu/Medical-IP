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

-- 数据同步日志
CREATE TABLE data_sync_log (
    id BIGSERIAL PRIMARY KEY,
    source_type VARCHAR(20) NOT NULL,
    data_type VARCHAR(20),
    last_sync_time VARCHAR(50),
    synced_count INTEGER DEFAULT 0,
    status VARCHAR(20),
    error_message TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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
CREATE INDEX idx_data_sync_log_source ON data_sync_log(source_type);

-- ===================== 基础数据 =====================

-- 科室
INSERT INTO department (id, name) VALUES (1, '内科');
INSERT INTO department (id, name) VALUES (2, '外科');
INSERT INTO department (id, name) VALUES (3, '检验科');
INSERT INTO department (id, name) VALUES (4, '放射科');

-- 岗位
INSERT INTO position (id, name, department_id, data_scope) VALUES (1, '主治医师', 1, 'DEPT');
INSERT INTO position (id, name, department_id, data_scope) VALUES (2, '检验技师', 3, 'DEPT');

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

-- ===================== 测试患者数据 =====================

INSERT INTO patient (id, unified_patient_id, name, gender, birth_date, id_card_no, phone)
VALUES
  (1, 'P20240001', '张三', '男', '1980-05-15', '110101198005150012', '13800138001'),
  (2, 'P20240002', '李四', '女', '1975-09-20', '110101197509200028', '13800138002'),
  (3, 'P20240003', '王五', '男', '1992-03-08', '110101199203080034', '13800138003');

-- 就诊记录
INSERT INTO encounter (id, patient_id, encounter_type, department_id, visit_datetime, admission_reason)
VALUES
  (1, 1, '住院', 1, NOW() - INTERVAL '3 days', '发热、咳嗽 3 天'),
  (2, 2, '门诊', 1, NOW() - INTERVAL '1 day', '头痛、头晕'),
  (3, 3, '急诊', 2, NOW() - INTERVAL '5 hours', '腹痛');

-- 检验医嘱
INSERT INTO lab_order (id, patient_id, encounter_id, order_no, department_id, order_datetime, status, specimen_type)
VALUES
  (1, 1, 1, 'L20260424001', 3, NOW() - INTERVAL '2 days', '已完成', '血液'),
  (2, 1, 1, 'L20260424002', 3, NOW() - INTERVAL '1 day', '已完成', '尿液'),
  (3, 2, 2, 'L20260424003', 3, NOW() - INTERVAL '12 hours', '已完成', '血液');

-- 检验结果
INSERT INTO lab_result (lab_order_id, item_code, item_name, result_value, unit, ref_range_low, ref_range_high, abnormal_flag, result_datetime)
VALUES
  (1, 'WBC', '白细胞计数', '12.5', '×10⁹/L', 4.0, 10.0, 'H', NOW() - INTERVAL '2 days'),
  (1, 'RBC', '红细胞计数', '4.2', '×10¹²/L', 3.8, 5.8, NULL, NOW() - INTERVAL '2 days'),
  (1, 'HGB', '血红蛋白', '128', 'g/L', 115, 150, NULL, NOW() - INTERVAL '2 days'),
  (1, 'PLT', '血小板计数', '88', '×10⁹/L', 100, 300, 'L', NOW() - INTERVAL '2 days'),
  (1, 'CRP', 'C反应蛋白', '48.6', 'mg/L', 0, 10, 'H', NOW() - INTERVAL '2 days'),
  (2, 'URO', '尿胆原', '阴性', NULL, NULL, NULL, NULL, NOW() - INTERVAL '1 day'),
  (2, 'PRO', '尿蛋白', '++', NULL, NULL, NULL, 'H', NOW() - INTERVAL '1 day'),
  (3, 'WBC', '白细胞计数', '6.8', '×10⁹/L', 4.0, 10.0, NULL, NOW() - INTERVAL '12 hours'),
  (3, 'HGB', '血红蛋白', '102', 'g/L', 115, 150, 'L', NOW() - INTERVAL '12 hours');

-- 影像医嘱
INSERT INTO imaging_order (id, patient_id, encounter_id, order_no, department_id, body_part, modality, order_datetime, status, pacs_url)
VALUES
  (1, 1, 1, 'I20260424001', 4, '胸部', 'CT', NOW() - INTERVAL '2 days', '已完成', 'http://pacs.example.com/view/1'),
  (2, 2, 2, 'I20260424002', 4, '头颅', 'MR', NOW() - INTERVAL '1 day', '已完成', 'http://pacs.example.com/view/2'),
  (3, 3, 3, 'I20260424003', 4, '腹部', 'CT', NOW() - INTERVAL '4 hours', '审核中', NULL);

-- 影像报告
INSERT INTO imaging_report (imaging_order_id, report_content, impression, report_doctor, report_datetime)
VALUES
  (1, '双肺纹理增多，右下肺见斑片状密度增高影，边界模糊，范围约3×4cm。纵隔未见明显肿大淋巴结。', '右下肺炎，建议抗感染治疗后复查。', '李放射医生', NOW() - INTERVAL '1 day 20 hours'),
  (2, '脑实质内未见明显异常信号，脑室系统未见扩大。脑沟、脑裂未见明显增宽。', '颅内MRI未见明显器质性病变，结合临床进一步诊治。', '王放射医生', NOW() - INTERVAL '20 hours');

-- 时间轴事件
INSERT INTO clinical_event (patient_id, event_type, event_datetime, event_summary, event_status, reference_id, department_id)
VALUES
  (1, 'lab', NOW() - INTERVAL '2 days', '血常规+CRP：白细胞升高，CRP明显升高，血小板偏低', '已完成', 1, 3),
  (1, 'imaging', NOW() - INTERVAL '2 days', '胸部CT：右下肺炎', '已完成', 1, 4),
  (1, 'lab', NOW() - INTERVAL '1 day', '尿常规：尿蛋白++', '已完成', 2, 3),
  (2, 'lab', NOW() - INTERVAL '12 hours', '血常规：血红蛋白偏低', '已完成', 3, 3),
  (2, 'imaging', NOW() - INTERVAL '1 day', '头颅MRI：未见明显异常', '已完成', 2, 4),
  (3, 'imaging', NOW() - INTERVAL '4 hours', '腹部CT：审核中', '审核中', 3, 4);

