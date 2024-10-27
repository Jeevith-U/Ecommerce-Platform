package com.ecomapplication.Repository;

import com.ecomapplication.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {

    Category findByCategoryName(String categoryName);
}
