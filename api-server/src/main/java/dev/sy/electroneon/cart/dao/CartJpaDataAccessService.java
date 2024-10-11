package dev.sy.electroneon.cart.dao;

import dev.sy.electroneon.cart.Cart;
import dev.sy.electroneon.cart.CartRepository;
import dev.sy.electroneon.exception.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Primary
public class CartJpaDataAccessService implements CartDao {

    private final CartRepository cartRepository;

    public CartJpaDataAccessService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public Optional<Cart> selectCartById(UUID cartId) {
        return cartRepository.findById(cartId);
    }

    @Override
    public List<Cart> selectCartsByUserId(UUID userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public boolean existsCartById(UUID cartId) {
        return cartRepository.existsById(cartId);
    }

    @Override
    public void insertCart(Cart cart) {
        cartRepository.save(cart);
    }

    @Override
    public void updateCart(Cart update) {
        UUID cartId = update.getId();
        int quantity = update.getQuantity();

        Cart cart = cartRepository
                .findById(cartId)
                .orElseThrow(() -> new NotFoundException(
                        "Cart entry " + cartId + " not found"
                ));

        cart.setQuantity(quantity);

        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void incrementCartQty(UUID cartId) {
        Cart cart = cartRepository
                .findById(cartId)
                .orElseThrow(() -> new NotFoundException(
                        "Cart entry " + cartId + " not found"
                ));

        int newQuantity = cart.getQuantity() + 1;
        cart.setQuantity(newQuantity);

        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void decrementCartQty(UUID cartId) {
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

    @Override
    public void deleteCartById(UUID cartId) {
        cartRepository.deleteById(cartId);
    }
}
