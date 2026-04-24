import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { Card, Descriptions, Button, Spin } from "antd";
import { ExportOutlined } from "@ant-design/icons";
import ImagingReportCard from "@/components/ImagingReportCard";
import { getImagingOrder } from "@/api/imaging";
import type { ImagingOrderDTO } from "@/types/imaging";

export default function ImagingDetail() {
  const { orderId } = useParams<{ orderId: string }>();
  const [data, setData] = useState<ImagingOrderDTO | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (orderId) {
      loadData();
    }
  }, [orderId]);

  const loadData = async () => {
    setLoading(true);
    try {
      const response = await getImagingOrder(Number(orderId));
      setData(response.data);
    } catch (error) {
      console.error("加载影像数据失败", error);
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
    return <div>未找到影像数据</div>;
  }

  const modalityNames: Record<string, string> = {
    CR: "计算机X线摄影",
    DR: "数字X线摄影",
    CT: "计算机断层扫描",
    MR: "磁共振成像",
    US: "超声检查",
    XA: "X线血管造影",
    RF: "射频消融",
  };

  return (
    <div style={{ padding: 24 }}>
      <Card
        title="影像检查信息"
        extra={
          data.pacsUrl && (
            <Button
              type="primary"
              icon={<ExportOutlined />}
              onClick={() => window.open(data.pacsUrl, "_blank")}
            >
              跳转 PACS 查看影像
            </Button>
          )
        }
      >
        <Descriptions column={4}>
          <Descriptions.Item label="检查单号">{data.orderNo}</Descriptions.Item>
          <Descriptions.Item label="检查科室">
            {data.departmentName}
          </Descriptions.Item>
          <Descriptions.Item label="检查部位">
            {data.bodyPart}
          </Descriptions.Item>
          <Descriptions.Item label="检查方式">
            {data.modality} - {modalityNames[data.modality] || data.modality}
          </Descriptions.Item>
          <Descriptions.Item label="检查时间">
            {new Date(data.orderDatetime).toLocaleString()}
          </Descriptions.Item>
          <Descriptions.Item label="状态">{data.status}</Descriptions.Item>
        </Descriptions>
      </Card>

      <ImagingReportCard
        report={data.report}
        aiSummary={
          data.report.impression
            ? `影像报告提示：${data.report.impression.substring(0, 100)}...`
            : undefined
        }
      />
    </div>
  );
}
