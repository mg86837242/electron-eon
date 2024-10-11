package dev.sy.electroneon.order.dto;

import dev.sy.electroneon.product.dto.ProductDTO;

import java.util.UUID;

public record NestedOrderProductWithNoOrderDTO(
        UUID id,
        ProductDTO product,
        Integer quantity
) {
}
