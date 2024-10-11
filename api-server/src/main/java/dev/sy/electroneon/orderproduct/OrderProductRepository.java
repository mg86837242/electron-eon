package dev.sy.electroneon.orderproduct;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.UUID;

public interface OrderProductRepository extends JpaRepository<OrderProduct, UUID> {

    List<OrderProduct> findByOrderId(UUID orderId);

    boolean existsById(@NonNull UUID orderProductId);

    boolean existsByOrderIdAndProductId(
            UUID orderId,
            UUID productId
    );

    void deleteByOrderId(UUID orderId);

}
