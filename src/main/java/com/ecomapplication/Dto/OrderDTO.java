package com.ecomapplication.Dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private String orderId;
    private String email;
    private List<OrderItemDTO> orderItems;
    private LocalDate orderDate;
    private PaymentDTO payment;
    private Double totalAmount;
    private String orderStatus;
    private Long addressId;
}
