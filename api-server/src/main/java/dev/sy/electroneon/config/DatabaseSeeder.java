package dev.sy.electroneon.config;

import dev.sy.electroneon.cart.CartService;
import dev.sy.electroneon.order.OrderService;
import dev.sy.electroneon.orderproduct.OrderProductService;
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
    private UserService us;
    private ProductService ps;
    private CartService cs;
    private OrderService os;
    private OrderProductService ops;

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
        us.addUser(user1);
        us.addUser(user2);
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
