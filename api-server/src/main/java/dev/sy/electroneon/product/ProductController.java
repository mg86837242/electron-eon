package dev.sy.electroneon.product;

import dev.sy.electroneon.product.dto.ProductDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable UUID id) {
        ProductDTO product = productService.getProductById(id);

        return ResponseEntity.ok(product);
    }

    @GetMapping(params = "category")
    public List<ProductDTO> getProductsByCategory(
            @RequestParam("category") String categoryStr
    ) {
        return productService.getProductsByCategory(categoryStr);
    }
}
