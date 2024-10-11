package dev.sy.electroneon.cart;

import dev.sy.electroneon.cart.dto.CartDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customer/carts")
public class CartCustomerController {

    private final CartService cartService;

    public CartCustomerController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartDTO> getCartById(@PathVariable UUID id) {
        CartDTO cart = cartService.getCartById(id);

        return ResponseEntity.ok(cart);
    }

    @GetMapping("/current-user")
    public List<CartDTO> getCartsForCurrUser(Authentication authn) {
        return cartService.getCartsForCurrUser(authn);
    }

    // For catalogue page
    @PostMapping("/current-user/products/{productId}/upsert-quantity")
    public ResponseEntity<CartDTO> upsertProdQtyInCartForCurrUser(
            Authentication authn,
            @PathVariable("productId") UUID productId,
            @RequestParam("quantity") int quantity
    ) {
        cartService.upsertProdQtyInCartForCurrUser(authn, productId, quantity);

        return ResponseEntity.ok().build();
    }

    // For catalogue page
    @PatchMapping("/current-user/products/{productId}/increment-quantity")
    public ResponseEntity<CartDTO> incrementProdQtyInCartForCurrUser(
            Authentication authn,
            @PathVariable("productId") UUID productId
    ) {
        cartService.incrementProdQtyInCartForCurrUser(authn, productId);

        return ResponseEntity.ok().build();
    }

    // For catalogue page
    @PatchMapping("/current-user/products/{productId}/decrement-quantity")
    public ResponseEntity<CartDTO> decrementProdQtyInCartForCurrUser(
            Authentication authn,
            @PathVariable("productId") UUID productId
    ) {
        cartService.decrementProdQtyInCartForCurrUser(authn, productId);

        return ResponseEntity.ok().build();
    }

    // For individual product page
    @PatchMapping("/{id}/update-quantity")
    public ResponseEntity<CartDTO> updateProdQtyInCartById(
            @PathVariable UUID id,
            @RequestParam("quantity") int quantity
    ) {
        cartService.updateProdQtyInCartById(id, quantity);

        return ResponseEntity.ok().build();
    }

    // For dedicated cart page or admin page
    @PatchMapping("/{id}/increment-quantity")
    public ResponseEntity<CartDTO> incrementProdQtyInCartById(@PathVariable UUID id) {
        cartService.incrementProdQtyInCartById(id);

        return ResponseEntity.ok().build();
    }

    // For dedicated cart page or admin page
    @PatchMapping("/{id}/decrement-quantity")
    public ResponseEntity<CartDTO> decrementProdQtyInCartById(@PathVariable UUID id) {
        cartService.decrementProdQtyInCartById(id);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public void deleteCartById(@PathVariable UUID id) {
        cartService.deleteCartById(id);
    }

    @DeleteMapping("/current-user")
    public void deleteCartForCurrUser(Authentication authn) {
        cartService.deleteCartForCurrUser(authn);
    }
}
