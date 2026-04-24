// frontend/src/components/PatientHeader.tsx
import { Card, Tag, Descriptions } from "antd";
import type { Patient, Encounter } from "@/types/patient";

interface Props {
  patient: Patient;
  encounter: Encounter;
}

export default function PatientHeader({ patient, encounter }: Props) {
  const age = patient.birthDate
    ? new Date().getFullYear() - new Date(patient.birthDate).getFullYear()
    : "-";

  const typeColor: Record<string, string> = {
    门诊: "blue",
    住院: "green",
    急诊: "red",
  };

  return (
    <Card>
      <Descriptions column={4} size="small">
        <Descriptions.Item label="姓名">{patient.name}</Descriptions.Item>
        <Descriptions.Item label="性别">{patient.gender}</Descriptions.Item>
        <Descriptions.Item label="年龄">{age}岁</Descriptions.Item>
        <Descriptions.Item label="就诊类型">
          <Tag color={typeColor[encounter.encounterType] || "default"}>
            {encounter.encounterType}
          </Tag>
        </Descriptions.Item>
        <Descriptions.Item label="科室">
          {encounter.departmentName}
        </Descriptions.Item>
        <Descriptions.Item label="就诊时间">
          {new Date(encounter.visitDatetime).toLocaleString()}
        </Descriptions.Item>
        <Descriptions.Item label="就诊原因" span={2}>
          {encounter.admissionReason}
        </Descriptions.Item>
      </Descriptions>
    </Card>
  );
}
