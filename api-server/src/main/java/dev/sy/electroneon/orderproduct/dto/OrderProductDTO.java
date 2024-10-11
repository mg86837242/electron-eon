package dev.sy.electroneon.orderproduct.dto;

import dev.sy.electroneon.order.dto.OrderDTO;
import dev.sy.electroneon.product.dto.ProductDTO;

import java.util.UUID;

public record OrderProductDTO(
        UUID id,
        OrderDTO order,
        ProductDTO product,
        Integer quantity
) {
}
