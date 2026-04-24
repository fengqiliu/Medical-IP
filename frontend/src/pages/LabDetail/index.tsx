import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { Card, Descriptions, Spin, Modal } from "antd";
import LabResultTable from "@/components/LabResultTable";
import AISummaryCard from "@/components/AISummaryCard";
import { getLabOrder } from "@/api/lab";
import type { LabOrderDTO } from "@/types/lab";

export default function LabDetail() {
  const { orderId } = useParams<{ orderId: string }>();
  const [data, setData] = useState<LabOrderDTO | null>(null);
  const [loading, setLoading] = useState(true);
  const [trendModal, setTrendModal] = useState<{
    visible: boolean;
    itemCode: string;
    itemName: string;
  }>({ visible: false, itemCode: "", itemName: "" });

  useEffect(() => {
    if (orderId) {
      loadData();
    }
  }, [orderId]);

  const loadData = async () => {
    setLoading(true);
    try {
      const response = await getLabOrder(Number(orderId));
      setData(response.data);
    } catch (error) {
      console.error("加载检验数据失败", error);
    } finally {
      setLoading(false);
    }
  };

  const handleViewTrend = (itemCode: string, itemName: string) => {
    setTrendModal({ visible: true, itemCode, itemName });
  };

  if (loading) {
    return (
      <Spin
        style={{ display: "flex", justifyContent: "center", marginTop: 100 }}
      />
    );
  }

  if (!data) {
    return <div>未找到检验数据</div>;
  }

  const abnormalCount = data.results.filter((r) => r.abnormalFlag).length;

  return (
    <div style={{ padding: 24 }}>
      <Card title="检验单信息">
        <Descriptions column={4}>
          <Descriptions.Item label="检验单号">{data.orderNo}</Descriptions.Item>
          <Descriptions.Item label="检验科室">
            {data.departmentName}
          </Descriptions.Item>
          <Descriptions.Item label="标本类型">
            {data.specimenType}
          </Descriptions.Item>
          <Descriptions.Item label="检验时间">
            {new Date(data.orderDatetime).toLocaleString()}
          </Descriptions.Item>
          <Descriptions.Item label="状态">{data.status}</Descriptions.Item>
        </Descriptions>
      </Card>

      <Card title="检验结果" style={{ marginTop: 16 }}>
        <LabResultTable results={data.results} onViewTrend={handleViewTrend} />
      </Card>

      <AISummaryCard
        summary={`检验单 ${data.orderNo} 包含 ${data.results.length} 个检验项目，其中 ${abnormalCount} 项异常。`}
      />

      <Modal
        title={`${trendModal.itemName} 趋势图`}
        open={trendModal.visible}
        onCancel={() => setTrendModal({ ...trendModal, visible: false })}
        footer={null}
        width={800}
      >
        <div
          style={{
            height: 300,
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            color: "#999",
          }}
        >
          趋势图组件（可使用 @ant-design/charts 或 recharts 实现）
        </div>
      </Modal>
    </div>
  );
}
