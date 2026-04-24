// frontend/src/components/AISummaryCard.tsx
import { Card, Alert } from "antd";

interface Props {
  summary: string;
  loading?: boolean;
}

export default function AISummaryCard({ summary, loading }: Props) {
  return (
    <Card title="AI 辅助摘要" loading={loading}>
      <Alert
        message="辅助生成，仅供参考"
        description={summary || "暂无摘要信息"}
        type="info"
        showIcon
      />
    </Card>
  );
}
