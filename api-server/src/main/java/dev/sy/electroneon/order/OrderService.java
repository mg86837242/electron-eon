package dev.sy.electroneon.order;

import dev.sy.electroneon.cart.CartRepository;
import dev.sy.electroneon.exception.BadRequestException;
import dev.sy.electroneon.exception.NotFoundException;
import dev.sy.electroneon.order.dto.OrderDTO;
import dev.sy.electroneon.order.dto.OrderDTOMapper;
import dev.sy.electroneon.order.dto.OrderWithProductsDTO;
import dev.sy.electroneon.order.dto.OrderWithProductsDTOMapper;
import dev.sy.electroneon.order.request.OrderCreationCustomerRequest;
import dev.sy.electroneon.order.request.OrderUpdateRequest;
import dev.sy.electroneon.orderproduct.OrderProduct;
import dev.sy.electroneon.orderproduct.OrderProductRepository;
import dev.sy.electroneon.product.Product;
import dev.sy.electroneon.product.ProductRepository;
import dev.sy.electroneon.user.User;
import dev.sy.electroneon.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDTOMapper orderDTOMapper;
    private final OrderWithProductsDTOMapper orderWithProductsDTOMapper;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;
    private final CartRepository cartRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderDTOMapper orderDTOMapper,
            OrderWithProductsDTOMapper orderWithProductsDTOMapper,
            UserRepository userRepository,
            ProductRepository productRepository,
            OrderProductRepository orderProductRepository,
            CartRepository cartRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderDTOMapper = orderDTOMapper;
        this.orderWithProductsDTOMapper = orderWithProductsDTOMapper;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderProductRepository = orderProductRepository;
        this.cartRepository = cartRepository;
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository
                .findAll()
                .stream()
                .map(orderDTOMapper)
                .collect(Collectors.toList());
    }

    public OrderWithProductsDTO getOrderById(UUID orderId) {
        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new NotFoundException(
                        "Order " + orderId + " not found"
                ));

        List<OrderProduct> orderProducts =
                orderProductRepository.findByOrderId(orderId);

        return orderWithProductsDTOMapper.apply(order, orderProducts);
    }

    public List<OrderDTO> getOrdersByUserId(UUID userId) {
        isUserExisting(userId);

        return orderRepository
                .findByUserId(userId)
                .stream()
                .map(orderDTOMapper)
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getOrdersForCurrUser(Authentication authn) {
        String email = authn.getName();

        isEmailExisting(email);

        return orderRepository
                .findByEmail(email)
                .stream()
                .map(orderDTOMapper)
                .collect(Collectors.toList());
    }

    public void isOrderExisting(UUID orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new NotFoundException(
                    "Order " + orderId + " not found"
            );
        }
    }

    public void isUserExisting(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(
                    "User " + userId + " not found"
            );
        }
    }

    public void isEmailExisting(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new NotFoundException(
                    "Email " + email + " not found"
            );
        }
    }

    @Transactional
    public OrderWithProductsDTO addOrderForCurrUser(
            Authentication authn,
            OrderCreationCustomerRequest request
    ) {
        // Find the curr authenticated user based on the email
        String email = authn.getName();

        isEmailExisting(email);

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new NotFoundException(
                        "User not found based on the principal's email: " + email
                ));

        // Create a new `Order` object, then save and flush to the db
        Order order = new Order(
                user,
                request.street(),
                request.city(),
                LocalDateTime.now()
        );
        Order savedOrder = orderRepository.saveAndFlush(order);

        // Convert the nested list within the request to a list of
        // `OrderProduct` objects, then save and flush the list to the db
        List<OrderProduct> orderProducts = request
                .orderProducts()
                .stream()
                .map(orderProduct -> {
                    UUID productId = orderProduct.productId();

                    Product product = productRepository
                            .findByIdAndIsArchivedFalse(productId)
                            .orElseThrow(() -> new NotFoundException(
                                    "Product " + productId + " not found"
                            ));

                    return new OrderProduct(
                            savedOrder,
                            product,
                            orderProduct.quantity()
                    );
                })
                .toList();

        List<OrderProduct> savedOrderProducts =
                orderProductRepository.saveAllAndFlush(orderProducts);

        // Use DTO to nest the `OrderProduct` list into the `Order` object
        OrderWithProductsDTO savedOrderWithProductsDTO =
                orderWithProductsDTOMapper.apply(
                        savedOrder,
                        savedOrderProducts
                );

        // Discard the cart entries associated w/ the curr authenticated user after
        // placing the order (if there's a feature to let users tick what items
        // to check out, then this line needs to be removed, or replaced with
        // logics to check what item(s) needs to be removed from the cart)
        cartRepository.deleteByUserId(user.getId());

        return savedOrderWithProductsDTO;
    }

    public void updateOrderById(
            UUID orderId,
            OrderUpdateRequest request
    ) {
        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new NotFoundException(
                        "Order " + orderId + " not found"
                ));

        boolean shouldUpdate = false;

        if (request.street() != null & !Objects.equals(
                request.street(),
                order.getStreet()
        )) {
            order.setStreet(request.street());
            shouldUpdate = true;
        }

        if (request.city() != null & !Objects.equals(
                request.city(),
                order.getCity()
        )) {
            order.setCity(request.city());
            shouldUpdate = true;
        }

        if (!shouldUpdate) {
            throw new BadRequestException("No changes detected");
        }

        orderRepository.save(order);
    }

    @Transactional
    public void deleteOrderById(UUID orderId) {
        isOrderExisting(orderId);

        // Delete order entries (or `OrderProduct` records) that are
        // included in the order
        orderProductRepository.deleteByOrderId(orderId);

        orderRepository.deleteById(orderId);
    }
}
