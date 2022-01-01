package com.bnifp.mufis.logservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class BaseResponse<T> {
    private Boolean success = Boolean.TRUE;
    private String message = "Operation succes";

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public BaseResponse(T data){
        this.data = data;
    }

    public BaseResponse(Boolean success, String message){
        this.success = success;
        this.message = message;
    }
}
