package dev.sy.electroneon.order.dao;

import dev.sy.electroneon.order.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class OrderHibernateDataAccessService implements OrderDao {

    @PersistenceContext
    private EntityManager em;

    public OrderHibernateDataAccessService() {
    }

    @Override
    public List<Order> selectAllOrders() {
        String jpql = "SELECT o from Order o";

        return em
                .createQuery(jpql, Order.class)
                .getResultList();
    }

    @Override
    public Optional<Order> selectOrderById(UUID orderId) {
        return Optional.ofNullable(
                em.find(Order.class, orderId)
        );
    }

    @Override
    public boolean existsOrderById(UUID orderId) {
        return selectOrderById(orderId).isPresent();
    }

    @Override
    @Transactional
    public void insertOrder(Order order) {
        em.persist(order);
    }

    @Override
    @Transactional
    public void updateOrder(Order update) {
        // Destructure the `update` object
        UUID orderId = update.getId();
        String street = update.getStreet();
        String city = update.getCity();

        Optional<Order> optionalOrder = Optional.ofNullable(
                em.find(Order.class, orderId)
        );
        optionalOrder.ifPresent(order -> {
            order.setStreet(street);
            order.setCity(city);
        });
    }

    @Override
    @Transactional
    public void deleteOrderById(UUID orderId) {
        Optional<Order> optionalOrder = Optional.ofNullable(
                em.find(Order.class, orderId)
        );
        optionalOrder.ifPresent(order -> em.remove(order));
    }
}
