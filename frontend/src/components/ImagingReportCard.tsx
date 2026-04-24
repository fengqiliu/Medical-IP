import { Card, Divider } from "antd";

interface Props {
  report: {
    reportContent: string;
    impression: string;
    reportDoctor: string;
    reportDatetime: string;
  };
  aiSummary?: string;
}

export default function ImagingReportCard({ report, aiSummary }: Props) {
  return (
    <Card title="影像报告">
      <div style={{ marginBottom: 16 }}>
        <strong>检查所见：</strong>
        <p style={{ whiteSpace: "pre-wrap", lineHeight: 1.8 }}>
          {report.reportContent || "暂无"}
        </p>
      </div>
      <Divider />
      <div style={{ marginBottom: 16 }}>
        <strong>诊断意见：</strong>
        <p
          style={{
            whiteSpace: "pre-wrap",
            lineHeight: 1.8,
            color: "#cf1322",
            fontWeight: "bold",
          }}
        >
          {report.impression || "暂无"}
        </p>
      </div>
      <Divider />
      <div style={{ color: "#666", fontSize: 12 }}>
        <span>报告医生：{report.reportDoctor || "-"}</span>
        <span style={{ marginLeft: 16 }}>
          报告时间：
          {report.reportDatetime
            ? new Date(report.reportDatetime).toLocaleString()
            : "-"}
        </span>
      </div>

      {aiSummary && (
        <>
          <Divider />
          <div>
            <strong>AI 辅助摘要：</strong>
            <p
              style={{ whiteSpace: "pre-wrap", lineHeight: 1.8, color: "#666" }}
            >
              {aiSummary}
            </p>
          </div>
        </>
      )}
    </Card>
  );
}
