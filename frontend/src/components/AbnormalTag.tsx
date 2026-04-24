import { Tag } from "antd";

interface Props {
  flag: string;
}

export default function AbnormalTag({ flag }: Props) {
  if (flag === "H") {
    return <Tag color="red">偏高 ↑</Tag>;
  }
  if (flag === "L") {
    return <Tag color="blue">偏低 ↓</Tag>;
  }
  if (flag === "HH" || flag === "LL") {
    return (
      <Tag color="red" style={{ fontWeight: "bold" }}>
        严重异常
      </Tag>
    );
  }
  return null;
}
