package com.medical360.dto;

import lombok.Data;
import java.util.List;

@Data
public class DepartmentDTO {
    private Long id;
    private String name;
    private Long parentId;
    private String parentName;
    private List<String> positions;
}
