// frontend/src/pages/System/index.tsx
import { useEffect, useState } from "react";
import {
  Tabs,
  Table,
  Button,
  Modal,
  Form,
  Input,
  Select,
  Switch,
  Space,
  Tag,
  message,
  Popconfirm,
  Row,
  Col,
  Card,
  DatePicker,
  Typography,
} from "antd";
import type { ColumnsType } from "antd/es/table";
import {
  getUsers,
  createUser,
  updateUser,
  deleteUser,
  setUserEnabled,
  getRoles,
  createRole,
  updateRole,
  deleteRole,
  getDepartments,
  createDepartment,
  updateDepartment,
  deleteDepartment,
  getPositions,
  createPosition,
  updatePosition,
  deletePosition,
  getAccessLogs,
} from "@/api/system";
import type {
  UserDTO,
  RoleDTO,
  DepartmentDTO,
  PositionDTO,
  AccessLogDTO,
} from "@/types/system";

const { RangePicker } = DatePicker;
const { Title } = Typography;

export default function SystemPage() {
  // ==================== 用户管理 ====================
  const [users, setUsers] = useState<UserDTO[]>([]);
  const [usersLoading, setUsersLoading] = useState(false);
  const [userModalVisible, setUserModalVisible] = useState(false);
  const [userEditing, setUserEditing] = useState<UserDTO | null>(null);
  const [userForm] = Form.useForm();
  const [departments, setDepartments] = useState<DepartmentDTO[]>([]);
  const [positions, setPositions] = useState<PositionDTO[]>([]);

  const loadUsers = async () => {
    setUsersLoading(true);
    try {
      const res = await getUsers();
      setUsers(res.data);
    } catch (e) {
      message.error("加载用户失败");
    } finally {
      setUsersLoading(false);
    }
  };

  const loadRefData = async () => {
    const [deptRes, posRes] = await Promise.all([
      getDepartments(),
      getPositions(),
    ]);
    setDepartments(deptRes.data);
    setPositions(posRes.data);
  };

  const handleUserSave = async () => {
    try {
      const values = await userForm.validateFields();
      if (userEditing) {
        await updateUser(userEditing.id, values);
        message.success("更新成功");
      } else {
        await createUser(values);
        message.success("创建成功");
      }
      setUserModalVisible(false);
      loadUsers();
    } catch (e) {
      message.error("保存失败");
    }
  };

  const handleUserDelete = async (id: number) => {
    await deleteUser(id);
    message.success("删除成功");
    loadUsers();
  };

  const handleUserEnabled = async (id: number, enabled: boolean) => {
    await setUserEnabled(id, enabled);
    message.success(enabled ? "已启用" : "已禁用");
    loadUsers();
  };

  // ==================== 角色管理 ====================
  const [roleList, setRoleList] = useState<RoleDTO[]>([]);
  const [roleLoading, setRoleLoading] = useState(false);
  const [roleModalVisible, setRoleModalVisible] = useState(false);
  const [roleEditing, setRoleEditing] = useState<RoleDTO | null>(null);
  const [roleForm] = Form.useForm();

  const MENU_OPTIONS = [
    { label: "工作台", value: "dashboard" },
    { label: "患者360", value: "patient360" },
    { label: "时间轴", value: "timeline" },
    { label: "系统管理", value: "system" },
  ];

  const loadRoles = async () => {
    setRoleLoading(true);
    try {
      const res = await getRoles();
      setRoleList(res.data);
    } catch (e) {
      message.error("加载角色失败");
    } finally {
      setRoleLoading(false);
    }
  };

  const handleRoleSave = async () => {
    try {
      const values = await roleForm.validateFields();
      if (roleEditing) {
        await updateRole(roleEditing.id, values);
      } else {
        await createRole(values);
      }
      message.success("保存成功");
      setRoleModalVisible(false);
      loadRoles();
    } catch (e) {
      message.error("保存失败");
    }
  };

  // ==================== 部门管理 ====================
  const [deptList, setDeptList] = useState<DepartmentDTO[]>([]);
  const [deptLoading, setDeptLoading] = useState(false);
  const [deptModalVisible, setDeptModalVisible] = useState(false);
  const [deptEditing, setDeptEditing] = useState<DepartmentDTO | null>(null);
  const [deptForm] = Form.useForm();

  const loadDepartments = async () => {
    setDeptLoading(true);
    try {
      const res = await getDepartments();
      setDeptList(res.data);
    } catch (e) {
      message.error("加载部门失败");
    } finally {
      setDeptLoading(false);
    }
  };

  const handleDeptSave = async () => {
    try {
      const values = await deptForm.validateFields();
      if (deptEditing) {
        await updateDepartment(deptEditing.id, values);
      } else {
        await createDepartment(values);
      }
      message.success("保存成功");
      setDeptModalVisible(false);
      loadDepartments();
    } catch (e) {
      message.error("保存失败");
    }
  };

  // ==================== 岗位管理 ====================
  const [posList, setPosList] = useState<PositionDTO[]>([]);
  const [posLoading, setPosLoading] = useState(false);
  const [posModalVisible, setPosModalVisible] = useState(false);
  const [posEditing, setPosEditing] = useState<PositionDTO | null>(null);
  const [posForm] = Form.useForm();

  const loadPositions = async () => {
    setPosLoading(true);
    try {
      const res = await getPositions();
      setPosList(res.data);
    } catch (e) {
      message.error("加载岗位失败");
    } finally {
      setPosLoading(false);
    }
  };

  const handlePosSave = async () => {
    try {
      const values = await posForm.validateFields();
      if (posEditing) {
        await updatePosition(posEditing.id, values);
      } else {
        await createPosition(values);
      }
      message.success("保存成功");
      setPosModalVisible(false);
      loadPositions();
    } catch (e) {
      message.error("保存失败");
    }
  };

  // ==================== 访问日志 ====================
  const [logs, setLogs] = useState<AccessLogDTO[]>([]);
  const [logsLoading, setLogsLoading] = useState(false);
  const [logFilters, setLogFilters] = useState<{
    userId?: number;
    patientId?: number;
    action?: string;
    startDate?: string;
    endDate?: string;
    page: number;
    size: number;
  }>({ page: 1, size: 20 });

  const loadLogs = async () => {
    setLogsLoading(true);
    try {
      const res = await getAccessLogs(logFilters);
      setLogs(res.data);
    } catch (e) {
      message.error("加载日志失败");
    } finally {
      setLogsLoading(false);
    }
  };

  // ==================== 初始化 ====================
  useEffect(() => {
    loadUsers();
    loadRoles();
    loadDepartments();
    loadPositions();
  }, []);

  useEffect(() => {
    loadLogs();
  }, [logFilters]);

  // ==================== 表格列定义 ====================
  const userColumns: ColumnsType<UserDTO> = [
    { title: "ID", dataIndex: "id", width: 60 },
    { title: "用户名", dataIndex: "username" },
    { title: "姓名", dataIndex: "name" },
    { title: "部门", dataIndex: "departmentName" },
    { title: "岗位", dataIndex: "positionName" },
    {
      title: "角色",
      dataIndex: "roles",
      render: (r: string[]) => r?.map((x) => <Tag key={x}>{x}</Tag>),
    },
    {
      title: "状态",
      dataIndex: "enabled",
      width: 80,
      render: (v: boolean, r) => (
        <Switch checked={v} onChange={(c) => handleUserEnabled(r.id, c)} />
      ),
    },
    {
      title: "操作",
      width: 180,
      render: (_, r) => (
        <Space>
          <Button
            size="small"
            type="link"
            onClick={() => {
              setUserEditing(r);
              userForm.setFieldsValue(r);
              setUserModalVisible(true);
            }}
          >
            编辑
          </Button>
          <Popconfirm
            title="确认删除？"
            onConfirm={() => handleUserDelete(r.id)}
          >
            <Button size="small" type="link" danger>
              删除
            </Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  const roleColumns: ColumnsType<RoleDTO> = [
    { title: "ID", dataIndex: "id", width: 60 },
    { title: "角色名", dataIndex: "name" },
    { title: "描述", dataIndex: "description" },
    {
      title: "菜单权限",
      dataIndex: "menuCodes",
      render: (c: string[]) => c?.map((x) => <Tag key={x}>{x}</Tag>),
    },
    {
      title: "操作",
      width: 120,
      render: (_, r) => (
        <Space>
          <Button
            size="small"
            type="link"
            onClick={() => {
              setRoleEditing(r);
              roleForm.setFieldsValue(r);
              setRoleModalVisible(true);
            }}
          >
            编辑
          </Button>
          <Popconfirm
            title="确认删除？"
            onConfirm={async () => {
              await deleteRole(r.id);
              loadRoles();
            }}
          >
            <Button size="small" type="link" danger>
              删除
            </Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  const deptColumns: ColumnsType<DepartmentDTO> = [
    { title: "ID", dataIndex: "id", width: 60 },
    { title: "部门名称", dataIndex: "name" },
    { title: "上级部门", dataIndex: "parentName" },
    {
      title: "操作",
      width: 120,
      render: (_, r) => (
        <Space>
          <Button
            size="small"
            type="link"
            onClick={() => {
              setDeptEditing(r);
              deptForm.setFieldsValue(r);
              setDeptModalVisible(true);
            }}
          >
            编辑
          </Button>
          <Popconfirm
            title="确认删除？"
            onConfirm={async () => {
              await deleteDepartment(r.id);
              loadDepartments();
            }}
          >
            <Button size="small" type="link" danger>
              删除
            </Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  const posColumns: ColumnsType<PositionDTO> = [
    { title: "ID", dataIndex: "id", width: 60 },
    { title: "岗位名称", dataIndex: "name" },
    { title: "所属部门", dataIndex: "departmentName" },
    { title: "数据范围", dataIndex: "dataScope" },
    {
      title: "操作",
      width: 120,
      render: (_, r) => (
        <Space>
          <Button
            size="small"
            type="link"
            onClick={() => {
              setPosEditing(r);
              posForm.setFieldsValue(r);
              setPosModalVisible(true);
            }}
          >
            编辑
          </Button>
          <Popconfirm
            title="确认删除？"
            onConfirm={async () => {
              await deletePosition(r.id);
              loadPositions();
            }}
          >
            <Button size="small" type="link" danger>
              删除
            </Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  const logColumns: ColumnsType<AccessLogDTO> = [
    { title: "ID", dataIndex: "id", width: 60 },
    { title: "用户", dataIndex: "username" },
    { title: "患者", dataIndex: "patientName" },
    { title: "操作", dataIndex: "action" },
    { title: "IP", dataIndex: "ipAddress", width: 130 },
    { title: "时间", dataIndex: "accessDatetime", width: 170 },
    { title: "详情", dataIndex: "requestDetail", ellipsis: true },
  ];

  // ==================== 渲染 ====================
  return (
    <div style={{ padding: 24 }}>
      <Title level={4}>系统管理</Title>

      <Tabs
        defaultActiveKey="users"
        items={[
          {
            key: "users",
            label: "用户管理",
            children: (
              <Card>
                <Row style={{ marginBottom: 16 }}>
                  <Col>
                    <Button
                      type="primary"
                      onClick={() => {
                        setUserEditing(null);
                        userForm.resetFields();
                        loadRefData();
                        setUserModalVisible(true);
                      }}
                    >
                      新建用户
                    </Button>
                  </Col>
                </Row>
                <Table
                  columns={userColumns}
                  dataSource={users}
                  loading={usersLoading}
                  rowKey="id"
                  pagination={{ pageSize: 10 }}
                />
              </Card>
            ),
          },
          {
            key: "roles",
            label: "角色管理",
            children: (
              <Card>
                <Row style={{ marginBottom: 16 }}>
                  <Col>
                    <Button
                      type="primary"
                      onClick={() => {
                        setRoleEditing(null);
                        roleForm.resetFields();
                        setRoleModalVisible(true);
                      }}
                    >
                      新建角色
                    </Button>
                  </Col>
                </Row>
                <Table
                  columns={roleColumns}
                  dataSource={roleList}
                  loading={roleLoading}
                  rowKey="id"
                  pagination={{ pageSize: 10 }}
                />
              </Card>
            ),
          },
          {
            key: "departments",
            label: "部门管理",
            children: (
              <Card>
                <Row style={{ marginBottom: 16 }}>
                  <Col>
                    <Button
                      type="primary"
                      onClick={() => {
                        setDeptEditing(null);
                        deptForm.resetFields();
                        setDeptModalVisible(true);
                      }}
                    >
                      新建部门
                    </Button>
                  </Col>
                </Row>
                <Table
                  columns={deptColumns}
                  dataSource={deptList}
                  loading={deptLoading}
                  rowKey="id"
                  pagination={{ pageSize: 10 }}
                />
              </Card>
            ),
          },
          {
            key: "positions",
            label: "岗位管理",
            children: (
              <Card>
                <Row style={{ marginBottom: 16 }}>
                  <Col>
                    <Button
                      type="primary"
                      onClick={() => {
                        setPosEditing(null);
                        posForm.resetFields();
                        setPosModalVisible(true);
                      }}
                    >
                      新建岗位
                    </Button>
                  </Col>
                </Row>
                <Table
                  columns={posColumns}
                  dataSource={posList}
                  loading={posLoading}
                  rowKey="id"
                  pagination={{ pageSize: 10 }}
                />
              </Card>
            ),
          },
          {
            key: "accessLogs",
            label: "访问日志",
            children: (
              <Card>
                <Space style={{ marginBottom: 16 }} wrap>
                  <Input
                    placeholder="操作名称"
                    style={{ width: 150 }}
                    onChange={(e) =>
                      setLogFilters((f) => ({
                        ...f,
                        action: e.target.value || undefined,
                      }))
                    }
                  />
                  <RangePicker
                    onChange={(_, ds) =>
                      setLogFilters((f) => ({
                        ...f,
                        startDate: ds[0] || undefined,
                        endDate: ds[1] || undefined,
                      }))
                    }
                  />
                  <Button
                    onClick={() => setLogFilters((f) => ({ ...f, page: 1 }))}
                  >
                    搜索
                  </Button>
                </Space>
                <Table
                  columns={logColumns}
                  dataSource={logs}
                  loading={logsLoading}
                  rowKey="id"
                  pagination={{
                    pageSize: 20,
                    total: logs.length,
                    onChange: (p) => setLogFilters((f) => ({ ...f, page: p })),
                  }}
                />
              </Card>
            ),
          },
        ]}
      />

      {/* 用户编辑弹窗 */}
      <Modal
        title={userEditing ? "编辑用户" : "新建用户"}
        open={userModalVisible}
        onOk={handleUserSave}
        onCancel={() => setUserModalVisible(false)}
      >
        <Form form={userForm} layout="vertical">
          <Form.Item
            name="username"
            label="用户名"
            rules={[{ required: true }]}
          >
            <Input />
          </Form.Item>
          <Form.Item name="name" label="姓名" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="departmentId" label="部门">
            <Select
              allowClear
              options={departments.map((d) => ({ label: d.name, value: d.id }))}
            />
          </Form.Item>
          <Form.Item name="positionId" label="岗位">
            <Select
              allowClear
              options={positions.map((p) => ({ label: p.name, value: p.id }))}
            />
          </Form.Item>
          <Form.Item
            name="enabled"
            label="启用"
            valuePropName="checked"
            initialValue={true}
          >
            <Switch />
          </Form.Item>
        </Form>
      </Modal>

      {/* 角色编辑弹窗 */}
      <Modal
        title={roleEditing ? "编辑角色" : "新建角色"}
        open={roleModalVisible}
        onOk={handleRoleSave}
        onCancel={() => setRoleModalVisible(false)}
      >
        <Form form={roleForm} layout="vertical">
          <Form.Item name="name" label="角色名" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="description" label="描述">
            <Input />
          </Form.Item>
          <Form.Item name="menuCodes" label="菜单权限">
            <Select mode="multiple" options={MENU_OPTIONS} />
          </Form.Item>
        </Form>
      </Modal>

      {/* 部门编辑弹窗 */}
      <Modal
        title={deptEditing ? "编辑部门" : "新建部门"}
        open={deptModalVisible}
        onOk={handleDeptSave}
        onCancel={() => setDeptModalVisible(false)}
      >
        <Form form={deptForm} layout="vertical">
          <Form.Item name="name" label="部门名称" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="parentId" label="上级部门">
            <Select
              allowClear
              placeholder="无上级"
              options={deptList.map((d) => ({ label: d.name, value: d.id }))}
            />
          </Form.Item>
        </Form>
      </Modal>

      {/* 岗位编辑弹窗 */}
      <Modal
        title={posEditing ? "编辑岗位" : "新建岗位"}
        open={posModalVisible}
        onOk={handlePosSave}
        onCancel={() => setPosModalVisible(false)}
      >
        <Form form={posForm} layout="vertical">
          <Form.Item name="name" label="岗位名称" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="departmentId" label="所属部门">
            <Select
              allowClear
              options={deptList.map((d) => ({ label: d.name, value: d.id }))}
            />
          </Form.Item>
          <Form.Item name="dataScope" label="数据范围">
            <Select
              allowClear
              options={[
                { label: "本人", value: "SELF" },
                { label: "本部门", value: "DEPT" },
                { label: "全部", value: "ALL" },
              ]}
            />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
}
