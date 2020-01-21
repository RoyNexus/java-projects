package com.metrics.controllers;

import com.metrics.controllers.dto.TeamDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TeamControllerTest {

    private static final String TEAM_NAME_A = "Squad A";
    private static final String TEAM_NAME_B = "Squad B";
    private static final String TEAM_NAME_C = "Squad C";
    public static final String API_TEAMS = "/api/v1/teams";

    @Autowired
    private WebTestClient testClient;

    @Test
    public void postNewTeamOK() {
        // Given:
        TeamDto teamDto = new TeamDto();
        teamDto.setName(TEAM_NAME_A);
        // When && Then:
        this.testClient.post()
                .uri(API_TEAMS)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(teamDto), TeamDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.name").isEqualTo(TEAM_NAME_A)
                .jsonPath("$.id").isNotEmpty();
    }

    @Test
    public void getTeamsOK() {
        // Given:
        Stream.of(TEAM_NAME_A, TEAM_NAME_B, TEAM_NAME_C)
                .forEach(this::addTeam);

        // When && Then:
        this.testClient.get()
                .uri(API_TEAMS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$").isNotEmpty()
                .jsonPath("$[0].id").isNotEmpty()
                .jsonPath("$[1].id").isNotEmpty()
                .jsonPath("$[2].id").isNotEmpty()
                .jsonPath("$[3]").doesNotExist();
    }

    private void addTeam(String teamName) {
        TeamDto teamDto = new TeamDto();
        teamDto.setName(teamName);
        this.testClient.post()
                .uri(API_TEAMS)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(teamDto), TeamDto.class)
                .exchange();
    }

}
