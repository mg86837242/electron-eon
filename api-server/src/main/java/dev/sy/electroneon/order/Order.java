package dev.sy.electroneon.order;

import dev.sy.electroneon.user.User;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(
        name = "orders"
)
public class Order {

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

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String city;

    // @see: https://www.baeldung.com/hibernate-date-time
    private LocalDateTime createdAt;

    public Order() {
    }

    public Order(
            UUID id,
            User user,
            String street,
            String city,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.user = user;
        this.street = street;
        this.city = city;
        this.createdAt = createdAt;
    }

    public Order(
            User user,
            String street,
            String city,
            LocalDateTime createdAt
    ) {
        this(UUID.randomUUID(), user, street, city, createdAt);
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", FK_user_id=" + user.getId() +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
