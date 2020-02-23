package com.metrics.controllers;

import com.metrics.controllers.dto.CycleTimeDto;
import com.metrics.mappers.CycleTimeMapper;
import com.metrics.repositories.CycleTimeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static java.util.Objects.requireNonNull;

@RestController
public class CycleTimeController {

    private final CycleTimeRepository cycleTimeRepository;
    private final CycleTimeMapper cycleTimeMapper;

    public CycleTimeController(CycleTimeRepository cycleTimeRepository, CycleTimeMapper cycleTimeMapper) {
        this.cycleTimeRepository = requireNonNull(cycleTimeRepository, "CycleTimeRepository must not be null!");
        this.cycleTimeMapper = requireNonNull(cycleTimeMapper, "CycleTimeMapper must not be null!");
    }

    @PostMapping(path = "/api/v1/teams/{teamId}/cycle-times")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CycleTimeDto> addTeam(@PathVariable String teamId,
                                      @RequestBody Mono<CycleTimeDto> cycleTimeDtoMono) {

        return this.cycleTimeRepository
                .saveAll(cycleTimeDtoMono.map((cycleTimeDto) -> {
                    var cycleTime = this.cycleTimeMapper.toCycleTime(cycleTimeDto);
                    cycleTime.setTeamId(teamId);
                    return cycleTime;
                }))
                .next()
                .map(this.cycleTimeMapper::toCycleTimeDto);
    }

}
