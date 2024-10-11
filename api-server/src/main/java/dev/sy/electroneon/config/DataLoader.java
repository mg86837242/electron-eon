package dev.sy.electroneon.config;

import dev.sy.electroneon.cart.dao.CartDaoTestUtil;
import dev.sy.electroneon.order.dao.OrderDaoTestUtil;
import dev.sy.electroneon.orderproduct.dao.OrderProductDaoTestUtil;
import dev.sy.electroneon.product.dao.ProductDaoTestUtil;
import dev.sy.electroneon.user.dao.UserDaoTestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for loading test data into the application.
 * This class defines methods to run test scripts for Data Access Services
 * (DAS).
 * This class and its variation can be later used for database seeding.
 */
@Configuration
public class DataLoader {

    private static final Logger LOG = LoggerFactory.getLogger(DataLoader.class);
    private final UserDaoTestUtil udtu;
    private final ProductDaoTestUtil pdtu;
    private final CartDaoTestUtil cdtu;
    private final OrderDaoTestUtil odtu;
    private final OrderProductDaoTestUtil opdtu;

    public DataLoader(
            UserDaoTestUtil udtu,
            ProductDaoTestUtil pdtu,
            CartDaoTestUtil cdtu,
            OrderDaoTestUtil odtu,
            OrderProductDaoTestUtil opdtu
    ) {
        this.udtu = udtu;
        this.pdtu = pdtu;
        this.cdtu = cdtu;
        this.odtu = odtu;
        this.opdtu = opdtu;
    }

    @SuppressWarnings("unused")
    private void runTestScriptsForDAS() {
        udtu.insertUser();
        udtu.selectAllUsers();

        pdtu.insertProduct();
        pdtu.selectAllProducts();

        cdtu.insertCart();
        cdtu.incrementCartQty();
        cdtu.selectCartsByUserId();
        cdtu.decrementCartQty();
        cdtu.selectCartsByUserId();

        odtu.insertOrder();
        odtu.updateOrder();
        odtu.selectAllOrders();

        opdtu.selectOrderProductById();
        opdtu.insertOrderProduct();
        opdtu.selectOrderProductsByOrderId();
        opdtu.updateOrderProducts();
        opdtu.selectOrderProductsByOrderId();
    }

    private void seedDatabase() {
        udtu.insertUser();
        pdtu.insertProduct();
        cdtu.insertCart();
        odtu.insertOrder();
        opdtu.insertOrderProduct();
    }

    private void helloDataLoader() {
        LOG.info(">> Data loader initiated...");
    }

    @Bean
    public ApplicationRunner runner() {
        return args -> {
            helloDataLoader();
            seedDatabase();
        };
    }
}
