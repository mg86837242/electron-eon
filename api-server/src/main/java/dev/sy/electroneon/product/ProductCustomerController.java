package dev.sy.electroneon.product;

import dev.sy.electroneon.product.dto.ProductWithQtyInCartProjection;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customer/products")
public class ProductCustomerController {

    private final ProductService productService;

    public ProductCustomerController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductWithQtyInCartProjection> getAllProdsForCurrUser(
            Authentication authn
    ) {
        return productService.getAllProdsWithCartQtyForCurrUser(authn);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductWithQtyInCartProjection> getProdByIdForCurrUser(
            Authentication authn,
            @PathVariable UUID id
    ) {
        ProductWithQtyInCartProjection product =
                productService.getProdWithCartQtyByIdForCurrUser(authn, id);

        return ResponseEntity.ok(product);
    }

    @GetMapping(params = "category")
    public List<ProductWithQtyInCartProjection> getProdsByCategoryForCurrUser(
            Authentication authn,
            @RequestParam("category") String categoryStr
    ) {
        return productService.getProdsWithCartQtyByCategoryForCurrUser(
                authn,
                categoryStr
        );
    }
}
