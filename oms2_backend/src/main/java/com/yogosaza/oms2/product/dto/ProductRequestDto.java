package com.yogosaza.oms2.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {

    private String name;
    private String description;
    private Integer quantity;
    private Integer price;

}
