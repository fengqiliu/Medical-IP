package com.medical360.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("clinical_event")
public class ClinicalEvent {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long patientId;
    private String eventType;
    private LocalDateTime eventDatetime;
    private String eventSummary;
    private String eventStatus;
    private Long referenceId;
    private Long departmentId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}