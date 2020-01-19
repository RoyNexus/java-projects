package com.metrics.mappers;

import com.metrics.controllers.dto.CycleTimeDto;
import com.metrics.domain.CycleTime;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CycleTimeMapper {

    CycleTime toCycleTime(CycleTimeDto cycleTimeDto);

    CycleTimeDto toCycleTimeDto(CycleTime cycleTime);

}
