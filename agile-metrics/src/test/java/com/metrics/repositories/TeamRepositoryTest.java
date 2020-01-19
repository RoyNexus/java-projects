package com.metrics.repositories;

import com.metrics.domain.Team;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Slf4j
public class TeamRepositoryTest {

    @Autowired
    private TeamRepository repository;

    @Test
    public void saveOneTeamOK() {
        // Given:
        Team team = new Team();
        team.setName("Squad 1");
        // When:
        Mono<Team> monoTeam = this.repository.save(team);
        // Then:
        StepVerifier
                .create(monoTeam)
                .assertNext(item -> {
                    assertNotNull(item.getId());
                    assertThat(item.getName()).isEqualTo("Squad 1");
                    log.info("Team ID: " + item.getId());
                })
                .expectComplete()
                .verify();


    }
}
