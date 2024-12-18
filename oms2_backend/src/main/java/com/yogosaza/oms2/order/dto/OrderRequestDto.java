package com.yogosaza.oms2.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {

    private Integer userId;
    private Integer productId;
    private Integer quantity;
    private Integer price;

}
