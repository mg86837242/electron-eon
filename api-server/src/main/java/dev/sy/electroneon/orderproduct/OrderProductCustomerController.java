package dev.sy.electroneon.orderproduct;

import dev.sy.electroneon.orderproduct.dto.OrderProductDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customer/order-products")
public class OrderProductCustomerController {

    private final OrderProductService orderProductService;

    public OrderProductCustomerController(OrderProductService orderProductService) {
        this.orderProductService = orderProductService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderProductDTO> getOrderProductById(@PathVariable UUID id) {
        OrderProductDTO orderProduct =
                orderProductService.getOrderProductById(id);

        return ResponseEntity.ok(orderProduct);
    }

    @GetMapping("by-order-id/{orderId}")
    public List<OrderProductDTO> getOrderProductsByOrderId(
            @PathVariable UUID orderId
    ) {
        return orderProductService.getOrderProductsByOrderId(orderId);
    }
}
