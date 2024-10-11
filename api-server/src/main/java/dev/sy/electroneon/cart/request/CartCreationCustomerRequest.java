package dev.sy.electroneon.cart.request;

import java.util.UUID;

public record CartCreationCustomerRequest(
        UUID productId,
        Integer quantity
) {
}
