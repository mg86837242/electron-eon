package dev.sy.electroneon.product;

import dev.sy.electroneon.exception.BadRequestException;
import dev.sy.electroneon.exception.NotFoundException;
import dev.sy.electroneon.product.dto.ProductDTO;
import dev.sy.electroneon.product.dto.ProductDTOMapper;
import dev.sy.electroneon.product.dto.ProductWithQtyInCartProjection;
import dev.sy.electroneon.product.request.ProductCreationRequest;
import dev.sy.electroneon.product.request.ProductUpdateRequest;
import dev.sy.electroneon.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductDTOMapper productDTOMapper;
    private final UserRepository userRepository;

    public ProductService(
            ProductRepository productRepository,
            ProductDTOMapper productDTOMapper,
            UserRepository userRepository
    ) {
        this.productRepository = productRepository;
        this.productDTOMapper = productDTOMapper;
        this.userRepository = userRepository;
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository
                .findAllByIsArchivedFalse()
                .stream()
                .map(
                        productDTOMapper)
                .collect(Collectors.toList());
    }

    public ProductDTO getProductById(UUID productId) {
        return productRepository
                .findByIdAndIsArchivedFalse(productId)
                .map(
                        productDTOMapper)
                .orElseThrow(() -> new NotFoundException(
                        "Product " + productId + " not found"));
    }

    public List<ProductDTO> getProductsByCategory(String categoryStr) {
        Category category = Stream
                .of(Category.values())
                .filter(value -> value
                        .getCode()
                        .equals(categoryStr))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(
                        "Invalid query parameter" + categoryStr));

        return productRepository
                .findByCategoryAndIsArchivedFalse(category)
                .stream()
                .map(productDTOMapper)
                .collect(Collectors.toList());
    }

    public List<ProductWithQtyInCartProjection> getAllProdsWithCartQtyForCurrUser(
            Authentication authn
    ) {
        String email = authn.getName();

        isEmailExisting(email);

        // Find the curr authenticated user's id based on the email
        UUID userId = userRepository
                .findIdByEmail(email)
                .orElseThrow(() -> new NotFoundException(
                        "User not found based on the principal's email: " + email));

        // Use interface-based project (or classed-based, aka DTO) to add the
        // `quantity` field of the `Cart` entity to each `Product` object in
        // the list
        return productRepository.findAllProductsWithQtyInCartByUserIdAndIsArchivedFalse(
                userId);
    }

    public ProductWithQtyInCartProjection getProdWithCartQtyByIdForCurrUser(
            Authentication authn, UUID productId
    ) {
        String email = authn.getName();

        isEmailExisting(email);

        // Find the curr authenticated user's id based on the email
        UUID userId = userRepository
                .findIdByEmail(email)
                .orElseThrow(() -> new NotFoundException(
                        "User not found based on the principal's email: " + email));

        // Use interface-based project (or classed-based, aka DTO) to add the
        // `quantity` field of the `Cart` entity to the `Product` object
        return productRepository
                .findProductWithQtyInCartByUserIdAndByIdAndIsArchivedFalse(
                        userId,
                        productId
                )
                .orElseThrow(() -> new NotFoundException("Product " + productId + " not found"));
    }

    public List<ProductWithQtyInCartProjection> getProdsWithCartQtyByCategoryForCurrUser(
            Authentication authn, String categoryStr
    ) {
        String email = authn.getName();

        isEmailExisting(email);

        // Find the curr authenticated user's id based on the email
        UUID userId = userRepository
                .findIdByEmail(email)
                .orElseThrow(() -> new NotFoundException(
                        "User not found based on the principal's email: " + email));

        // Parse the category string
        Category category = Stream
                .of(Category.values())
                .filter(value -> value
                        .getCode()
                        .equals(categoryStr))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(
                        "Invalid path variable"));

        // Use interface-based project (or classed-based, aka DTO) to add the
        // `quantity` field of the `Cart` entity to each `Product` object in
        // the list
        return productRepository.findProductsWithQtyInCartByUserIdAndCategoryAndIsArchivedFalse(
                userId,
                category
        );
    }

    public void isEmailExisting(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new NotFoundException("Email " + email + " not found");
        }
    }

    public ProductDTO addProduct(ProductCreationRequest request) {
        Product product = new Product(
                request.name(),
                request.description(),
                request.price(),
                request.category()
        );

        Product savedProduct = productRepository.saveAndFlush(product);

        return productDTOMapper.apply(savedProduct);
    }

    public void addProductForSeeding(Product product) {
        productRepository.save(product);
    }

    public void updateProductById(
            UUID productId, ProductUpdateRequest request
    ) {
        Product product = productRepository
                .findByIdAndIsArchivedFalse(productId)
                .orElseThrow(() -> new NotFoundException("Product " + productId + " not found"));

        boolean shouldUpdate = false;

        if (request.name() != null && !Objects.equals(
                request.name(),
                product.getName()
        )) {
            product.setName(request.name());
            shouldUpdate = true;
        }

        if (request.description() != null & !Objects.equals(
                request.description(),
                product.getDescription()
        )) {
            product.setDescription(request.description());
            shouldUpdate = true;
        }

        if (request.price() != null & !Objects.equals(
                request.price(),
                product.getPrice()
        )) {
            product.setPrice(request.price());
            shouldUpdate = true;
        }

        if (request.category() != null & !Objects.equals(
                request.category(),
                product.getCategory()
        )) {
            product.setCategory(request.category());
            shouldUpdate = true;
        }

        if (!shouldUpdate) {
            throw new BadRequestException("No changes detected");
        }

        productRepository.save(product);
    }

    public void deleteProductById(UUID productId) {
        Product product = productRepository
                .findByIdAndIsArchivedFalse(productId)
                .orElseThrow(() -> new NotFoundException("Product " + productId + " not found"));

        product.setArchived(true);

        productRepository.save(product);
    }
}
