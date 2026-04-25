package com.medical360.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("encounter")
public class Encounter {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long emrEncounterId;
    private Long patientId;
    private String encounterType;
    private Long departmentId;
    private LocalDateTime visitDatetime;
    private String admissionReason;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
