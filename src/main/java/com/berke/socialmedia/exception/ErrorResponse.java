package com.berke.socialmedia.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private Date time;
    private String message;
    private String details;

    public ErrorResponse(Date time, String message) {
        this.time = time;
        this.message = message;
    }
}
