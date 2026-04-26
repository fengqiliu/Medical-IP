import { Card, Row, Col, Statistic, List, Tag } from "antd";
import {
  ExperimentOutlined,
  ScanOutlined,
  UserAddOutlined,
  AlertOutlined,
} from "@ant-design/icons";

export default function Dashboard() {
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
              valueStyle={{ color: "#3f8600" }}
            />
          </Card>
        </Col>
        <Col span={6}>
          <Card>
            <Statistic
              title="今日影像"
              value={86}
              prefix={<ScanOutlined />}
              valueStyle={{ color: "#1890ff" }}
            />
          </Card>
        </Col>
        <Col span={6}>
          <Card>
            <Statistic
              title="新增患者"
              value={34}
              prefix={<UserAddOutlined />}
              valueStyle={{ color: "#faad14" }}
            />
          </Card>
        </Col>
        <Col span={6}>
          <Card>
            <Statistic
              title="异常提醒"
              value={12}
              prefix={<AlertOutlined />}
              valueStyle={{ color: "#cf1322" }}
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
                {
                  id: 1,
                  patient: "张三",
                  orderNo: "L20240101001",
                  datetime: "10:30",
                  status: "已完成",
                },
                {
                  id: 2,
                  patient: "李四",
                  orderNo: "L20240101002",
                  datetime: "10:25",
                  status: "已完成",
                },
                {
                  id: 3,
                  patient: "王五",
                  orderNo: "L20240101003",
                  datetime: "10:20",
                  status: "进行中",
                },
              ]}
              renderItem={(item) => (
                <List.Item>
                  <List.Item.Meta
                    title={item.patient}
                    description={`${item.orderNo} - ${item.datetime}`}
                  />
                  <Tag color={item.status === "已完成" ? "green" : "blue"}>
                    {item.status}
                  </Tag>
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
                {
                  id: 1,
                  patient: "赵六",
                  orderNo: "I20240101001",
                  bodyPart: "胸部",
                  modality: "DR",
                  status: "已完成",
                },
                {
                  id: 2,
                  patient: "孙七",
                  orderNo: "I20240101002",
                  bodyPart: "腹部",
                  modality: "CT",
                  status: "已完成",
                },
                {
                  id: 3,
                  patient: "周八",
                  orderNo: "I20240101003",
                  bodyPart: "头部",
                  modality: "MR",
                  status: "进行中",
                },
              ]}
              renderItem={(item) => (
                <List.Item>
                  <List.Item.Meta
                    title={`${item.patient} - ${item.bodyPart} ${item.modality}`}
                    description={item.orderNo}
                  />
                  <Tag color={item.status === "已完成" ? "green" : "blue"}>
                    {item.status}
                  </Tag>
                </List.Item>
              )}
            />
          </Card>
        </Col>
      </Row>
    </div>
  );
}
