package dev.sy.electroneon.product;

import dev.sy.electroneon.product.dto.ProductWithQtyInCartProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    List<Product> findByCategory(Category category);

    List<Product> findAllByIsArchivedFalse();

    Optional<Product> findByIdAndIsArchivedFalse(UUID productId);

    List<Product> findByCategoryAndIsArchivedFalse(Category category);

    @Query("""
            SELECT
                p.id AS id,
                p.name AS name,
                p.description AS description,
                p.price AS price,
                p.category AS category,
                c.quantity AS quantity
            FROM Product p
            LEFT JOIN Cart c
            ON p.id = c.product.id AND c.user.id = :userId
            WHERE p.isArchived = false
            """)
    List<ProductWithQtyInCartProjection> findAllProductsWithQtyInCartByUserIdAndIsArchivedFalse(
            @Param("userId") UUID userId
    );

    @Query("""
            SELECT
                p.id AS id,
                p.name AS name,
                p.description AS description,
                p.price AS price,
                p.category AS category,
                c.quantity AS quantity
            FROM Product p
            LEFT JOIN Cart c
            ON p.id = c.product.id AND c.user.id = :userId
            WHERE p.isArchived = false AND p.id = :productId
            """)
    Optional<ProductWithQtyInCartProjection> findProductWithQtyInCartByUserIdAndByIdAndIsArchivedFalse(
            @Param("userId") UUID userId,
            @Param("productId") UUID productId
    );

    @Query("""
            SELECT
                p.id AS id,
                p.name AS name,
                p.description AS description,
                p.price AS price,
                p.category AS category,
                c.quantity AS quantity
            FROM Product p
            LEFT JOIN Cart c
            ON p.id = c.product.id AND c.user.id = :userId
            WHERE p.isArchived = false AND p.category = :category
            """)
    List<ProductWithQtyInCartProjection> findProductsWithQtyInCartByUserIdAndCategoryAndIsArchivedFalse(
            @Param("userId") UUID userId,
            @Param("category") Category category
    );

    boolean existsById(@NonNull UUID productId);

}
