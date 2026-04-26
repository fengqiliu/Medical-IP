package com.medical360.service.impl;

import com.medical360.dto.PositionDTO;
import com.medical360.entity.Position;
import com.medical360.mapper.DepartmentMapper;
import com.medical360.mapper.PositionMapper;
import com.medical360.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {

    private final PositionMapper positionMapper;
    private final DepartmentMapper departmentMapper;

    @Override
    public List<PositionDTO> listPositions() {
        List<Position> positions = positionMapper.selectList(null);
        return positions.stream().map(this::toDTO).toList();
    }

    @Override
    public PositionDTO getPositionById(Long id) {
        Position position = positionMapper.selectById(id);
        if (position == null) {
            throw new RuntimeException("岗位不存在");
        }
        return toDTO(position);
    }

    @Override
    @Transactional
    public Position createPosition(Position position) {
        positionMapper.insert(position);
        return position;
    }

    @Override
    @Transactional
    public Position updatePosition(Long id, Position position) {
        Position existing = positionMapper.selectById(id);
        if (existing == null) {
            throw new RuntimeException("岗位不存在");
        }
        existing.setName(position.getName());
        existing.setDepartmentId(position.getDepartmentId());
        existing.setDataScope(position.getDataScope());
        positionMapper.updateById(existing);
        return existing;
    }

    @Override
    @Transactional
    public void deletePosition(Long id) {
        positionMapper.deleteById(id);
    }

    private PositionDTO toDTO(Position position) {
        PositionDTO dto = new PositionDTO();
        dto.setId(position.getId());
        dto.setName(position.getName());
        dto.setDepartmentId(position.getDepartmentId());
        dto.setDataScope(position.getDataScope());
        if (position.getDepartmentId() != null) {
            var dept = departmentMapper.selectById(position.getDepartmentId());
            dto.setDepartmentName(dept != null ? dept.getName() : null);
        }
        return dto;
    }
}
