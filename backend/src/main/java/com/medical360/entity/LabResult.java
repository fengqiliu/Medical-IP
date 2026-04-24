package com.medical360.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("lab_result")
public class LabResult {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long labOrderId;
    private String itemCode;
    private String itemName;
    private String resultValue;
    private String unit;
    private BigDecimal refRangeLow;
    private BigDecimal refRangeHigh;
    private String abnormalFlag;
    private LocalDateTime resultDatetime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}