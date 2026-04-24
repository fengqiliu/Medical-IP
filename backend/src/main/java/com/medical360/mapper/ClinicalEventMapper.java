package com.medical360.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medical360.entity.ClinicalEvent;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ClinicalEventMapper extends BaseMapper<ClinicalEvent> {
}