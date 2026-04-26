package com.medical360.service;

import com.medical360.dto.DepartmentDTO;
import com.medical360.entity.Department;
import java.util.List;

public interface DepartmentService {

    List<DepartmentDTO> listAllDepartments();

    DepartmentDTO getDepartmentById(Long id);

    Department createDepartment(Department department);

    Department updateDepartment(Long id, Department department);

    void deleteDepartment(Long id);
}
