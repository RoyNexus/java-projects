package com.metrics.repositories;

import com.metrics.domain.CycleTime;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CycleTimeRepository extends ReactiveMongoRepository<CycleTime, String> {

}
