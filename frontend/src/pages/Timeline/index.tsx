import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { Card, Spin } from "antd";
import PatientHeader from "@/components/PatientHeader";
import Timeline from "@/components/Timeline";
import { getTimeline } from "@/api/timeline";
import type { TimelineEventDTO } from "@/types/timeline";
import type { Patient360DTO } from "@/types/patient";
import { getPatient360 } from "@/api/patient";

export default function TimelinePage() {
  const { patientId } = useParams<{ patientId: string }>();
  const [events, setEvents] = useState<TimelineEventDTO[]>([]);
  const [patientData, setPatientData] = useState<Patient360DTO | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (patientId) {
      loadData();
    }
  }, [patientId]);

  const loadData = async () => {
    setLoading(true);
    try {
      const [timelineRes, patientRes] = await Promise.all([
        getTimeline(Number(patientId)),
        getPatient360(Number(patientId)),
      ]);
      setEvents(timelineRes.data);
      setPatientData(patientRes.data);
    } catch (error) {
      console.error("加载数据失败", error);
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

  if (!patientData) {
    return <div>未找到患者数据</div>;
  }

  return (
    <div style={{ padding: 24 }}>
      <PatientHeader
        patient={patientData.patient}
        encounter={patientData.encounter}
      />

      <Card title="就诊时间轴" style={{ marginTop: 16 }}>
        <Timeline events={events} />
      </Card>
    </div>
  );
}
