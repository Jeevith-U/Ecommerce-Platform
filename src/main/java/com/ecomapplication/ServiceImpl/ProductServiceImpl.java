package com.ecomapplication.ServiceImpl;

import com.ecomapplication.Dto.CartDTO;
import com.ecomapplication.Dto.ProductDTO;
import com.ecomapplication.Dto.ProductResponse;
import com.ecomapplication.Entity.Cart;
import com.ecomapplication.Entity.Category;
import com.ecomapplication.Entity.Product;
import com.ecomapplication.Exception.APIException;
import com.ecomapplication.Exception.ResourceNotFoundException;
import com.ecomapplication.Repository.CartRepository;
import com.ecomapplication.Repository.CategoryRepository;
import com.ecomapplication.Repository.ProductRepository;
import com.ecomapplication.Service.CartService;
import com.ecomapplication.Service.FileService;
import com.ecomapplication.Service.ProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CartRepository cartRepository ;

    @Autowired
    private CartService cartService ;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    private Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private FileService fileService ;

    @Value("${project.image}")
    private String path;

    @Value("${image.base.url}")
    private String imageBaseUrl;


    @Autowired
    public ProductServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository, ModelMapper modelMapper, FileService fileService) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.fileService = fileService;
    }

    @Override
    public ResponseEntity<ProductDTO> addProduct(String categoryId, ProductDTO productDTO) {

        Category category = categoryRepository.findById(categoryId).
                orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        boolean isProductPresent = category.getProducts().stream()
                .anyMatch(product -> product.getProductName().equalsIgnoreCase(productDTO.getProductName()));

        if (!isProductPresent){
            Product product = modelMapper.map(productDTO, Product.class);
            product.setImage("default.img");
            product.setCategory(category);
            product.setSpecialPrice(product.getPrice() - ((product.getDiscount()) * 0.01) * product.getPrice() );
            Product savedProd = productRepository.save(product);
            ProductDTO savedProductDto = modelMapper.map(savedProd, ProductDTO.class);
            return new ResponseEntity<>(savedProductDto, HttpStatus.CREATED);
        }
        throw new APIException("Product with the ID " + productDTO.getProductId() + " already exists in this category.");
    }

//    @Override
    public ResponseEntity<ProductResponse> getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber , pageSize, sortByAndOrder);

        Page<Product> products = productRepository.findAll(pageable);

        List<Product> prodList = products.getContent();

        List<ProductDTO> prodDtoList = products.stream()
                .map(product -> {
                    ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
                    productDTO.setImage(constructImageURL(product.getImage()));
                    return productDTO;
                })
                .toList() ;

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(prodDtoList);
        productResponse.setPageNumber(products.getNumber());
        productResponse.setPageSize(products.getSize());
        productResponse.setTotalElements(products.getTotalElements());
        productResponse.setTotalPages(products.getTotalPages());
        productResponse.setLastPage(products.isLast());

        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ProductResponse> searchByCategory(String categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        logger.info("Retrieving the Category For searchByCategory() ");

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber , pageSize, sortByAndOrder);

        Page<Product> products = productRepository.findByCategoryOrderByPriceAsc(category, pageable);

        List<Product> prodList = products.getContent();

        if(prodList.isEmpty()) throw new APIException(category.getCategoryName() + " category does not have any products");

        List<ProductDTO> prodDtoList = products.stream().map(
                        product -> modelMapper.map(product, ProductDTO.class))
                .toList() ;

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(prodDtoList);
        productResponse.setPageNumber(products.getNumber());
        productResponse.setPageSize(products.getSize());
        productResponse.setTotalElements(products.getTotalElements());
        productResponse.setTotalPages(products.getTotalPages());
        productResponse.setLastPage(products.isLast());

        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    private String constructImageURL(String imageName) {
        return imageName.endsWith("/")? imageBaseUrl + imageName : imageBaseUrl+ "/" + imageName ;
    }


    @Override
    public ResponseEntity<ProductResponse> searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber , pageSize, sortByAndOrder);

        Page<Product> products = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%', pageable);

        List<Product> prodList = products.getContent();

        List<ProductDTO> prodDtoList = products.stream().map(
                        product -> modelMapper.map(product, ProductDTO.class))
                .toList() ;
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(prodDtoList);
        productResponse.setPageNumber(products.getNumber());
        productResponse.setPageSize(products.getSize());
        productResponse.setTotalElements(products.getTotalElements());
        productResponse.setTotalPages(products.getTotalPages());
        productResponse.setLastPage(products.isLast());

        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ProductDTO> updateProduct(String productId, ProductDTO productDTO) {

        Product productFromDb = productRepository.findById(productId).
                orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        Product product = modelMapper.map(productDTO, Product.class);

        product.setProductId(productFromDb.getProductId());
        product.setProductName(productDTO.getProductName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setDiscount(productDTO.getDiscount());
        product.setQuantity(productDTO.getQuantity());
        product.setCategory(productFromDb.getCategory());
        product.setSpecialPrice(productDTO.getSpecialPrice());

        Product savedProd = productRepository.save(product);

        ProductDTO savedProductDto = modelMapper.map(product, ProductDTO.class);

        List<Cart> carts = cartRepository.findCartsByProductId(productId);

        List<CartDTO> cartDTOs = carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

            List<ProductDTO> products = cart.getCartItems().stream()
                    .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());

            cartDTO.setProducts(products);

            return cartDTO;

        }).collect(Collectors.toList());

        cartDTOs.forEach(cart -> cartService.updateProductInCarts(cart.getCartId(), productId));

        return ResponseEntity.ok(savedProductDto);
    }

    @Override
    public ResponseEntity<ProductDTO> deleteProduct(String productId) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        // Deleting the product from the cart
        List<Cart> carts = cartRepository.findCartsByProductId(productId);
        carts.forEach(cart -> cartService.deleteProductFromCart(cart.getCartId(), productId));


        productRepository.delete(product);

        return ResponseEntity.ok(modelMapper.map(product, ProductDTO.class));
    }

    @Override
    public ResponseEntity<ProductDTO> updateProductImage(String productId, MultipartFile image) throws IOException {

        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        String fileName = fileService.uploadImage(path, image);
        productFromDb.setImage(fileName);

        Product updatedProduct = productRepository.save(productFromDb);
        ProductDTO updatedProdDto = modelMapper.map(updatedProduct, ProductDTO.class);

        return ResponseEntity.ok(updatedProdDto) ;

    }
}
