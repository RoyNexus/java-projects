package com.metrics.controllers.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CycleTimeDto {

    private String id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String teamId;
}
