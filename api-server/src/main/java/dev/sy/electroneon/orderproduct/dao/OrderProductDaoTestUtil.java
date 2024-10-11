package dev.sy.electroneon.orderproduct.dao;

import dev.sy.electroneon.order.Order;
import dev.sy.electroneon.order.dao.OrderDao;
import dev.sy.electroneon.orderproduct.OrderProduct;
import dev.sy.electroneon.product.Product;
import dev.sy.electroneon.product.dao.ProductDao;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class OrderProductDaoTestUtil {

    private static final Logger LOG = LoggerFactory.getLogger(
            OrderProductDaoTestUtil.class);
    private final OrderProductDao opd;
    private final OrderDao od;
    private final ProductDao pd;

    public OrderProductDaoTestUtil(
            OrderProductDao opd,
            OrderDao od,
            ProductDao pd
    ) {
        this.opd = opd;
        this.od = od;
        this.pd = pd;
    }

    public void selectOrderProductById() {
        Optional<OrderProduct> optionalOrderProduct = opd.selectOrderProductById(
                UUID.fromString(
                        "31b7c3c0-83a4-42bb-97cc-de837b6015a7"
                )
        );

        LOG.info(">> selectOrderProductById() result:");
        LOG.info(String.valueOf(optionalOrderProduct));
    }

    public void selectOrderProductsByOrderId() {
        UUID orderId1 = UUID.fromString(
                "41c0c8c8-6de5-4836-a934-bdbb5ba6bfd1"
        );
        UUID orderId2 = UUID.fromString(
                "42ecab4a-344e-49ba-94f7-ac5f32fe1b82"
        );

        List<OrderProduct> orderProducts1 = opd.selectOrderProductsByOrderId(
                orderId1);

        LOG.info(
                ">> selectOrderProductByOrderId() result for order {}:",
                orderId1
        );
        if (orderProducts1.isEmpty()) {
            LOG.info("None");
        } else {
            orderProducts1.forEach(orderProduct -> LOG.info(String.valueOf(
                    orderProduct)));
        }

        List<OrderProduct> orderProducts2 = opd.selectOrderProductsByOrderId(
                orderId2);

        LOG.info(
                ">> selectOrderProductByOrderId() result for order {}:",
                orderId2
        );
        if (orderProducts2.isEmpty()) {
            LOG.info("None");
        } else {
            orderProducts2.forEach(orderProduct -> LOG.info(String.valueOf(
                    orderProduct)));
        }
    }

    public void insertOrderProduct() {
        Order order1 = od
                .selectOrderById(
                        UUID.fromString(
                                "41c0c8c8-6de5-4836-a934-bdbb5ba6bfd1"
                        )
                )
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        Order order2 = od
                .selectOrderById(
                        UUID.fromString(
                                "42ecab4a-344e-49ba-94f7-ac5f32fe1b82"
                        )
                )
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        Product product1 = pd
                .selectProductById(
                        UUID.fromString(
                                "2103757c-2e2d-4dfb-befd-987f4fcce43a"
                        )
                )
                .orElseThrow(() -> new EntityNotFoundException(
                        "Product not found"));
        Product product2 = pd
                .selectProductById(
                        UUID.fromString(
                                "22cd8441-4277-4d7c-bafa-2cd8af3d5cc4"
                        )
                )
                .orElseThrow(() -> new EntityNotFoundException(
                        "Product not found"));
        OrderProduct orderProduct1 = new OrderProduct(
                UUID.fromString(
                        "31b7c3c0-83a4-42bb-97cc-de837b6015a7"
                ),
                order1,
                product1,
                1
        );
        OrderProduct orderProduct2 = new OrderProduct(
                UUID.fromString(
                        "3283e51c-fe1a-4180-a03d-911181435336"
                ),
                order1,
                product2,
                2
        );
        OrderProduct orderProduct3 = new OrderProduct(
                UUID.fromString(
                        "33200d98-f9d3-48d1-b3e8-f87ee833f71f"
                ),
                order2,
                product1,
                3
        );
        OrderProduct orderProduct4 = new OrderProduct(
                UUID.fromString(
                        "3420cfa9-854d-4e53-b076-6d7493314c20"
                ),
                order2,
                product2,
                4
        );

        opd.insertOrderProduct(orderProduct1);
        opd.insertOrderProduct(orderProduct2);
        opd.insertOrderProduct(orderProduct3);
        opd.insertOrderProduct(orderProduct4);
    }

    public void updateOrderProducts() {
        Order order2 = od
                .selectOrderById(
                        UUID.fromString(
                                "42ecab4a-344e-49ba-94f7-ac5f32fe1b82"
                        )
                )
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        Product product2 = pd
                .selectProductById(
                        UUID.fromString(
                                "22cd8441-4277-4d7c-bafa-2cd8af3d5cc4"
                        )
                )
                .orElseThrow(() -> new EntityNotFoundException(
                        "Product not found"));
        OrderProduct update = new OrderProduct(
                UUID.fromString(
                        "3420cfa9-854d-4e53-b076-6d7493314c20"
                ),
                order2,
                product2,
                5
        );

        opd.updateOrderProduct(update);
    }

    public void deleteOrderProductById() {
        opd.deleteOrderProductById(
                UUID.fromString(
                        "3420cfa9-854d-4e53-b076-6d7493314c20"
                )
        );
    }

    public void deleteOrderProductsByOrderId() {
        opd.deleteOrderProductsByOrderId(
                UUID.fromString(
                        "42ecab4a-344e-49ba-94f7-ac5f32fe1b82"
                )
        );
    }
}
