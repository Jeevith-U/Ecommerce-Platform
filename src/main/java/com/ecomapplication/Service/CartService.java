package com.ecomapplication.Service;

import com.ecomapplication.Dto.CartDTO;
import org.springframework.stereotype.Service;

@Service
public interface CartService {

    public CartDTO addProductToCart(String productId, Integer quantity);
}
