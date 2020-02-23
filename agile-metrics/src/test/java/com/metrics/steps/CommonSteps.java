package com.metrics.steps;

import com.metrics.AgileMetricsApplication;
import io.cucumber.java8.En;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import static java.util.Objects.requireNonNull;

@SpringBootTest(classes = AgileMetricsApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class CommonSteps implements En {

    private final MongoTemplate mongoTemplate;

    public CommonSteps(MongoTemplate mongoTemplate) {

        this.mongoTemplate = requireNonNull(mongoTemplate, "MongoTemplate must not be null!");

        Before("@CleanDatabase", () -> {
            log.info("Cleaning database");
            this.mongoTemplate.dropCollection("team");
            this.mongoTemplate.dropCollection("cycleTime");
        });

    }
}
