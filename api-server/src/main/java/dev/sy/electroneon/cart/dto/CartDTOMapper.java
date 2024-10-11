package dev.sy.electroneon.cart.dto;

import dev.sy.electroneon.cart.Cart;
import dev.sy.electroneon.product.dto.ProductDTOMapper;
import dev.sy.electroneon.user.dto.UserDTOMapper;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CartDTOMapper implements Function<Cart, CartDTO> {

    private final UserDTOMapper userDTOMapper;
    private final ProductDTOMapper productDTOMapper;

    public CartDTOMapper(
            UserDTOMapper userDTOMapper,
            ProductDTOMapper productDTOMapper
    ) {
        this.userDTOMapper = userDTOMapper;
        this.productDTOMapper = productDTOMapper;
    }

    @Override
    public CartDTO apply(Cart cart) {
        return new CartDTO(
                cart.getId(),
                userDTOMapper.apply(cart.getUser()),
                productDTOMapper.apply(cart.getProduct()),
                cart.getQuantity()
        );
    }
}
