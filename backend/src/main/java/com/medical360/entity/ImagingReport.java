package com.medical360.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("imaging_report")
public class ImagingReport {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long imagingOrderId;
    private String reportContent;
    private String impression;
    private String reportDoctor;
    private LocalDateTime reportDatetime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}