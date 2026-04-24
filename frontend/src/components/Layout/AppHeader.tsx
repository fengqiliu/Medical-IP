import { Layout, Button, Dropdown, Avatar, Space } from "antd";
import {
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  UserOutlined,
  LogoutOutlined,
} from "@ant-design/icons";
import { useNavigate } from "react-router-dom";
import { useAuthStore } from "@/stores/authStore";

const { Header } = Layout;

interface Props {
  collapsed: boolean;
  onToggle: () => void;
}

export default function AppHeader({ collapsed, onToggle }: Props) {
  const navigate = useNavigate();
  const { user, logout } = useAuthStore();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <Header
      style={{
        padding: "0 16px",
        background: "#fff",
        display: "flex",
        alignItems: "center",
      }}
    >
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
              {
                key: "logout",
                icon: <LogoutOutlined />,
                label: "退出登录",
                onClick: handleLogout,
              },
            ],
          }}
        >
          <Space style={{ cursor: "pointer" }}>
            <Avatar icon={<UserOutlined />} />
            <span>{user?.name || "用户"}</span>
          </Space>
        </Dropdown>
      </Space>
    </Header>
  );
}
