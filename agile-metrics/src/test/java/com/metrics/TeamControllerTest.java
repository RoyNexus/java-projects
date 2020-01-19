package com.metrics;

import com.metrics.controllers.dto.TeamDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TeamControllerTest {

    private static final String TEAM_NAME = "Squad A";

    @Autowired
    private WebTestClient testClient;

    @Test
    public void postNewTeamOK() {
        // Given:
        TeamDto teamDto = new TeamDto();
        teamDto.setName(TEAM_NAME);
        // When && Then:
        this.testClient.post()
                .uri("/api/v1/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(teamDto), TeamDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.name").isEqualTo(TEAM_NAME)
                .jsonPath("$.id").isNotEmpty();
    }

}
