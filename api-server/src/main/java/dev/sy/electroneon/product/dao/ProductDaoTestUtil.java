package dev.sy.electroneon.product.dao;

import dev.sy.electroneon.product.Category;
import dev.sy.electroneon.product.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ProductDaoTestUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ProductDaoTestUtil.class);
    private final ProductDao pd;

    public ProductDaoTestUtil(ProductDao pd) {
        this.pd = pd;
    }

    public void selectAllProducts() {
        List<Product> products = pd.selectAllProducts();

        LOG.info(">> selectAllProducts() result:");
        if (products.isEmpty()) {
            LOG.info("None");
        } else {
            products.forEach(product -> LOG.info(String.valueOf(product)));
        }
    }

    public void selectProductById() {
        Optional<Product> optionalProduct = pd.selectProductById(
                UUID.fromString(
                        "2103757c-2e2d-4dfb-befd-987f4fcce43a"
                )
        );

        LOG.info(">> selectProductById() result:");
        LOG.info(String.valueOf(optionalProduct));
    }

    public void selectProductsByCategory() {
        List<Product> products = pd.selectProductsByCategory(Category.COMPUTER_ACCESSORY);

        LOG.info(">> selectProductsByCategory() result:");
        if (products.isEmpty()) {
            LOG.info("None");
        } else {
            products.forEach(product -> LOG.info(String.valueOf(product)));
        }
    }

    public void insertProduct() {
        Product product1 = new Product(
                UUID.fromString(
                        "2103757c-2e2d-4dfb-befd-987f4fcce43a"
                ),
                "Dell Inspiron 3511 Laptop",
                "Lorem ipsum dolor sit amet.",
                706.5,
                Category.LAPTOP
        );
        Product product2 = new Product(
                UUID.fromString(
                        "22cd8441-4277-4d7c-bafa-2cd8af3d5cc4"
                ),
                "Lenovo IdeaPad 1 Student Laptop",
                "Lorem ipsum dolor sit amet.",
                528.5,
                Category.LAPTOP
        );
        Product product3 = new Product(
                UUID.fromString(
                        "23d4364f-cd62-4e80-a017-3c16c43a7908"
                ),
                "Samsung Galaxy A04e",
                "Lorem ipsum dolor sit amet.",
                121.9,
                Category.SMARTPHONE
        );
        Product product4 = new Product(
                UUID.fromString(
                        "24f8ae7f-de04-4e4e-aa53-2cea8974a861"
                ),
                "Motorola Moto G 5G (2023)",
                "Lorem ipsum dolor sit amet.",
                203.8,
                Category.SMARTPHONE
        );
        Product product5 = new Product(
                UUID.fromString(
                        "252c3087-57f7-4332-9b22-5d858b302783"
                ),
                "LG Electronics 24LM530S-PU 24-Inch HD",
                "Lorem ipsum dolor sit amet.",
                217.5,
                Category.COMPUTER_ACCESSORY
        );
        Product product6 = new Product(
                UUID.fromString(
                        "2621f16a-c94f-4225-b817-e54c4fb9d2b9"
                ),
                "Samsung 32-Inch Class QLED Q60A Series - 4K UHD",
                "Lorem ipsum dolor sit amet.",
                611.3,
                Category.COMPUTER_ACCESSORY
        );

        pd.insertProduct(product1);
        pd.insertProduct(product2);
        pd.insertProduct(product3);
        pd.insertProduct(product4);
        pd.insertProduct(product5);
        pd.insertProduct(product6);
    }

    public void updateProduct() {
        Product update = new Product(
                UUID.fromString(
                        "22cd8441-4277-4d7c-bafa-2cd8af3d5cc4"
                ),
                "Lenovo IdeaPad 1 Laptop",
                "Lorem ipsum dolor sit amet.",
                489.8,
                Category.LAPTOP

        );

        pd.updateProduct(update);
    }

    public void deleteProductById() {
        pd.deleteProductById(
                UUID.fromString(
                        "22cd8441-4277-4d7c-bafa-2cd8af3d5cc4"
                )
        );
    }
}
