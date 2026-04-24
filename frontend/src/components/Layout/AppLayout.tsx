import { useState } from "react";
import { Outlet } from "react-router-dom";
import { Layout } from "antd";
import AppHeader from "./AppHeader";
import AppSidebar from "./AppSidebar";

const { Content } = Layout;

export default function AppLayout() {
  const [collapsed, setCollapsed] = useState(false);

  return (
    <Layout style={{ minHeight: "100vh" }}>
      <AppSidebar collapsed={collapsed} />
      <Layout>
        <AppHeader
          collapsed={collapsed}
          onToggle={() => setCollapsed(!collapsed)}
        />
        <Content style={{ margin: 16 }}>
          <Outlet />
        </Content>
      </Layout>
    </Layout>
  );
}
