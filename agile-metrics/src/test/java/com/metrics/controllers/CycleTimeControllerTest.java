package com.metrics.controllers;

import com.metrics.controllers.dto.CycleTimeDto;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CycleTimeControllerTest {

    private static final String TEAM_ID = ObjectId.get().toString();
    private static final String API_CYCLETIME = "/api/v1/teams/" + TEAM_ID + "/cycle-times";

    @Autowired
    private WebTestClient testClient;

    @Test
    public void postNewCycleTimeOK() {
        // Given:
        CycleTimeDto cycleTimeDto = new CycleTimeDto();
        cycleTimeDto.setStartDate(LocalDate.of(2019, 1, 10));
        cycleTimeDto.setEndDate(LocalDate.of(2019, 1, 19));

        // When && Then:
        this.testClient.post()
                .uri(API_CYCLETIME)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(cycleTimeDto), CycleTimeDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.startDate").isNotEmpty()
                .jsonPath("$.endDate").isNotEmpty();
    }

}
