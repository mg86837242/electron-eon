package dev.sy.electroneon.order;

import dev.sy.electroneon.order.dto.OrderDTO;
import dev.sy.electroneon.order.dto.OrderWithProductsDTO;
import dev.sy.electroneon.order.request.OrderCreationCustomerRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customer/orders")
public class OrderCustomerController {

    private final OrderService orderService;

    public OrderCustomerController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/current-user")
    public List<OrderDTO> getOrdersForCurrUser(Authentication authn) {
        return orderService.getOrdersForCurrUser(authn);
    }

    // TODO add logic to examine if the order belongs to the curr user
    @GetMapping("/{id}")
    public ResponseEntity<OrderWithProductsDTO> getOrderByIdForCurrUser(@PathVariable UUID id) {
        OrderWithProductsDTO order = orderService.getOrderById(id);

        return ResponseEntity.ok(order);
    }

    @PostMapping("/current-user")
    public ResponseEntity<OrderWithProductsDTO> addOrderForCurrUser(
            Authentication authn,
            @RequestBody OrderCreationCustomerRequest request
    ) {
        OrderWithProductsDTO savedOrder = orderService.addOrderForCurrUser(
                authn,
                request
        );
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
                "/{id}").buildAndExpand(savedOrder.id()).toUri();

        return ResponseEntity.created(location).body(savedOrder);
    }
}
