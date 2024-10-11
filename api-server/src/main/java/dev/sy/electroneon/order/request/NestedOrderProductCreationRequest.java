package dev.sy.electroneon.order.request;

import java.util.UUID;

public record NestedOrderProductCreationRequest(
        UUID productId,
        Integer quantity
) {
}
