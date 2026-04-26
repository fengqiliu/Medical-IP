package com.medical360.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("patient")
public class Patient {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long emrPatientId;
    private String unifiedPatientId;
    private String name;
    private String gender;
    private LocalDate birthDate;
    private String idCardNo;
    private String phone;
    private String address;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
