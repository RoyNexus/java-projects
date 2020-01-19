package com.metrics.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document
@Data
public class CycleTime {

    @Id
    private String id;
    private LocalDate startDate;
    private LocalDate endDate;

    private String teamId;
}
