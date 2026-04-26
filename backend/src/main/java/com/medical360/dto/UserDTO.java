package com.medical360.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String name;
    private Long departmentId;
    private String departmentName;
    private Long positionId;
    private String positionName;
    private Boolean enabled;
    private List<String> roles;
    private LocalDateTime createdAt;
}
