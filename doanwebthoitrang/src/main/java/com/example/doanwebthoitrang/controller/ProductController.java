package com.example.doanwebthoitrang.controller;

import com.example.doanwebthoitrang.DTO.ApiResponse;
import com.example.doanwebthoitrang.dto.ProductDTO;
import com.example.doanwebthoitrang.entity.entities.Brand;
import com.example.doanwebthoitrang.entity.entities.Category;
import com.example.doanwebthoitrang.entity.entities.Product;
import com.example.doanwebthoitrang.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController extends BaseController<Product, Integer> {
    // Service is inherited, but we might need the specific type for custom methods
    private final ProductService productService;

    public ProductController(ProductService productService) {
        super(productService);
        this.productService = productService;
    }

    @Override
    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<Page<ProductDTO>>> findAll(Pageable pageable) {
        Page<Product> productPage = service.findAll(pageable);
        Page<ProductDTO> productDTOPage = productPage.map(this::convertToDto);
        return ResponseEntity.ok(ApiResponse.success(productDTOPage, "Products retrieved successfully", HttpStatus.OK));
    }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<ProductDTO>> findById(@PathVariable Integer id) {
        Optional<Product> productOptional = service.findById(id);
        return productOptional.map(product -> ResponseEntity.ok(ApiResponse.success(convertToDto(product), "Product retrieved successfully", HttpStatus.OK)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Product not found", HttpStatus.NOT_FOUND)));
    }

    // Modify the existing search endpoint to return DTOs
    @GetMapping("/search")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<Page<ProductDTO>>> searchProducts(@RequestParam("query") String query, Pageable pageable) {
        Page<Product> productPage = productService.searchProducts(query, pageable); // Use the specific service method
        Page<ProductDTO> productDTOPage = productPage.map(this::convertToDto);
        return ResponseEntity.ok(ApiResponse.success(productDTOPage, "Products found successfully", HttpStatus.OK));
    }

    // Helper method to convert Product entity to ProductDTO
    private ProductDTO convertToDto(Product product) {
        if (product == null) {
            return null;
        }
        Category category = product.getCategory();
        Brand brand = product.getBrand();

        String categoryName = (category != null) ? category.getName() : null;
        String brandName = (brand != null) ? brand.getName() : null;

        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getImageUrl(),
                categoryName,
                brandName,
                product.getCreatedAt(),
                product.getUpdatedAt()
                // Add average rating/review count mapping here if needed
        );
    }

    // Note: Create, Update, Delete methods from BaseController still work with Product entity.
} 