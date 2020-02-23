package com.metrics.steps;

import com.metrics.controllers.dto.CycleTimeDto;
import io.cucumber.java8.En;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.time.LocalDate;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

public class CycleTimeSteps implements En {

    private static final String API_CYCLETIME = "/api/v1/teams/{id}/cycle-times";

    private final TestRestTemplate restTemplate;
    private final World world;

    public CycleTimeSteps(TestRestTemplate restTemplate, World world) {

        this.restTemplate = requireNonNull(restTemplate,
                "TestRestTemplate must not be null!");
        this.world = requireNonNull(world,
                "World must not be null!");

        Given("a cycle time with start date {string} and end date {string}",
                (String startDate, String endDate) -> {
                    this.world.cycleTimeDto = new CycleTimeDto();
                    this.world.cycleTimeDto.setStartDate(LocalDate.parse(startDate));
                    this.world.cycleTimeDto.setEndDate(LocalDate.parse(endDate));
                });

        When("the client send a POST with previous cycle time and previous created team",
                () -> {
                    assertThat(this.world.cycleTimeDto).isNotNull();
                    assertThat(this.world.teamResponseEntity.getBody()).isNotNull();
                    assertThat(this.world.teamResponseEntity.getBody().getId()).isNotNull();

                    this.world.cycleTimeResponseEntity = this.restTemplate
                            .postForEntity(API_CYCLETIME, this.world.cycleTimeDto, CycleTimeDto.class,
                                    this.world.teamResponseEntity.getBody().getId());
                    assertThat(this.world.cycleTimeResponseEntity).isNotNull();
                    this.world.httpCodeResponse = this.world.cycleTimeResponseEntity.getStatusCodeValue();
                });

        Then("the client receives a cycle time from {string} to {string} and non-empty id",
                (String startDate, String endDate) -> {
                    assertThat(this.world.cycleTimeResponseEntity.getBody()).isNotNull();
                    assertThat(this.world.cycleTimeResponseEntity.getBody().getId()).isNotEmpty();
                    assertThat(this.world.cycleTimeResponseEntity.getBody().getStartDate()).isEqualTo(startDate);
                    assertThat(this.world.cycleTimeResponseEntity.getBody().getEndDate()).isEqualTo(endDate);
                });


    }
}
