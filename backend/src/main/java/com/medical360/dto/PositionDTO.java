package com.medical360.dto;

import lombok.Data;

@Data
public class PositionDTO {
    private Long id;
    private String name;
    private Long departmentId;
    private String departmentName;
    private String dataScope;
}
