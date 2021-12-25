package com.bnifp.mufis.logservice.repository;

import com.bnifp.mufis.logservice.model.Log;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends MongoRepository<Log, String> {
}
