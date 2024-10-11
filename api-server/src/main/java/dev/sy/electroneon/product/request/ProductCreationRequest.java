package dev.sy.electroneon.product.request;

import dev.sy.electroneon.product.Category;

public record ProductCreationRequest(
        String name,
        String description,
        Double price,
        Category category
) {
}
