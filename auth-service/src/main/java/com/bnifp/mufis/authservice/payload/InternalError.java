package com.bnifp.mufis.authservice.payload;

import lombok.Data;

import java.util.Date;

@Data
public class InternalError {
    private Date timestamp = new Date();
    private int status = 500;
    private String message = "Internal Server Error";

    public InternalError() {}
    public InternalError(String message) {
        this.message = message;
    }

    public InternalError(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
