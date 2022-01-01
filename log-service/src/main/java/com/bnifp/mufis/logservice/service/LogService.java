package com.bnifp.mufis.logservice.service;

import com.bnifp.mufis.logservice.dto.input.LogInput;
import com.bnifp.mufis.logservice.dto.response.BaseResponse;
import com.bnifp.mufis.logservice.model.Log;
import org.springframework.http.ResponseEntity;

public interface LogService {
    Log create(LogInput input);
    ResponseEntity<BaseResponse> getAll();
}
