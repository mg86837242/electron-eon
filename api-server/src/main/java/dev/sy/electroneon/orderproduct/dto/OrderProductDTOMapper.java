package dev.sy.electroneon.orderproduct.dto;

import dev.sy.electroneon.order.dto.OrderDTOMapper;
import dev.sy.electroneon.orderproduct.OrderProduct;
import dev.sy.electroneon.product.dto.ProductDTOMapper;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class OrderProductDTOMapper implements Function<OrderProduct, OrderProductDTO> {

    private final OrderDTOMapper orderDTOMapper;
    private final ProductDTOMapper productDTOMapper;

    public OrderProductDTOMapper(
            OrderDTOMapper orderDTOMapper,
            ProductDTOMapper productDTOMapper
    ) {
        this.orderDTOMapper = orderDTOMapper;
        this.productDTOMapper = productDTOMapper;
    }

    @Override
    public OrderProductDTO apply(OrderProduct orderProduct) {
        return new OrderProductDTO(
                orderProduct.getId(),
                orderDTOMapper.apply(orderProduct.getOrder()),
                productDTOMapper.apply(orderProduct.getProduct()),
                orderProduct.getQuantity()
        );
    }
}
