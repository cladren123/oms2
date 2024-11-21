package com.yogosaza.oms2.handler;



import com.yogosaza.oms2.exception.CommonException;
import com.yogosaza.oms2.util.Util;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CommonException.class)
    public ErrorResponseDto handleException(CommonException e) {
        return ErrorResponseDto.builder()
                .code(e.getCode())
                .message(e.getMessage())
                .timestamp(Util.seoulTime())
                .build();
    }

}
