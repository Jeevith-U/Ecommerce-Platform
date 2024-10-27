package com.ecomapplication.ServiceImpl;

import com.ecomapplication.Dto.CategoryDTO;
import com.ecomapplication.Dto.CategoryResponse;
import com.ecomapplication.Entity.Category;
import com.ecomapplication.Exception.APIException;
import com.ecomapplication.Exception.ResourceNotFoundException;
import com.ecomapplication.Mapper.CategoryMapper;
import com.ecomapplication.Repository.CategoryRepository;
import com.ecomapplication.Service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImplementation implements CategoryService {

    private CategoryMapper categoryMapper;

    private CategoryRepository categoryRepository;

    public CategoryServiceImplementation(CategoryMapper categoryMapper, CategoryRepository repository){
        this.categoryMapper = categoryMapper;
        this.categoryRepository = repository;
    }

    @Override
    public ResponseEntity<CategoryResponse> getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ?Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber , pageSize, sortByAndOrder) ;

        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);

        List<Category> categories = categoryPage.getContent();

        if (categories.isEmpty())
            throw new APIException("No category created till now.");



        List<CategoryDTO> response = categories.stream().map(
                        category -> categoryMapper.converToCategoryDTO(category))
                .toList();

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(response);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());

        return ResponseEntity.status(HttpStatus.OK).body(categoryResponse) ;


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
