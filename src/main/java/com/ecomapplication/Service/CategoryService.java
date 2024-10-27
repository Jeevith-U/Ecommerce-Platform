package com.ecomapplication.Service;

import com.ecomapplication.Dto.CategoryDTO;
import com.ecomapplication.Dto.CategoryResponse;
import org.springframework.http.ResponseEntity;

public interface CategoryService {

    ResponseEntity<CategoryResponse> getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) ;

    ResponseEntity<CategoryDTO> createCategory(CategoryDTO categoryDTO);

    ResponseEntity<CategoryDTO> updateCategory(CategoryDTO categoryDTO, String categoryId);

    ResponseEntity<CategoryDTO> deleteCategory(String categoryId);
}
