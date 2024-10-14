package dev.sy.electroneon.config;

import dev.sy.electroneon.cart.CartService;
import dev.sy.electroneon.order.OrderService;
import dev.sy.electroneon.orderproduct.OrderProductService;
import dev.sy.electroneon.product.Category;
import dev.sy.electroneon.product.Product;
import dev.sy.electroneon.product.ProductService;
import dev.sy.electroneon.user.Role;
import dev.sy.electroneon.user.User;
import dev.sy.electroneon.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    private void seedDatabase() {
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
    }

    private void seederStart() {
        LOG.info(">> Database seeder starts...");
    }

    private void seederFinish() {
        LOG.info(">> Database seeder finished!");
    }

    @Bean
    public ApplicationRunner runner() {
        return args -> {
            seederStart();
            seedDatabase();
            seederFinish();
        };
    }
}
