package com.medical360.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("access_log")
public class AccessLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long patientId;
    private String action;
    private String requestDetail;
    private String ipAddress;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime accessDatetime;
}
