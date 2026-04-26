package com.medical360.service.impl;

import com.medical360.dto.DepartmentDTO;
import com.medical360.entity.Department;
import com.medical360.mapper.DepartmentMapper;
import com.medical360.mapper.PositionMapper;
import com.medical360.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentMapper departmentMapper;
    private final PositionMapper positionMapper;

    @Override
    public List<DepartmentDTO> listAllDepartments() {
        List<Department> departments = departmentMapper.selectList(null);
        return departments.stream().map(this::toDTO).toList();
    }

    @Override
    public DepartmentDTO getDepartmentById(Long id) {
        Department dept = departmentMapper.selectById(id);
        if (dept == null) {
            throw new RuntimeException("部门不存在");
        }
        return toDTO(dept);
    }

    @Override
    @Transactional
    public Department createDepartment(Department department) {
        departmentMapper.insert(department);
        return department;
    }

    @Override
    @Transactional
    public Department updateDepartment(Long id, Department department) {
        Department existing = departmentMapper.selectById(id);
        if (existing == null) {
            throw new RuntimeException("部门不存在");
        }
        existing.setName(department.getName());
        existing.setParentId(department.getParentId());
        departmentMapper.updateById(existing);
        return existing;
    }

    @Override
    @Transactional
    public void deleteDepartment(Long id) {
        departmentMapper.deleteById(id);
    }

    private DepartmentDTO toDTO(Department dept) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(dept.getId());
        dto.setName(dept.getName());
        dto.setParentId(dept.getParentId());
        if (dept.getParentId() != null) {
            Department parent = departmentMapper.selectById(dept.getParentId());
            dto.setParentName(parent != null ? parent.getName() : null);
        }
        return dto;
    }
}
