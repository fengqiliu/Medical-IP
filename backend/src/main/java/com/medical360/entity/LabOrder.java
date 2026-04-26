package com.medical360.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("lab_order")
public class LabOrder {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long patientId;
    private Long encounterId;
    private String orderNo;
    private Long departmentId;
    private LocalDateTime orderDatetime;
    private String status;
    private String specimenType;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}