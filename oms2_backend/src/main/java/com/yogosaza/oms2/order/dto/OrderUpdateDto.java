package com.yogosaza.oms2.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderUpdateDto {

    private Integer id;
    private Integer quantity;
    private Integer price;
    private String status;

}
