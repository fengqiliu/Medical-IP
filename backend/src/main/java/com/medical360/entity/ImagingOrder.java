package com.medical360.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("imaging_order")
public class ImagingOrder {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long patientId;
    private Long encounterId;
    private String orderNo;
    private Long departmentId;
    private String bodyPart;
    private String modality;
    private LocalDateTime orderDatetime;
    private String status;
    private String pacsUrl;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}