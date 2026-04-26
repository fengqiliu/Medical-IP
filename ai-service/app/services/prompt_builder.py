LAB_SUMMARY_PROMPT = """你是一位医疗检验数据摘要助手。请根据以下检验结果生成简洁摘要。

患者：{patient_name}
检验单号：{order_no}
标本类型：{specimen_type}

检验结果：
{results_text}

请按以下格式输出：
1. 异常项汇总：列出所有异常项目
2. 关键指标：列出最重要的2-3个指标及其临床意义
3. 注意事项：如有需要关注的情况请说明

注意：只做客观数据摘要，不做诊断建议。输出内容需标注"辅助生成，仅供参考"。
"""

def build_lab_summary_prompt(request) -> str:
    results_text = "\n".join([
        f"- {r.item_name}: {r.result_value} {r.unit or ''} (参考值: {r.ref_range or 'N/A'}, 异常: {r.abnormal_flag or '否'})"
        for r in request.results
    ])
    return LAB_SUMMARY_PROMPT.format(
        patient_name=request.patient_name,
        order_no=request.order_no,
        specimen_type=request.specimen_type,
        results_text=results_text
    )

IMAGING_SUMMARY_PROMPT = """你是一位医学影像报告摘要助手。请根据以下影像报告生成简洁摘要。

患者：{patient_name}
检查单号：{order_no}
检查部位：{body_part}
检查方式：{modality}
报告医生：{report_doctor}

报告内容：
检查所见：{report_content}

诊断意见：{impression}

请按以下格式输出：
1. 检查概述：简要说明检查情况
2. 主要发现：列出关键影像学发现
3. 诊断要点：总结诊断意见

注意：只做客观报告摘要，不做诊断建议。输出内容需标注"辅助生成，仅供参考"。
"""

def build_imaging_summary_prompt(request) -> str:
    return IMAGING_SUMMARY_PROMPT.format(
        patient_name=request.patient_name,
        order_no=request.order_no,
        body_part=request.body_part,
        modality=request.modality,
        report_content=request.report_content or "无",
        impression=request.impression or "无",
        report_doctor=request.report_doctor or "未知"
    )
