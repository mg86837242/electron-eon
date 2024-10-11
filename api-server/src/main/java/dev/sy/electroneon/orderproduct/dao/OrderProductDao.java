package dev.sy.electroneon.orderproduct.dao;

import dev.sy.electroneon.orderproduct.OrderProduct;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderProductDao {

    Optional<OrderProduct> selectOrderProductById(UUID orderProductId);

    List<OrderProduct> selectOrderProductsByOrderId(UUID orderId);

    boolean existsOrderProductById(UUID orderProductId);

    void insertOrderProduct(OrderProduct orderProduct);

    void updateOrderProduct(OrderProduct update);

    void deleteOrderProductById(UUID orderProductId);

    void deleteOrderProductsByOrderId(UUID orderId);
}
