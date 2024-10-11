package dev.sy.electroneon.user;

import dev.sy.electroneon.cart.CartRepository;
import dev.sy.electroneon.exception.BadRequestException;
import dev.sy.electroneon.exception.ConflictException;
import dev.sy.electroneon.exception.NotFoundException;
import dev.sy.electroneon.order.Order;
import dev.sy.electroneon.order.OrderRepository;
import dev.sy.electroneon.orderproduct.OrderProductRepository;
import dev.sy.electroneon.user.dto.UserDTO;
import dev.sy.electroneon.user.dto.UserDTOMapper;
import dev.sy.electroneon.user.request.UserRegistrationRequest;
import dev.sy.electroneon.user.request.UserUpdateRequest;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserDTOMapper userDTOMapper;
    private final PasswordEncoder passwordEncoder;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final CartRepository cartRepository;

    public UserService(
            UserRepository userRepository,
            UserDTOMapper userDTOMapper,
            PasswordEncoder passwordEncoder,
            OrderRepository orderRepository,
            OrderProductRepository orderProductRepository,
            CartRepository cartRepository
    ) {
        this.userRepository = userRepository;
        this.userDTOMapper = userDTOMapper;
        this.passwordEncoder = passwordEncoder;
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
        this.cartRepository = cartRepository;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userDTOMapper)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(UUID userId) {
        return userRepository.findById(userId)
                .map(userDTOMapper)
                .orElseThrow(() -> new NotFoundException(
                        "User " + userId + " not found"
                ));
    }

    public UserDTO getCurrUser(Authentication authn) {
        String email = authn.getName();

        return userRepository.findByEmail(email)
                .map(userDTOMapper)
                .orElseThrow(() -> new NotFoundException(
                        "User not found based on the principal's email: " + email
                ));
    }

    public void isUserExisting(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(
                    "User " + userId + " not found"
            );
        }
    }

    public void isEmailConflict(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException(
                    "Email " + email + " has already been used"
            );
        }
    }

    public UserDTO registerUser(UserRegistrationRequest request) {
        String email = request.email();

        isEmailConflict(email);

        User user = new User(
                request.email(),
                passwordEncoder.encode(request.password()),
                request.firstName(),
                request.lastName(),
                // Newly-registered defaults to customer role as per biz logic
                Role.CUSTOMER
        );

        User registeredUser = userRepository.saveAndFlush(user);

        return userDTOMapper.apply(registeredUser);
    }

    public void updateUserById(
            UUID userId,
            UserUpdateRequest request
    ) {
        // No usage of DTO to allow updating pass
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        "User " + userId + " not found"
                ));

        updateUserUtil(user, request);
    }

    public void updateCurrUser(
            Authentication authn,
            UserUpdateRequest request
    ) {
        String email = authn.getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new NotFoundException(
                        "User not found based on the principal's email: " + email
                ));

        updateUserUtil(user, request);
    }

    private void updateUserUtil(
            User user,
            UserUpdateRequest request
    ) {
        boolean shouldUpdate = false;

        if (request.email() != null && !Objects.equals(
                request.email(),
                user.getEmail()
        )) {
            isEmailConflict(request.email());

            user.setEmail(request.email());
            shouldUpdate = true;
        }

        if (request.password() != null && !Objects.equals(
                passwordEncoder.encode(request.password()),
                passwordEncoder.encode(user.getPassword())
        )) {
            user.setPassword(
                    passwordEncoder.encode(request.password())
            );
            shouldUpdate = true;
        }

        if (request.firstName() != null & !Objects.equals(
                request.firstName(),
                user.getFirstName()
        )) {
            user.setFirstName(request.firstName());
            shouldUpdate = true;
        }

        if (request.lastName() != null & !Objects.equals(
                request.lastName(),
                user.getLastName()
        )) {
            user.setLastName(request.lastName());
            shouldUpdate = true;
        }

        if (request.role() != null && !Objects.equals(
                request.role(),
                user.getRole()
        )) {
            user.setRole(request.role());
            shouldUpdate = true;
        }

        if (!shouldUpdate) {
            throw new BadRequestException("No changes detected");
        }

        userRepository.save(user);
    }

    @Transactional
    public void deleteUserById(UUID userId) {
        isUserExisting(userId);

        deleteUserUtil(userId);
    }

    @Transactional
    public void deleteCurrUser(Authentication authn) {
        String email = authn.getName();

        UUID userId = userRepository
                .findIdByEmail(email)
                .orElseThrow(() -> new NotFoundException(
                        "User not found based on the principal's email: " + email
                ));

        deleteUserUtil(userId);
    }

    private void deleteUserUtil(UUID userId) {
        // Delete order entries (or `OrderProduct` records) that are
        // included in the order created by the user w/ the provided `userId`
        List<Order> orders = orderRepository.findByUserId(userId);
        orders.forEach(order -> orderProductRepository.deleteByOrderId(order.getId()));

        // Delete orders by `userId`
        orderRepository.deleteByUserId(userId);

        // Delete cart entries by `userId`
        cartRepository.deleteByUserId(userId);

        // Delete the user by `userId`
        userRepository.deleteById(userId);
    }
}
