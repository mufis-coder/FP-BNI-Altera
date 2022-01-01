package com.bnifp.mufis.logservice.controller;

import com.bnifp.mufis.logservice.dto.input.LogInput;
import com.bnifp.mufis.logservice.dto.response.BaseResponse;
import com.bnifp.mufis.logservice.model.Log;
import com.bnifp.mufis.logservice.service.KafkaConsumer;
import com.bnifp.mufis.logservice.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
public class LogController {

    @Autowired
    private KafkaConsumer kafkaConsumer;

    private final LogService logService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody LogInput input){
        Log logCreated = logService.create(input);
        return ResponseEntity.ok(logCreated);
    }

    @GetMapping
    public ResponseEntity<BaseResponse> getAll() {
        return logService.getAll();
    }
}
