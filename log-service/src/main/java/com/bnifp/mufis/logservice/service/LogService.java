package com.bnifp.mufis.logservice.service;

import com.bnifp.mufis.logservice.dto.input.LogInput;
import com.bnifp.mufis.logservice.model.Log;

public interface LogService {
    Log create(LogInput input);
}
