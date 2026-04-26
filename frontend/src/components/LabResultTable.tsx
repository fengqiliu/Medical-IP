import { Table } from "antd";
import AbnormalTag from "./AbnormalTag";
import type { LabResultItem } from "@/types/lab";

interface Props {
  results: LabResultItem[];
  onViewTrend?: (itemCode: string, itemName: string) => void;
}

export default function LabResultTable({ results, onViewTrend }: Props) {
  return (
    <Table
      dataSource={results}
      rowKey="id"
      size="small"
      pagination={false}
      columns={[
        { title: "项目代码", dataIndex: "itemCode", width: 100 },
        { title: "项目名称", dataIndex: "itemName", width: 150 },
        {
          title: "结果值",
          dataIndex: "resultValue",
          width: 120,
          render: (value, record) => (
            <span
              style={{ fontWeight: record.abnormalFlag ? "bold" : "normal" }}
            >
              {value} {record.unit}
            </span>
          ),
        },
        {
          title: "参考范围",
          width: 150,
          render: (_, record) => (
            <span style={{ color: "#999" }}>
              {record.refRangeLow} ~ {record.refRangeHigh}
            </span>
          ),
        },
        {
          title: "状态",
          dataIndex: "abnormalFlag",
          width: 100,
          render: (flag) => <AbnormalTag flag={flag} />,
        },
        {
          title: "报告时间",
          dataIndex: "resultDatetime",
          width: 160,
          render: (v) => (v ? new Date(v).toLocaleString() : "-"),
        },
        {
          title: "操作",
          width: 100,
          render: (_, record) => (
            <a onClick={() => onViewTrend?.(record.itemCode, record.itemName)}>
              查看趋势
            </a>
          ),
        },
      ]}
    />
  );
}
