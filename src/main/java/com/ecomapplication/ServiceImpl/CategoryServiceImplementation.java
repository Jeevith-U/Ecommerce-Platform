package com.ecomapplication.ServiceImpl;

import com.ecomapplication.Dto.CategoryDTO;
import com.ecomapplication.Dto.CategoryResponse;
import com.ecomapplication.Entity.Category;
import com.ecomapplication.Exception.APIException;
import com.ecomapplication.Exception.ResourceNotFoundException;
import com.ecomapplication.Mapper.CategoryMapper;
import com.ecomapplication.Repository.CategoryRepository;
import com.ecomapplication.Service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public class CategoryServiceImplementation implements CategoryService {

    private CategoryMapper categoryMapper;

    private CategoryRepository categoryRepository;

    public CategoryServiceImplementation(CategoryMapper categoryMapper, CategoryRepository repository){
        this.categoryMapper = categoryMapper;
        this.categoryRepository = repository;
    }

    @Override
    public CategoryResponse getAllCategories() {
        return null;
    }

    @Override
    public ResponseEntity<CategoryDTO> createCategory(CategoryDTO categoryDTO) {

        Category category = categoryMapper.converToCategory(categoryDTO);

        Category catFromDb = categoryRepository.findByCategoryName(categoryDTO.getCategoryName());

        if(catFromDb != null){
            throw  new APIException("Category with the name " + category.getCategoryName() + " already exists !!!") ;
        }

        Category savedCat = categoryRepository.save(category);

        return new ResponseEntity<>(categoryMapper.converToCategoryDTO(savedCat), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<CategoryDTO> updateCategory(CategoryDTO categoryDTO, String categoryId) {

        Category foundCat = categoryRepository.findById(categoryId).
                orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        Category category = categoryMapper.converToCategory(categoryDTO);
        category.setCategoryName(categoryDTO.getCategoryName());
        Category savedCat = categoryRepository.save(category);
        return ResponseEntity.ok(categoryMapper.converToCategoryDTO(savedCat));

    }

    @Override
    public ResponseEntity<CategoryDTO> deleteCategory(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));

        categoryRepository.delete(category);
        return ResponseEntity.ok(categoryMapper.converToCategoryDTO(category));

    }
}
