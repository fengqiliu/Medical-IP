package com.medical360.service;

import com.medical360.dto.PositionDTO;
import com.medical360.entity.Position;
import java.util.List;

public interface PositionService {

    List<PositionDTO> listPositions();

    PositionDTO getPositionById(Long id);

    Position createPosition(Position position);

    Position updatePosition(Long id, Position position);

    void deletePosition(Long id);
}
