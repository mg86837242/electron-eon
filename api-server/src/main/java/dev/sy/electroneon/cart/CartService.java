package dev.sy.electroneon.cart;

import dev.sy.electroneon.cart.dto.CartDTO;
import dev.sy.electroneon.cart.dto.CartDTOMapper;
import dev.sy.electroneon.exception.NotFoundException;
import dev.sy.electroneon.product.Product;
import dev.sy.electroneon.product.ProductRepository;
import dev.sy.electroneon.user.User;
import dev.sy.electroneon.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartDTOMapper cartDTOMapper;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartService(
            CartRepository cartRepository,
            CartDTOMapper cartDTOMapper,
            UserRepository userRepository,
            ProductRepository productRepository
    ) {
        this.cartRepository = cartRepository;
        this.cartDTOMapper = cartDTOMapper;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public CartDTO getCartById(UUID cartId) {
        return cartRepository.findById(cartId)
                .map(cartDTOMapper)
                .orElseThrow(() -> new NotFoundException(
                        "Cart entry " + cartId + " not found"
                ));
    }

    public List<CartDTO> getCartsByUserId(UUID userId) {
        isUserExisting(userId);

        return cartRepository.findByUserId(userId)
                .stream()
                .map(cartDTOMapper)
                .collect(Collectors.toList());
    }

    public List<CartDTO> getCartsForCurrUser(Authentication authn) {
        String email = authn.getName();

        isEmailExisting(email);

        return cartRepository.findByEmail(email)
                .stream()
                .map(cartDTOMapper)
                .collect(Collectors.toList());
    }

    public void isCartExisting(UUID cartId) {
        if (!cartRepository.existsById(cartId)) {
            throw new NotFoundException(
                    "Cart entry " + cartId + " not found"
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

    // For catalogue page
    @Transactional
    public void upsertProdQtyInCartForCurrUser(
            Authentication authn,
            UUID productId,
            int quantity
    ) {
        String email = authn.getName();

        isEmailExisting(email);

        Optional<Cart> optionalCart = cartRepository.findByEmailAndProductId(
                email,
                productId
        );

        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();

            int newQuantity = cart.getQuantity() + quantity;
            cart.setQuantity(newQuantity);

            cartRepository.save(cart);
        } else {
            User user = userRepository
                    .findByEmail(email)
                    .orElseThrow(() -> new NotFoundException(
                            "User not found based on the principal's email: " + email
                    ));

            Product product = productRepository
                    .findByIdAndIsArchivedFalse(productId)
                    .orElseThrow(() -> new NotFoundException(
                            "Product " + productId + " not found"
                    ));

            Cart newCart = new Cart(
                    user, product, quantity
            );

            cartRepository.save(newCart);
        }
    }

    // For catalogue page
    @Transactional
    public void incrementProdQtyInCartForCurrUser(
            Authentication authn,
            UUID productId
    ) {
        String email = authn.getName();

        isEmailExisting(email);

        Optional<Cart> optionalCart = cartRepository.findByEmailAndProductId(
                email,
                productId
        );

        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();

            int newQuantity = cart.getQuantity() + 1;
            cart.setQuantity(newQuantity);

            cartRepository.save(cart);
        } else {
            User user = userRepository
                    .findByEmail(email)
                    .orElseThrow(() -> new NotFoundException(
                            "User not found based on the principal's email: " + email
                    ));

            Product product = productRepository
                    .findByIdAndIsArchivedFalse(productId)
                    .orElseThrow(() -> new NotFoundException(
                            "Product " + productId + " not found"
                    ));

            Cart newCart = new Cart(
                    user, product, 1
            );

            cartRepository.save(newCart);
        }
    }

    // For catalogue page
    @Transactional
    public void decrementProdQtyInCartForCurrUser(
            Authentication authn,
            UUID productId
    ) {
        String email = authn.getName();

        isEmailExisting(email);

        Optional<Cart> optionalCart = cartRepository.findByEmailAndProductId(
                email,
                productId
        );

        if (optionalCart.isEmpty()) {
            return;
        }

        Cart cart = optionalCart.get();

        if (cart.getQuantity() <= 1) {
            cartRepository.deleteById(cart.getId());
        } else {
            int newQuantity = cart.getQuantity() - 1;
            cart.setQuantity(newQuantity);

            cartRepository.save(cart);
        }
    }

    // For individual product page
    @Transactional
    public void updateProdQtyInCartById(
            UUID cartId,
            int quantity
    ) {
        Cart cart = cartRepository
                .findById(cartId)
                .orElseThrow(() -> new NotFoundException(
                        "Cart entry " + cartId + " not found"
                ));

        cart.setQuantity(quantity);

        cartRepository.save(cart);
    }

    // For dedicated cart page or admin page
    @Transactional
    public void incrementProdQtyInCartById(UUID cartId) {
        Cart cart = cartRepository
                .findById(cartId)
                .orElseThrow(() -> new NotFoundException(
                        "Cart entry " + cartId + " not found"
                ));

        int newQuantity = cart.getQuantity() + 1;
        cart.setQuantity(newQuantity);

        cartRepository.save(cart);
    }

    // For dedicated cart page or admin page
    @Transactional
    public void decrementProdQtyInCartById(UUID cartId) {
        Cart cart = cartRepository
                .findById(cartId)
                .orElseThrow(() -> new NotFoundException(
                        "Cart entry " + cartId + " not found"
                ));

        if (cart.getQuantity() <= 1) {
            cartRepository.deleteById(cartId);
            return;
        }

        int newQuantity = cart.getQuantity() - 1;
        cart.setQuantity(newQuantity);

        cartRepository.save(cart);
    }

    public void deleteCartById(UUID cartId) {
        isCartExisting(cartId);

        cartRepository.deleteById(cartId);
    }

    @Transactional
    public void deleteCartByUserId(UUID userId) {
        isUserExisting(userId);

        cartRepository.deleteByUserId(userId);
    }

    @Transactional
    public void deleteCartForCurrUser(Authentication authn) {
        String email = authn.getName();

        isEmailExisting(email);

        cartRepository.deleteByEmail(email);
    }
}
