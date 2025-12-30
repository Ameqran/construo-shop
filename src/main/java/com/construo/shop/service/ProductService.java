package com.construo.shop.service;

import com.construo.shop.domain.Category;
import com.construo.shop.domain.Product;
import com.construo.shop.dto.ProductCreateRequest;
import com.construo.shop.dto.ProductDto;
import com.construo.shop.exception.ResourceNotFoundException;
import com.construo.shop.repository.CategoryRepository;
import com.construo.shop.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public ProductDto create(ProductCreateRequest request) {
        Category category = getCategory(request.categoryId());
        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setCategory(category);
        product.setPrice(request.price());
        product.setStockQuantity(request.stockQuantity());
        product.setUnit(request.unit());
        Product saved = productRepository.save(product);
        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<ProductDto> findAll() {
        return productRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductDto> findAvailable() {
        return productRepository.findAvailableProducts().stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductDto> findByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId).stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductDto> searchByName(String keyword) {
        String pattern = "%" + keyword + "%";
        return productRepository.findByNameLike(pattern).stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductDto findById(Long id) {
        return toDto(getProduct(id));
    }

    public ProductDto update(Long id, ProductCreateRequest request) {
        Product product = getProduct(id);
        Category category = getCategory(request.categoryId());
        product.setName(request.name());
        product.setDescription(request.description());
        product.setCategory(category);
        product.setPrice(request.price());
        product.setStockQuantity(request.stockQuantity());
        product.setUnit(request.unit());
        return toDto(product);
    }

    public void delete(Long id) {
        Product product = getProduct(id);
        productRepository.delete(product);
    }

    private Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
    }

    private Category getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));
    }

    private ProductDto toDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCategory().getId(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getUnit(),
                product.getCreatedAt()
        );
    }
}
