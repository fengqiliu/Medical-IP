package com.medical360.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("data_sync_log")
public class DataSyncLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String sourceType;
    private String dataType;
    private String lastSyncTime;
    private Integer syncedCount;
    private String status;
    private String errorMessage;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
