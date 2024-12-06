package com.ecomapplication.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private String orderItemId;
    private ProductDTO product;
    private Integer quantity;
    private double discount;
    private double orderedProductPrice;
}
