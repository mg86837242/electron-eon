package dev.sy.electroneon.orderproduct;

import dev.sy.electroneon.exception.ConflictException;
import dev.sy.electroneon.exception.NotFoundException;
import dev.sy.electroneon.order.Order;
import dev.sy.electroneon.order.OrderRepository;
import dev.sy.electroneon.orderproduct.dto.OrderProductDTO;
import dev.sy.electroneon.orderproduct.dto.OrderProductDTOMapper;
import dev.sy.electroneon.orderproduct.request.OrderProductCreationRequest;
import dev.sy.electroneon.product.Product;
import dev.sy.electroneon.product.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderProductService {

    private final OrderProductRepository orderProductRepository;
    private final OrderProductDTOMapper orderProductDTOMapper;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderProductService(
            OrderProductRepository orderProductRepository,
            OrderProductDTOMapper orderProductDTOMapper,
            OrderRepository orderRepository,
            ProductRepository productRepository
    ) {
        this.orderProductRepository = orderProductRepository;
        this.orderProductDTOMapper = orderProductDTOMapper;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public OrderProductDTO getOrderProductById(UUID orderProductId) {
        return orderProductRepository
                .findById(orderProductId)
                .map(orderProductDTOMapper)
                .orElseThrow(() -> new NotFoundException(
                        "Order entry " + orderProductId + " not found"
                ));
    }

    public List<OrderProductDTO> getOrderProductsByOrderId(UUID orderId) {
        isOrderExisting(orderId);

        return orderProductRepository
                .findByOrderId(orderId)
                .stream()
                .map(orderProductDTOMapper)
                .collect(Collectors.toList());
    }

    public void isOrderProductExisting(UUID orderProductId) {
        if (!orderProductRepository.existsById(orderProductId)) {
            throw new NotFoundException(
                    "Order entry " + orderProductId + " not found"
            );
        }
    }

    public void isOrderExisting(UUID orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new NotFoundException(
                    "Order " + orderId + " not found"
            );
        }
    }

    public OrderProductDTO addOrderProduct(OrderProductCreationRequest request) {
        UUID orderId = request.orderId();
        UUID productId = request.productId();

        // Check if an `OrderProduct` record with the same combination of
        // `productId` and `orderId` already exists
        if (orderProductRepository.existsByOrderIdAndProductId(
                orderId,
                productId
        )) {
            throw new ConflictException(
                    "Order entry for orderId " + orderId + " and productId "
                            + productId + " already exists"
            );
        }

        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new NotFoundException(
                        "Order " + orderId + " not found"
                ));

        Product product = productRepository
                .findByIdAndIsArchivedFalse(productId)
                .orElseThrow(() -> new NotFoundException(
                        "Product " + productId + " not found"
                ));

        OrderProduct orderProduct = new OrderProduct(
                order,
                product,
                request.quantity()
        );

        OrderProduct savedOrderProduct =
                orderProductRepository.save(orderProduct);

        return orderProductDTOMapper.apply(savedOrderProduct);
    }

    public void addOrderProductForSeeding(OrderProduct orderProduct) {
        orderProductRepository.save(orderProduct);
    }

    @Transactional
    public void updateProductQtyInOrderById(
            UUID orderProductId,
            int quantity
    ) {
        OrderProduct orderProduct = orderProductRepository
                .findById(orderProductId)
                .orElseThrow(() -> new NotFoundException(
                        "Order entry " + orderProductId + " not found"
                ));

        orderProduct.setQuantity(quantity);

        orderProductRepository.save(orderProduct);
    }

    @Transactional
    public void incrementProductQtyInOrderById(UUID orderProductId) {
        OrderProduct orderProduct = orderProductRepository
                .findById(orderProductId)
                .orElseThrow(() -> new NotFoundException(
                        "Order entry " + orderProductId + " not found"
                ));

        int newQuantity = orderProduct.getQuantity() + 1;
        orderProduct.setQuantity(newQuantity);

        orderProductRepository.save(orderProduct);
    }

    @Transactional
    public void decrementProductQtyInOrderById(UUID orderProductId) {
        OrderProduct orderProduct = orderProductRepository
                .findById(orderProductId)
                .orElseThrow(() -> new NotFoundException(
                        "Order entry " + orderProductId + " not found"
                ));

        if (orderProduct.getQuantity() <= 1) {
            orderProductRepository.deleteById(orderProductId);
            return;
        }

        int newQuantity = orderProduct.getQuantity() - 1;
        orderProduct.setQuantity(newQuantity);

        orderProductRepository.save(orderProduct);
    }

    public void deleteOrderProductById(UUID orderProductId) {
        isOrderProductExisting(orderProductId);

        orderProductRepository.deleteById(orderProductId);
    }

    @Transactional
    public void deleteOrderProductsByOrderId(UUID orderId) {
        isOrderExisting(orderId);

        orderProductRepository.deleteByOrderId(orderId);
    }
}
