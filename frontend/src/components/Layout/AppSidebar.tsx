import { Layout, Menu } from "antd";
import { useNavigate, useLocation } from "react-router-dom";
import {
  DashboardOutlined,
  UserOutlined,
  SettingOutlined,
} from "@ant-design/icons";

const { Sider } = Layout;

interface Props {
  collapsed: boolean;
}

export default function AppSidebar({ collapsed }: Props) {
  const navigate = useNavigate();
  const location = useLocation();

  const items = [
    { key: "/dashboard", icon: <DashboardOutlined />, label: "工作台" },
    { key: "/patientSearch", icon: <UserOutlined />, label: "患者360" },
    { key: "/system", icon: <SettingOutlined />, label: "系统管理" },
  ];

  return (
    <Sider trigger={null} collapsible collapsed={collapsed}>
      <div
        style={{
          height: 64,
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          color: "#fff",
          fontSize: collapsed ? 14 : 18,
          fontWeight: "bold",
        }}
      >
        {collapsed ? "360" : "医技360"}
      </div>
      <Menu
        theme="dark"
        mode="inline"
        selectedKeys={[location.pathname]}
        items={items}
        onClick={({ key }) => navigate(key)}
      />
    </Sider>
  );
}
