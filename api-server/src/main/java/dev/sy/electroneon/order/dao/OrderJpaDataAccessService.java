package dev.sy.electroneon.order.dao;

import dev.sy.electroneon.order.Order;
import dev.sy.electroneon.order.OrderRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Primary
public class OrderJpaDataAccessService implements OrderDao {

    private final OrderRepository orderRepository;

    public OrderJpaDataAccessService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> selectAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> selectOrderById(UUID orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public boolean existsOrderById(UUID orderId) {
        return orderRepository.existsById(orderId);
    }

    @Override
    public void insertOrder(Order order) {
        orderRepository.save(order);
    }

    @Override
    public void updateOrder(Order update) {
        orderRepository.save(update);
    }

    @Override
    public void deleteOrderById(UUID orderId) {
        orderRepository.deleteById(orderId);
    }
}
