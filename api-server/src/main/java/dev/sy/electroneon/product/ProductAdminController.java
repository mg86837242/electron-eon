package dev.sy.electroneon.product;

import dev.sy.electroneon.product.dto.ProductDTO;
import dev.sy.electroneon.product.request.ProductCreationRequest;
import dev.sy.electroneon.product.request.ProductUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/admin/products")
public class ProductAdminController {

    private final ProductService productService;

    public ProductAdminController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductDTO> addProduct(
            @RequestBody ProductCreationRequest request
    ) {
        ProductDTO savedProduct = productService.addProduct(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
                "/{id}").buildAndExpand(savedProduct.id()).toUri();

        return ResponseEntity.created(location).body(savedProduct);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProductById(
            @PathVariable UUID id,
            @RequestBody ProductUpdateRequest request
    ) {
        productService.updateProductById(id, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public void deleteProductById(@PathVariable UUID id) {
        productService.deleteProductById(id);
    }
}
