package com.medical360.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medical360.entity.Department;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
}