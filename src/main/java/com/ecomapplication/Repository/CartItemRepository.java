package com.ecomapplication.Repository;

import com.ecomapplication.Entity.Cart;
import com.ecomapplication.Entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, String> {
}
