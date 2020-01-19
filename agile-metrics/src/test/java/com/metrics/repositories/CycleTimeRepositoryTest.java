package com.metrics.repositories;

import com.metrics.domain.CycleTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Slf4j
class CycleTimeRepositoryTest {

    @Autowired
    private CycleTimeRepository repository;

    @Test
    void saveOneCycleTimeOK() {
        // Given:
        CycleTime cycleTime = new CycleTime();
        cycleTime.setStartDate(LocalDate.of(2020, 1, 1));
        cycleTime.setEndDate(LocalDate.of(2020, 1, 10));

        // When:
        Mono<CycleTime> monoCycleTime = this.repository.save(cycleTime);

        // Then:
        StepVerifier
                .create(monoCycleTime)
                .assertNext(item -> {
                    assertNotNull(item.getId());
                    assertThat(item.getStartDate()).isEqualTo("2020-01-01");
                    assertThat(item.getEndDate()).isEqualTo("2020-01-10");
                    log.info("CycleTime ID: " + item.getId());
                })
                .expectComplete()
                .verify();

    }
}
