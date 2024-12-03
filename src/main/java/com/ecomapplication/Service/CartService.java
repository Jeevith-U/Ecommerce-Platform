package com.ecomapplication.Service;

import com.ecomapplication.Dto.CartDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CartService {

    public CartDTO addProductToCart(String productId, Integer quantity);

    List<CartDTO> getAllCarts();

    CartDTO getCart(String emailId, String cartId);

    @Transactional
    CartDTO updateProductQuantityInCart(String productId, Integer quantity);

    String deleteProductFromCart(String cartId, String productId);

    void updateProductInCarts(String cartId, String productId);

}
