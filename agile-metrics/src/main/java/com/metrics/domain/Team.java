package com.metrics.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("team")
@Data
public class Team {

    @Id
    private String id;
    private String name;

}
