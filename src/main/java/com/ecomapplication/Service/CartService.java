package com.ecomapplication.Service;

import com.ecomapplication.Dto.CartDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CartService {

    public CartDTO addProductToCart(String productId, Integer quantity);

    List<CartDTO> getAllCarts();
}
