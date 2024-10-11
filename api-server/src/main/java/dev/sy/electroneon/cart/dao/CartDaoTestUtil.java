package dev.sy.electroneon.cart.dao;

import dev.sy.electroneon.cart.Cart;
import dev.sy.electroneon.product.Product;
import dev.sy.electroneon.product.dao.ProductDao;
import dev.sy.electroneon.user.User;
import dev.sy.electroneon.user.dao.UserDao;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CartDaoTestUtil {

    private static final Logger LOG = LoggerFactory.getLogger(CartDaoTestUtil.class);
    private final CartDao cd;
    private final UserDao ud;
    private final ProductDao pd;

    public CartDaoTestUtil(
            CartDao cd,
            UserDao ud,
            ProductDao pd
    ) {
        this.cd = cd;
        this.ud = ud;
        this.pd = pd;
    }

    public void selectCartById() {
        Optional<Cart> optionalCart = cd.selectCartById(
                UUID.fromString(
                        "51d5f27a-77c9-4fd0-993d-2e2c2c529a0b"
                )
        );

        LOG.info(">> selectCartById() result:");
        LOG.info(String.valueOf(optionalCart));
    }

    public void selectCartsByUserId() {
        UUID userId1 = UUID.fromString(
                "1de64dad-aae0-4004-b4bc-f28c1f46589a"
        );
        UUID userId2 = UUID.fromString(
                "1c6a97c1-da90-464a-95be-700549add2ea"
        );

        List<Cart> carts1 = cd.selectCartsByUserId(userId1);
        User user = ud
                .selectUserById(userId1)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        LOG.info(
                ">> selectCartByUserId() result for {} {}:",
                user.getFirstName(),
                user.getLastName()
        );
        if (carts1.isEmpty()) {
            LOG.info("None");
        } else {
            carts1.forEach(cart -> LOG.info(String.valueOf(cart)));
        }

        List<Cart> carts2 = cd.selectCartsByUserId(userId2);
        User user2 = ud
                .selectUserById(userId2)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        LOG.info(
                ">> selectCartByUserId() result for {} {}:",
                user2.getFirstName(),
                user2.getLastName()
        );
        if (carts2.isEmpty()) {
            LOG.info("None");
        } else {
            carts2.forEach(cart -> LOG.info(String.valueOf(cart)));
        }
    }

    public void insertCart() {
        // NB The `selectUserById()` method returns a detached `Optional<User>`
        // object because the `EntityManger` is closed at the end of this
        // method. When passing the detached `User` object as an argument in the
        // constructor of the `Cart` entity (`new Cart(...)`), the `User` object
        // itself is not managed by the persistence context. Instead, only the
        // `UUID` representing the `User` entity is stored within the `Cart`
        // entity. This behavior is base on the behavior specified by the JPA
        // specification.
        //
        // NB2 Even if the `EntityManager` field within the Hibernate DAS is
        // annotated by `@PersistenceContext`, i.e., container-managed, the
        // `Optional<User>` object returned by the `selectUserById()` method
        // is still detached.
        User user1 = ud
                .selectUserById(
                        UUID.fromString(
                                "1de64dad-aae0-4004-b4bc-f28c1f46589a"
                        )
                )
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        User user2 = ud
                .selectUserById(
                        UUID.fromString(
                                "1c6a97c1-da90-464a-95be-700549add2ea"
                        )
                )
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Product product3 = pd
                .selectProductById(
                        UUID.fromString(
                                "23d4364f-cd62-4e80-a017-3c16c43a7908"
                        )
                )
                .orElseThrow(() -> new EntityNotFoundException(
                        "Product not found"));
        Product product4 = pd
                .selectProductById(
                        UUID.fromString(
                                "24f8ae7f-de04-4e4e-aa53-2cea8974a861"
                        )
                )
                .orElseThrow(() -> new EntityNotFoundException(
                        "Product not found"));
        Cart cart1 = new Cart(
                UUID.fromString(
                        "51d5f27a-77c9-4fd0-993d-2e2c2c529a0b"
                ),
                user1,
                product3,
                1
        );
        Cart cart2 = new Cart(
                UUID.fromString(
                        "521421eb-4ca1-47eb-bb18-02188e0c4b6d"
                ),
                user1,
                product4,
                2
        );
        Cart cart3 = new Cart(
                UUID.fromString(
                        "5331004b-ee52-4458-8fe9-d4bada3fd2be"
                ),
                user2,
                product3,
                3
        );
        Cart cart4 = new Cart(
                UUID.fromString(
                        "54360ebf-2a68-4dcc-b9d2-c12c38e1be21"
                ),
                user2,
                product4,
                4
        );

        cd.insertCart(cart1);
        cd.insertCart(cart2);
        cd.insertCart(cart3);
        cd.insertCart(cart4);
    }

    public void updateCart() {
        User user2 = ud
                .selectUserById(
                        UUID.fromString(
                                "1c6a97c1-da90-464a-95be-700549add2ea"
                        )
                )
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Product product4 = pd
                .selectProductById(
                        UUID.fromString(
                                "24f8ae7f-de04-4e4e-aa53-2cea8974a861"
                        )
                )
                .orElseThrow(() -> new EntityNotFoundException(
                        "Product not found"));
        Cart update = new Cart(
                UUID.fromString(
                        "54360ebf-2a68-4dcc-b9d2-c12c38e1be21"
                ),
                user2,
                product4,
                5
        );

        cd.updateCart(update);
    }

    public void incrementCartQty() {
        UUID cartId = UUID.fromString(
                "54360ebf-2a68-4dcc-b9d2-c12c38e1be21"
        );

        cd.incrementCartQty(cartId);
    }

    public void decrementCartQty() {
        UUID cartId = UUID.fromString(
                "54360ebf-2a68-4dcc-b9d2-c12c38e1be21"
        );

        cd.decrementCartQty(cartId);
    }

    public void deleteCartById() {
        cd.deleteCartById(
                UUID.fromString(
                        "54360ebf-2a68-4dcc-b9d2-c12c38e1be21"
                )
        );
    }
}