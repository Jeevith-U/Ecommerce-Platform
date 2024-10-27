package com.ecomapplication.Mapper;

import com.ecomapplication.Dto.CategoryDTO;
import com.ecomapplication.Entity.Category;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    @Autowired
    private ModelMapper modelMapper;

    public Category converToCategory(CategoryDTO categoryDTO) {

        return modelMapper.map(categoryDTO, Category.class);
    }

    public CategoryDTO converToCategoryDTO(Category category) {

        return modelMapper.map(category, CategoryDTO.class);
    }
}
