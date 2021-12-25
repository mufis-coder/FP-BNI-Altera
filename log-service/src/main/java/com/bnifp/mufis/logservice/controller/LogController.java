package com.bnifp.mufis.logservice.controller;

import com.bnifp.mufis.logservice.model.Log;
import com.bnifp.mufis.logservice.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Log log){
        Log logCreated = logService.create(log);
        return ResponseEntity.ok(logCreated);
    }
}
