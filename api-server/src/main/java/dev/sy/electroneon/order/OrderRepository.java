package dev.sy.electroneon.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findByUserId(UUID userId);

    @Query("SELECT o FROM Order o WHERE o.user.email = :email")
    List<Order> findByEmail(String email);

    boolean existsById(@NonNull UUID orderId);

    void deleteByUserId(UUID userId);

}
