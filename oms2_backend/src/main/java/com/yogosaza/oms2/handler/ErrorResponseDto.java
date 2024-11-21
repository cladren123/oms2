package com.yogosaza.oms2.handler;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponseDto {

    private String code;
    private String message;
    private String timestamp;

}
