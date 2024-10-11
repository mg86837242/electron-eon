package dev.sy.electroneon.product.dao;

import dev.sy.electroneon.product.Category;
import dev.sy.electroneon.product.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ProductHibernateDataAccessService implements ProductDao {

    @PersistenceContext
    private EntityManager em;

    public ProductHibernateDataAccessService() {
    }

    @Override
    public List<Product> selectAllProducts() {
        String jpql = "SELECT p from Product p";

        return em
                .createQuery(jpql, Product.class)
                .getResultList();
    }

    @Override
    public Optional<Product> selectProductById(UUID productId) {
        return Optional.ofNullable(
                em.find(Product.class, productId)
        );
    }

    @Override
    public List<Product> selectProductsByCategory(Category category) {
        // NB Using Enums in JPQL @see: https://www.baeldung.com/jpa-persisting-enums-in-jpa
        String jpql = "SELECT p from Product p WHERE p.category = :category";

        return em
                .createQuery(jpql, Product.class)
                .setParameter("category", category)
                .getResultList();
    }

    @Override
    public boolean existsProductById(UUID productId) {
        return selectProductById(productId).isPresent();
    }

    @Override
    @Transactional
    public void insertProduct(Product product) {
        em.persist(product);
    }

    @Override
    @Transactional
    public void updateProduct(Product update) {
        // Destructure the `update` object
        UUID productId = update.getId();
        String name = update.getName();
        String description = update.getDescription();
        Double price = update.getPrice();
        Category category = update.getCategory();

        Optional<Product> optionalProduct = Optional.ofNullable(
                em.find(Product.class, productId)
        );
        optionalProduct.ifPresent(product -> {
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setCategory(category);
        });
    }

    @Override
    @Transactional
    public void deleteProductById(UUID productId) {
        Optional<Product> optionalProduct = Optional.ofNullable(
                em.find(Product.class, productId)
        );
        optionalProduct.ifPresent(product -> em.remove(product));
    }
}
