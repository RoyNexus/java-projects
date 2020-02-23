package com.metrics.steps;

import com.metrics.controllers.dto.TeamDto;
import io.cucumber.java8.En;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import java.net.URI;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

public class TeamSteps implements En {

    private static final String API_TEAMS = "/api/v1/teams";

    private final TestRestTemplate restTemplate;
    private final World world;

    public TeamSteps(TestRestTemplate restTemplate, World world) {

        this.restTemplate = requireNonNull(restTemplate,
                "TestRestTemplate must not be null!");
        this.world = requireNonNull(world,
                "World must not be null!");

        Given("a team with name {string}",
                (String teamName) -> {
                    this.world.teamDto = new TeamDto();
                    this.world.teamDto.setName(teamName);
                });

        When("the client send a POST with previous team",
                () -> {
                    assertThat(this.world.teamDto).isNotNull();
                    this.world.teamResponseEntity = this.restTemplate
                            .postForEntity(API_TEAMS, this.world.teamDto, TeamDto.class);
                    assertThat(this.world.teamResponseEntity).isNotNull();
                    this.world.httpCodeResponse = this.world.teamResponseEntity.getStatusCodeValue();
                });


        And("the client receives a team with name {string} and non-empty id",
                (String teamName) -> {
                    assertThat(this.world.teamResponseEntity.getBody()).isNotNull();
                    assertThat(this.world.teamResponseEntity.getBody().getName()).isEqualTo(teamName);
                    assertThat(this.world.teamResponseEntity.getBody().getId()).isNotEmpty();
                });


        When("the client retrieves all teams",
                () -> {
                    this.world.teamDtoList = this.restTemplate.exchange(URI.create(API_TEAMS),
                            HttpMethod.GET,
                            HttpEntity.EMPTY,
                            ParameterizedTypeReference.forType(List.class)
                    );
                    this.world.httpCodeResponse = this.world.teamDtoList.getStatusCodeValue();
                });


        Then("the client receives {int} teams",
                (Integer numberOfTeams) -> {
                    assertThat(this.world.teamDtoList).isNotNull();
                    assertThat(this.world.teamDtoList.getBody()).isNotNull();
                    assertThat(this.world.teamDtoList.getBody().size()).isEqualTo(numberOfTeams);
                });


        Then("the client receives status code of {int}",
                (Integer httpStatusCode) ->
                        assertThat(this.world.httpCodeResponse).isEqualTo(httpStatusCode)
        );

    }
}