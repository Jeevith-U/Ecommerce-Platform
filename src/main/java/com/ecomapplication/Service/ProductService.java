package com.ecomapplication.Service;

import com.ecomapplication.Dto.ProductDTO;
import com.ecomapplication.Dto.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {

    ResponseEntity<ProductDTO> addProduct(String categoryId, ProductDTO product);

//    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ResponseEntity<ProductResponse> getAllProducts() ;

//    ProductResponse searchByCategory(String categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

//    ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ResponseEntity<ProductDTO> updateProduct(String productId, ProductDTO product);

    ResponseEntity<ProductDTO> deleteProduct(String productId);

    ResponseEntity<ProductDTO> updateProductImage(Long productId, MultipartFile image) throws IOException;

}
