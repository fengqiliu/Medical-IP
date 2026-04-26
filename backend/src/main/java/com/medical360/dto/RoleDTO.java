package com.medical360.dto;

import lombok.Data;
import java.util.List;

@Data
public class RoleDTO {
    private Long id;
    private String name;
    private String description;
    private List<String> menuCodes;
    private List<String> menuNames;
}
