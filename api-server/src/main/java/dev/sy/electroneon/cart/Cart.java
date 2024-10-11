package dev.sy.electroneon.cart;

import dev.sy.electroneon.product.Product;
import dev.sy.electroneon.user.User;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Objects;
import java.util.UUID;

/**
 * Entity class to represent each cart entry (or line) within a shopping cart.
 * <p>
 * Each cart entry contains information about the saved product, the saved
 * product's quantity, etc.
 */
@Entity
@Table(
        name = "carts"
)
public class Cart {

    @Id
    @Column(columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @ManyToOne
    @JoinColumn(
            name = "FK_user_id",
            nullable = false
    )
    private User user;

    @ManyToOne
    @JoinColumn(
            name = "FK_product_id",
            nullable = false
    )
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    public Cart() {
    }

    public Cart(
            UUID id,
            User user,
            Product product,
            Integer quantity
    ) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.quantity = quantity;
    }

    public Cart(
            User user,
            Product product,
            Integer quantity
    ) {
        this(UUID.randomUUID(), user, product, quantity);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
        Cart cart = (Cart) o;
        return Objects.equals(id, cart.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", FK_user_id=" + user.getId() +
                ", FK_product_id=" + product.getId() +
                ", quantity=" + quantity +
                '}';
    }
}
