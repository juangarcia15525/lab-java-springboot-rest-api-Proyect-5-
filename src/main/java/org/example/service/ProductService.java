package org.example.service;

import org.example.exception.InvalidPriceRangeException;
import org.example.exception.ProductNotFoundException;
import org.example.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final List<Product> products = new ArrayList<>();

    public Product addProduct(Product product) {
        products.add(product);
        return product;
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    public Product getProductByName(String name) {
        return products.stream()
                .filter(product -> product.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado: " + name));
    }

    public Product updateProduct(String name, Product updatedProduct) {
        Product existingProduct = getProductByName(name);
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setQuantity(updatedProduct.getQuantity());
        return existingProduct;
    }

    public void deleteProduct(String name) {
        Product product = getProductByName(name);
        products.remove(product);
    }

    public List<Product> getProductsByCategory(String category) {
        return products.stream()
                .filter(product -> product.getCategory().equalsIgnoreCase(category))
                .toList();
    }

    public List<Product> getProductsByPriceRange(double min, double max) {
        if (min < 0 || max < 0 || min > max) {
            throw new InvalidPriceRangeException("El rango de precios es invalido");
        }

        return products.stream()
                .filter(product -> product.getPrice() >= min && product.getPrice() <= max)
                .toList();
    }
}
