package com.bnifp.mufis.authservice.controller;

import com.bnifp.mufis.authservice.dto.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class BaseController {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BaseResponse<?> handleValidationError(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        String defaultMessage = fieldError.getDefaultMessage();
        BaseResponse<?> response = new BaseResponse<>(null);
        response.setMessage(defaultMessage);
        response.setSuccess(false);
        return response;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BaseResponse<?> handleGeneralException(Exception ex) {
        String message = ex.getMessage();
        BaseResponse<?> response = new BaseResponse<>(null);
        response.setMessage(message);
        response.setSuccess(false);
        return response;
    }

    @ExceptionHandler(MissingPathVariableException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BaseResponse<?> handleMissingPathVariableException(MissingPathVariableException ex) {
        String message = "Missing Path Variable";
        BaseResponse<?> response = new BaseResponse<>(null);
        response.setMessage(message);
        response.setSuccess(false);
        return response;
    }

}

