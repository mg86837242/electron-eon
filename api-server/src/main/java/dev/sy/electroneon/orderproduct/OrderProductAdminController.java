package dev.sy.electroneon.orderproduct;

import dev.sy.electroneon.orderproduct.dto.OrderProductDTO;
import dev.sy.electroneon.orderproduct.request.OrderProductCreationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/admin/order-products")
public class OrderProductAdminController {

    private final OrderProductService orderProductService;

    public OrderProductAdminController(OrderProductService orderProductService) {
        this.orderProductService = orderProductService;
    }

    @PostMapping
    public ResponseEntity<OrderProductDTO> addOrderProduct(
            @RequestBody OrderProductCreationRequest request
    ) {
        OrderProductDTO savedOrderProduct = orderProductService.addOrderProduct(
                request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
                "/{id}").buildAndExpand(savedOrderProduct.id()).toUri();

        return ResponseEntity.created(location).body(savedOrderProduct);
    }

    // For admin page to manage individual order
    @PatchMapping("/{id}/update-quantity")
    public ResponseEntity<OrderProductDTO> updateProdQtyInOrderById(
            @PathVariable UUID id,
            @RequestParam("quantity") int quantity
    ) {
        orderProductService.updateProductQtyInOrderById(id, quantity);

        return ResponseEntity.ok().build();
    }

    // For admin page to manage individual order
    @PatchMapping("/{id}/increment-quantity")
    public ResponseEntity<OrderProductDTO> incrementProdQtyInOrderById(@PathVariable UUID id) {
        orderProductService.incrementProductQtyInOrderById(id);

        return ResponseEntity.ok().build();
    }

    // For admin page to manage individual order
    @PatchMapping("/{id}/decrement-quantity")
    public ResponseEntity<OrderProductDTO> decrementProdQtyInOrderById(@PathVariable UUID id) {
        orderProductService.decrementProductQtyInOrderById(id);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public void deleteOrderProductById(@PathVariable UUID id) {
        orderProductService.deleteOrderProductById(id);
    }

    /**
     * This endpoint represents the functionality of emptying all the order
     * entries from a specific order, leaving the address etc. intact - this
     * functionality is not meant for customers.
     * <p>
     * Only users with administrative privileges are allowed to access this
     * endpoint.
     */
    @DeleteMapping("by-order-id/{orderId}")
    public void deleteOrderProductsByOrderId(@PathVariable UUID orderId) {
        orderProductService.deleteOrderProductsByOrderId(orderId);
    }
}
