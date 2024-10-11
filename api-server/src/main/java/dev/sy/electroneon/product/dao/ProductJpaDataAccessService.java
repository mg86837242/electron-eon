package dev.sy.electroneon.product.dao;

import dev.sy.electroneon.product.Category;
import dev.sy.electroneon.product.Product;
import dev.sy.electroneon.product.ProductRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Primary
public class ProductJpaDataAccessService implements ProductDao {

    private final ProductRepository productRepository;

    public ProductJpaDataAccessService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> selectAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> selectProductById(UUID productId) {
        return productRepository.findById(productId);
    }

    @Override
    public List<Product> selectProductsByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public boolean existsProductById(UUID productId) {
        return productRepository.existsById(productId);
    }

    @Override
    public void insertProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public void updateProduct(Product update) {
        productRepository.save(update);
    }

    @Override
    public void deleteProductById(UUID productId) {
        productRepository.deleteById(productId);
    }
}
