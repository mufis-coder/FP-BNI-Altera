package com.bnifp.mufis.logservice.service;

import com.bnifp.mufis.logservice.dto.input.LogInput;
import com.bnifp.mufis.logservice.model.Log;
import com.bnifp.mufis.logservice.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService{

    @Autowired
    private ModelMapper mapper;

    @Autowired
    public void setMapper(ModelMapper mapper){
        this.mapper = mapper;
    }

    private final LogRepository logRepository;

    @Override
    public Log create(LogInput input){
        Log log = this.mapper.map(input, Log.class);
        log.setDate();
        logRepository.save(log);
        return log;
    }

}
