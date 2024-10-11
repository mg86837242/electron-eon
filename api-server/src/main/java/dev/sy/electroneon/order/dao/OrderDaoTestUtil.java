package dev.sy.electroneon.order.dao;

import dev.sy.electroneon.order.Order;
import dev.sy.electroneon.user.User;
import dev.sy.electroneon.user.dao.UserDao;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class OrderDaoTestUtil {

    private static final Logger LOG = LoggerFactory.getLogger(OrderDaoTestUtil.class);
    private final OrderDao od;
    private final UserDao ud;

    public OrderDaoTestUtil(
            OrderDao od,
            UserDao ud
    ) {
        this.od = od;
        this.ud = ud;
    }

    public void selectAllOrders() {
        List<Order> orders = od.selectAllOrders();

        LOG.info(">> selectAllOrders() result:");
        if (orders.isEmpty()) {
            LOG.info("None");
        } else {
            orders.forEach(order -> LOG.info(String.valueOf(order)));
        }
    }

    public void selectOrderById() {
        Optional<Order> optionalOrder = od.selectOrderById(
                UUID.fromString(
                        "41c0c8c8-6de5-4836-a934-bdbb5ba6bfd1"
                )
        );

        LOG.info(">> selectOrderById() result:");
        LOG.info(String.valueOf(optionalOrder));
    }

    public void insertOrder() {
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
        Order order1 = new Order(
                UUID.fromString(
                        "41c0c8c8-6de5-4836-a934-bdbb5ba6bfd1"
                ),
                user1,
                "123 ABC Street",
                "DEF",
                LocalDateTime.now()
        );
        Order order2 = new Order(
                UUID.fromString(
                        "42ecab4a-344e-49ba-94f7-ac5f32fe1b82"
                ),
                user2,
                "123 ABC Street",
                "DEF",
                LocalDateTime.now()
        );

        od.insertOrder(order1);
        od.insertOrder(order2);
    }

    public void updateOrder() {
        User user2 = ud
                .selectUserById(
                        UUID.fromString(
                                "1c6a97c1-da90-464a-95be-700549add2ea"
                        )
                )
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Order update = new Order(
                UUID.fromString(
                        "42ecab4a-344e-49ba-94f7-ac5f32fe1b82"
                ),
                user2,
                "456 GHI Street",
                "JKL",
                // NB The value of the following argument doesn't matter since
                // the `setCreateAt()` won't be invoked in the `updateOrderById()`
                LocalDateTime.now()
        );

        od.updateOrder(update);
    }

    public void deleteCartById() {
        od.deleteOrderById(
                UUID.fromString(
                        "42ecab4a-344e-49ba-94f7-ac5f32fe1b82"
                )
        );
    }
}
