package com.bnifp.mufis.logservice.service.impl;

import com.bnifp.mufis.logservice.dto.input.LogInput;
import com.bnifp.mufis.logservice.dto.response.BaseResponse;
import com.bnifp.mufis.logservice.model.Log;
import com.bnifp.mufis.logservice.repository.LogRepository;
import com.bnifp.mufis.logservice.service.LogService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.IterableUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

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

    @Override
    public ResponseEntity<BaseResponse> getAll(){
        Iterable<Log> logs = logRepository.findAll();
        List<Log> logList = IterableUtils.toList(logs);

//        List<UserOutput> outputs = new ArrayList<>();
//
//        for(User post: userList){
//            outputs.add(mapper.map(post, UserOutput.class));
//        }
        return new ResponseEntity<BaseResponse>(new BaseResponse<>(logList), HttpStatus.OK);
    }

}
