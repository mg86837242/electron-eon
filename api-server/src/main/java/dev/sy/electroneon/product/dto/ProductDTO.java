package dev.sy.electroneon.product.dto;

import dev.sy.electroneon.product.Category;

import java.util.UUID;

public record ProductDTO(
        UUID id,
        String name,
        String description,
        Double price,
        Category category
) {
}
