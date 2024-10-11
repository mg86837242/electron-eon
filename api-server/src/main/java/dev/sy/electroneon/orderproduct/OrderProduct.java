package dev.sy.electroneon.orderproduct;

import dev.sy.electroneon.order.Order;
import dev.sy.electroneon.product.Product;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Objects;
import java.util.UUID;

/**
 * Entity class to represent each order entry (or line) within an order.
 * <p>
 * Each order entry contains information about the ordered product, the
 * ordered product's quantity, etc.
 */
@Entity
@Table(
        name = "orders_products_jt"
)
public class OrderProduct {

    @Id
    @Column(columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @ManyToOne
    @JoinColumn(
            name = "FK_order_id",
            nullable = false
    )
    private Order order;

    @ManyToOne
    @JoinColumn(
            name = "FK_product_id",
            nullable = false
    )
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    public OrderProduct() {
    }

    public OrderProduct(
            UUID id,
            Order order,
            Product product,
            Integer quantity
    ) {
        this.id = id;
        this.order = order;
        this.product = product;
        this.quantity = quantity;
    }

    public OrderProduct(
            Order order,
            Product product,
            Integer quantity
    ) {
        this(UUID.randomUUID(), order, product, quantity);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderProduct that = (OrderProduct) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OrderProduct{" +
                "id=" + id +
                ", FK_order_id=" + order.getId() +
                ", FK_product_id=" + product.getId() +
                ", quantity=" + quantity +
                '}';
    }
}
