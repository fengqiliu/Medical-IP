// frontend/src/pages/Patient360/index.tsx
import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Card, Row, Col, Table, Button, Space, Statistic, Spin } from "antd";
import {
  ExperimentOutlined,
  ScanOutlined,
  AlertOutlined,
} from "@ant-design/icons";
import PatientHeader from "@/components/PatientHeader";
import AISummaryCard from "@/components/AISummaryCard";
import { getPatient360 } from "@/api/patient";
import type { Patient360DTO } from "@/types/patient";

export default function Patient360() {
  const { patientId } = useParams<{ patientId: string }>();
  const navigate = useNavigate();
  const [data, setData] = useState<Patient360DTO | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (patientId) {
      loadData();
    }
  }, [patientId]);

  const loadData = async () => {
    setLoading(true);
    try {
      const response = await getPatient360(Number(patientId));
      setData(response.data);
    } catch (error) {
      console.error("加载患者360数据失败", error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <Spin
        style={{ display: "flex", justifyContent: "center", marginTop: 100 }}
      />
    );
  }

  if (!data) {
    return <div>未找到患者数据</div>;
  }

  const { patient, encounter, labSummary, imagingSummary, aiSummary } = data;

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
              valueStyle={{ color: "#3f8600" }}
            />
          </Card>
        </Col>
        <Col span={8}>
          <Card>
            <Statistic
              title="近期影像"
              value={imagingSummary.recentCount}
              prefix={<ScanOutlined />}
              valueStyle={{ color: "#1890ff" }}
            />
          </Card>
        </Col>
        <Col span={8}>
          <Card>
            <Statistic
              title="异常项"
              value={labSummary.abnormalCount}
              prefix={<AlertOutlined />}
              valueStyle={{ color: "#cf1322" }}
            />
          </Card>
        </Col>
      </Row>

      {/* 快捷入口 */}
      <Card style={{ marginTop: 16 }}>
        <Space>
          <Button
            type="primary"
            onClick={() => navigate(`/timeline/${patientId}`)}
          >
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
            { title: "检验单号", dataIndex: "orderNo" },
            {
              title: "时间",
              dataIndex: "orderDatetime",
              render: (v) => new Date(v).toLocaleString(),
            },
            { title: "标本类型", dataIndex: "specimenType" },
            { title: "科室", dataIndex: "departmentName" },
            { title: "状态", dataIndex: "status" },
            {
              title: "操作",
              render: (_, record) => (
                <Button
                  type="link"
                  onClick={() => navigate(`/lab/${record.id}`)}
                >
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
            { title: "检查单号", dataIndex: "orderNo" },
            {
              title: "时间",
              dataIndex: "orderDatetime",
              render: (v) => new Date(v).toLocaleString(),
            },
            { title: "检查部位", dataIndex: "bodyPart" },
            { title: "检查方式", dataIndex: "modality" },
            { title: "科室", dataIndex: "departmentName" },
            {
              title: "操作",
              render: (_, record) => (
                <Button
                  type="link"
                  onClick={() => navigate(`/imaging/${record.id}`)}
                >
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
  );
}
