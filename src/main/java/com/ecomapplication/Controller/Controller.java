package com.ecomapplication.Controller;

import com.ecomapplication.Dto.CategoryDTO;
import com.ecomapplication.Dto.CategoryResponse;
import com.ecomapplication.Service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Api")
public class Controller {

    @Autowired
    public CategoryService categoryService;

    @PostMapping("/public/categories")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        return categoryService.createCategory(categoryDTO);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable String categoryId){
        return categoryService.deleteCategory(categoryId);
    }


    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO,
                                                      @PathVariable String categoryId){
        return categoryService.updateCategory(categoryDTO, categoryId);
    }

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(){
        return categoryService.getAllCategories() ;
    }

}