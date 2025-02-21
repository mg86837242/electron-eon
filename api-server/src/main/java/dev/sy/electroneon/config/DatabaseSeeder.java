package dev.sy.electroneon.config;

import dev.sy.electroneon.cart.Cart;
import dev.sy.electroneon.cart.CartService;
import dev.sy.electroneon.order.Order;
import dev.sy.electroneon.order.OrderService;
import dev.sy.electroneon.orderproduct.OrderProduct;
import dev.sy.electroneon.orderproduct.OrderProductService;
import dev.sy.electroneon.product.Category;
import dev.sy.electroneon.product.Product;
import dev.sy.electroneon.product.ProductService;
import dev.sy.electroneon.user.Role;
import dev.sy.electroneon.user.User;
import dev.sy.electroneon.user.UserService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Configuration class for loading test data into the application, i.e.,
 * database seeding.
 */
@Configuration
public class DatabaseSeeder {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseSeeder.class);
    private final PasswordEncoder passwordEncoder;
    private final UserService us;
    private final ProductService ps;
    private final CartService cs;
    private final OrderService os;
    private final OrderProductService ops;

    public DatabaseSeeder(
            UserService us,
            ProductService ps,
            CartService cs,
            OrderService os,
            OrderProductService ops,
            PasswordEncoder passwordEncoder
    ) {
        this.us = us;
        this.ps = ps;
        this.cs = cs;
        this.os = os;
        this.ops = ops;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    private void seedDatabase() {
        List<User> users = seedUsers();
        List<Product> products = seedProducts();
        seedCarts(users, products);
        List<Order> orders = seedOrders(users);
        seedOrderProducts(orders, products);
    }

    private List<User> seedUsers() {
        User user1 = new User(
                UUID.fromString("1de64dad-aae0-4004-b4bc-f28c1f46589a"),
                "john@server.com",
                passwordEncoder.encode("abcd1234"),
                "john",
                "doe",
                Role.ADMIN
        );
        User user2 = new User(
                UUID.fromString("1c6a97c1-da90-464a-95be-700549add2ea"),
                "jane@server.com",
                passwordEncoder.encode("abcd1234"),
                "jane",
                "doe",
                Role.CUSTOMER
        );
        us.addUserForSeeding(user1);
        us.addUserForSeeding(user2);

        return List.of(user1, user2);
    }

    private List<Product> seedProducts() {
        Product product1 = new Product(
                UUID.fromString("2103757c-2e2d-4dfb-befd-987f4fcce43a"),
                "Dell Inspiron 3511 Laptop",
                "Lorem ipsum dolor sit amet.",
                706.5,
                Category.LAPTOP
        );
        Product product2 = new Product(
                UUID.fromString("22cd8441-4277-4d7c-bafa-2cd8af3d5cc4"),
                "Lenovo IdeaPad 1 Student Laptop",
                "Lorem ipsum dolor sit amet.",
                528.5,
                Category.LAPTOP
        );
        Product product3 = new Product(
                UUID.fromString("23d4364f-cd62-4e80-a017-3c16c43a7908"),
                "Samsung Galaxy A04e",
                "Lorem ipsum dolor sit amet.",
                121.9,
                Category.SMARTPHONE
        );
        Product product4 = new Product(
                UUID.fromString("24f8ae7f-de04-4e4e-aa53-2cea8974a861"),
                "Motorola Moto G 5G (2023)",
                "Lorem ipsum dolor sit amet.",
                203.8,
                Category.SMARTPHONE
        );
        Product product5 = new Product(
                UUID.fromString("252c3087-57f7-4332-9b22-5d858b302783"),
                "LG Electronics 24LM530S-PU 24-Inch HD",
                "Lorem ipsum dolor sit amet.",
                217.5,
                Category.COMPUTER_ACCESSORY
        );
        Product product6 = new Product(
                UUID.fromString("2621f16a-c94f-4225-b817-e54c4fb9d2b9"),
                "Samsung 32-Inch Class QLED Q60A Series - 4K UHD",
                "Lorem ipsum dolor sit amet.",
                611.3,
                Category.COMPUTER_ACCESSORY
        );
        ps.addProductForSeeding(product1);
        ps.addProductForSeeding(product2);
        ps.addProductForSeeding(product3);
        ps.addProductForSeeding(product4);
        ps.addProductForSeeding(product5);
        ps.addProductForSeeding(product6);

        return List.of(
                product1,
                product2,
                product3,
                product4,
                product5,
                product6
        );
    }

    private void seedCarts(List<User> users, List<Product> products) {
        Cart cart1 = new Cart(
                UUID.fromString(
                        "31b7c3c0-83a4-42bb-97cc-de837b6015a7"),
                users.getFirst(),
                products.get(2),
                1
        );
        Cart cart2 = new Cart(
                UUID.fromString(
                        "3283e51c-fe1a-4180-a03d-911181435336"),
                users.getFirst(),
                products.get(3),
                2
        );
        Cart cart3 = new Cart(
                UUID.fromString(
                        "33200d98-f9d3-48d1-b3e8-f87ee833f71f"),
                users.get(1),
                products.get(2),
                3
        );
        Cart cart4 = new Cart(
                UUID.fromString(
                        "3420cfa9-854d-4e53-b076-6d7493314c20"),
                users.get(1),
                products.get(3),
                4
        );
        cs.addCartForSeeding(cart1);
        cs.addCartForSeeding(cart2);
        cs.addCartForSeeding(cart3);
        cs.addCartForSeeding(cart4);
    }

    private List<Order> seedOrders(List<User> users) {
        Order order1 = new Order(
                UUID.fromString(
                        "41c0c8c8-6de5-4836-a934-bdbb5ba6bfd1"),
                users.getFirst(),
                "123 ABC Street",
                "DEF",
                LocalDateTime.now()
        );
        Order order2 = new Order(
                UUID.fromString(
                        "42ecab4a-344e-49ba-94f7-ac5f32fe1b82"),
                users.get(1),
                "123 ABC Street",
                "DEF",
                LocalDateTime.now()
        );
        os.addOrderForSeeding(order1);
        os.addOrderForSeeding(order2);

        return List.of(order1, order2);
    }

    private void seedOrderProducts(List<Order> orders, List<Product> products) {
        OrderProduct orderProduct1 = new OrderProduct(
                UUID.fromString(
                        "51d5f27a-77c9-4fd0-993d-2e2c2c529a0b"),
                orders.getFirst(),
                products.getFirst(),
                1
        );
        OrderProduct orderProduct2 = new OrderProduct(
                UUID.fromString(
                        "521421eb-4ca1-47eb-bb18-02188e0c4b6d"),
                orders.getFirst(),
                products.get((1)),
                2
        );
        OrderProduct orderProduct3 = new OrderProduct(
                UUID.fromString(
                        "5331004b-ee52-4458-8fe9-d4bada3fd2be"),
                orders.get(1),
                products.getFirst(),
                3
        );
        OrderProduct orderProduct4 = new OrderProduct(
                UUID.fromString(
                        "54360ebf-2a68-4dcc-b9d2-c12c38e1be21"),
                orders.get(1),
                products.get(1),
                4
        );
        ops.addOrderProductForSeeding(orderProduct1);
        ops.addOrderProductForSeeding(orderProduct2);
        ops.addOrderProductForSeeding(orderProduct3);
        ops.addOrderProductForSeeding(orderProduct4);
    }

    private void seedingStart() {
        LOG.info(">> Database seeding starts...");
    }

    private void seedingFinished() {
        LOG.info(">> Database seeding finished!");
    }

    @Bean
    public ApplicationRunner runner() {
        return args -> {
            seedingStart();
            seedDatabase();
            seedingFinished();
        };
    }
}
