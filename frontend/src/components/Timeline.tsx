import { Timeline as AntTimeline, Tag, Card } from "antd";
import { ExperimentOutlined, ScanOutlined } from "@ant-design/icons";
import { useNavigate } from "react-router-dom";
import type { TimelineEventDTO } from "@/types/timeline";

interface Props {
  events: TimelineEventDTO[];
  loading?: boolean;
}

export default function Timeline({ events, loading }: Props) {
  const navigate = useNavigate();

  const getColor = (type: string, abnormal?: boolean) => {
    if (abnormal) return "red";
    return type === "lab" ? "green" : "blue";
  };

  const getIcon = (type: string) => {
    return type === "lab" ? <ExperimentOutlined /> : <ScanOutlined />;
  };

  const formatDate = (datetime: string) => {
    const date = new Date(datetime);
    return `${date.getMonth() + 1}-${date.getDate()} ${date.getHours()}:${String(date.getMinutes()).padStart(2, "0")}`;
  };

  return (
    <Card loading={loading}>
      <AntTimeline
        items={events.map((event) => ({
          color: getColor(event.eventType, event.abnormal),
          dot: getIcon(event.eventType),
          children: (
            <div
              style={{ cursor: "pointer" }}
              onClick={() =>
                navigate(
                  `/${event.eventType === "lab" ? "lab" : "imaging"}/${event.referenceId}`,
                )
              }
            >
              <div style={{ display: "flex", alignItems: "center", gap: 8 }}>
                <strong>{formatDate(event.eventDatetime)}</strong>
                <Tag color={event.eventType === "lab" ? "green" : "blue"}>
                  {event.eventType === "lab" ? "检验" : "影像"}
                </Tag>
                {event.abnormal && <Tag color="red">异常</Tag>}
                {event.departmentName && <Tag>{event.departmentName}</Tag>}
              </div>
              <p style={{ margin: "4px 0 0 0", color: "#666" }}>
                {event.eventSummary}
              </p>
            </div>
          ),
        }))}
      />
    </Card>
  );
}
