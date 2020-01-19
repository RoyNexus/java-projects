package com.metrics.mappers;

import com.metrics.controllers.dto.TeamDto;
import com.metrics.domain.Team;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    Team toTeam(TeamDto teamDto);

    TeamDto toTeamDto(Team team);
}
