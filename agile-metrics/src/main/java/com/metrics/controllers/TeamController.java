package com.metrics.controllers;

import com.metrics.controllers.dto.TeamDto;
import com.metrics.mappers.TeamMapper;
import com.metrics.repositories.TeamRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.util.Objects.requireNonNull;

@RestController
@RequestMapping(path = "/api/v1/teams")
public class TeamController {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    public TeamController(TeamRepository teamRepository, TeamMapper teamMapper) {
        this.teamRepository = requireNonNull(teamRepository, "TeamRepository must not be null!");
        this.teamMapper = requireNonNull(teamMapper, "TeamMapper must not be null!");
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<TeamDto> addTeam(@RequestBody Mono<TeamDto> teamDtoMono) {
        return this.teamRepository
                .saveAll(teamDtoMono.map(this.teamMapper::toTeam))
                .next()
                .map(this.teamMapper::toTeamDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<TeamDto> getAllTeams() {
        return this.teamRepository
                .findAll()
                .map(this.teamMapper::toTeamDto);
    }

}
