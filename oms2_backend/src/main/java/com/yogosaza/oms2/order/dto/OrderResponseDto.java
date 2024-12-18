package com.yogosaza.oms2.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {

    private Integer id;
    private Integer userId;
    private Integer productId;
    private Integer quantity;
    private Integer price;
    private String status;
    private String createdDateTime;
    private boolean isDeleted;

}
