package dev.sy.electroneon.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {

    List<Cart> findByUserId(UUID userId);

    @Query("SELECT c FROM Cart c WHERE c.user.email = :email")
    List<Cart> findByEmail(String email);

    @Query("SELECT c FROM Cart c WHERE c.user.email = :email AND c.product.id" +
            " = :productId")
    Optional<Cart> findByEmailAndProductId(
            String email,
            UUID productId
    );

    boolean existsById(@NonNull UUID cartId);

    void deleteByUserId(UUID userId);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Cart c WHERE c.user.email = :email")
    void deleteByEmail(String email);

}
