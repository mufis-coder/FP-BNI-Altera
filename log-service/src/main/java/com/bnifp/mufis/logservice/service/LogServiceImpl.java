package com.bnifp.mufis.logservice.service;

import com.bnifp.mufis.logservice.model.Log;
import com.bnifp.mufis.logservice.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService{

    private final LogRepository logRepository;

    @Override
    public Log create(Log log){
        return logRepository.save(log);
    }

}
