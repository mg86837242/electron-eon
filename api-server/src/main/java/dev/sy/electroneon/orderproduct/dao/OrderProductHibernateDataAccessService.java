package dev.sy.electroneon.orderproduct.dao;

import dev.sy.electroneon.orderproduct.OrderProduct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class OrderProductHibernateDataAccessService implements OrderProductDao {

    @PersistenceContext
    private EntityManager em;

    public OrderProductHibernateDataAccessService() {
    }

    @Override
    public Optional<OrderProduct> selectOrderProductById(UUID orderProductId) {
        return Optional.ofNullable(
                em.find(OrderProduct.class, orderProductId)
        );
    }

    @Override
    public List<OrderProduct> selectOrderProductsByOrderId(UUID orderId) {
        String jpql = "SELECT op from OrderProduct op WHERE op.order.id = :orderId";

        return em
                .createQuery(jpql, OrderProduct.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    @Override
    public boolean existsOrderProductById(UUID orderProductId) {
        return selectOrderProductById(orderProductId).isPresent();
    }

    @Override
    @Transactional
    public void insertOrderProduct(OrderProduct orderProduct) {
        em.persist(orderProduct);
    }

    @Override
    @Transactional
    public void updateOrderProduct(OrderProduct update) {
        // Destructure the `update` object
        UUID orderProductId = update.getId();
        Integer quantity = update.getQuantity();

        Optional<OrderProduct> optionalOrderProduct = Optional.ofNullable(
                em.find(OrderProduct.class, orderProductId)
        );
        optionalOrderProduct.ifPresent(orderProduct -> {
            orderProduct.setQuantity(quantity);
        });
    }

    @Override
    @Transactional
    public void deleteOrderProductById(UUID orderProductId) {
        Optional<OrderProduct> optionalOrderProduct = Optional.ofNullable(
                em.find(OrderProduct.class, orderProductId)
        );
        optionalOrderProduct.ifPresent(orderProduct -> em.remove(orderProduct));
    }

    @Override
    @Transactional
    public void deleteOrderProductsByOrderId(UUID orderId) {
        String jpql = "DELETE FROM OrderProduct op WHERE op.order.id = :orderId";

        em.createQuery(jpql)
                .setParameter("orderId", orderId)
                .executeUpdate();
    }
}
