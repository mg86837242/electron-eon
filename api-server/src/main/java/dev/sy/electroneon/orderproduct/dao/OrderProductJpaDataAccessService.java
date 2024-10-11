package dev.sy.electroneon.orderproduct.dao;

import dev.sy.electroneon.orderproduct.OrderProduct;
import dev.sy.electroneon.orderproduct.OrderProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Primary
public class OrderProductJpaDataAccessService implements OrderProductDao {

    private final OrderProductRepository orderProductRepository;

    public OrderProductJpaDataAccessService(OrderProductRepository orderProductRepository) {
        this.orderProductRepository = orderProductRepository;
    }

    @Override
    public Optional<OrderProduct> selectOrderProductById(UUID orderProductId) {
        return orderProductRepository.findById(orderProductId);
    }

    @Override
    public List<OrderProduct> selectOrderProductsByOrderId(UUID orderId) {
        return orderProductRepository.findByOrderId(orderId);
    }

    @Override
    public boolean existsOrderProductById(UUID orderProductId) {
        return orderProductRepository.existsById(orderProductId);
    }

    @Override
    public void insertOrderProduct(OrderProduct orderProduct) {
        orderProductRepository.save(orderProduct);
    }

    @Override
    public void updateOrderProduct(OrderProduct update) {
        orderProductRepository.save(update);
    }

    @Override
    public void deleteOrderProductById(UUID orderProductId) {
        orderProductRepository.deleteById(orderProductId);
    }

    @Override
    @Transactional
    public void deleteOrderProductsByOrderId(UUID orderId) {
        orderProductRepository.deleteByOrderId(orderId);
    }
}
