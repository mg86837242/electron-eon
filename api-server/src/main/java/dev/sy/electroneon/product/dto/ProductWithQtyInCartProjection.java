package dev.sy.electroneon.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.sy.electroneon.product.Category;

import java.util.UUID;

public interface ProductWithQtyInCartProjection {

    UUID getId();

    String getName();

    String getDescription();

    Double getPrice();

    Category getCategory();

    @JsonIgnore
    Integer getQuantity(); // exclude this field from JSON output

    // `COALESCE` won't work in JPQL with interface-based projections, instead,
    // use this getter method
    default Integer getCartQuantity() {
        return getQuantity() != null ? getQuantity() : 0;
    }

}

// References:
// -- bug: "No converter found capable of converting from type
// [org.springframework.data.jpa.repository.query.AbstractJpaQuery$TupleConverter$TupleBackedMap]"
// -- further explanations for projections: https://medium.com/@AlexanderObregon/optimizing-queries-with-query-annotation-in-spring-data-jpa-fe213c8a60a
//    => https://stackoverflow.com/questions/52591535/spring-jpa-no-converter-found-capable-of-converting-from-type
// -- bug: "jpa projection all null" => https://stackoverflow.com/questions/69843383/spring-boot-interface-projection-only-returns-null