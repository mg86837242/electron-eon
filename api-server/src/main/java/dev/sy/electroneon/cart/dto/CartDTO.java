package dev.sy.electroneon.cart.dto;

import dev.sy.electroneon.product.dto.ProductDTO;
import dev.sy.electroneon.user.dto.UserDTO;

import java.util.UUID;

public record CartDTO(
        UUID id,
        UserDTO user,
        ProductDTO product,
        Integer quantity
) {
}
