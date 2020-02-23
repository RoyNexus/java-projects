package com.metrics.steps;

import com.metrics.controllers.dto.CycleTimeDto;
import com.metrics.controllers.dto.TeamDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class World {

    TeamDto teamDto;
    CycleTimeDto cycleTimeDto;

    ResponseEntity<TeamDto> teamResponseEntity;
    ResponseEntity<List<TeamDto>> teamDtoList;
    ResponseEntity<CycleTimeDto> cycleTimeResponseEntity;

    int httpCodeResponse;

}
