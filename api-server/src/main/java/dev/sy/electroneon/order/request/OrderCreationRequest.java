package dev.sy.electroneon.order.request;

import java.util.List;
import java.util.UUID;

public record OrderCreationRequest(
        UUID userId,
        String street,
        String city,
        List<NestedOrderProductCreationRequest> orderProducts
) {

}
