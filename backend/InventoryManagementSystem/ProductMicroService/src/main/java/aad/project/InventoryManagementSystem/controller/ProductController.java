package aad.project.InventoryManagementSystem.controller;

import aad.project.InventoryManagementSystem.config.scurity.exceptions.InvalidAuthRequest;
import aad.project.InventoryManagementSystem.storage.entitites.Product;
import aad.project.InventoryManagementSystem.storage.entitites.User;
import aad.project.InventoryManagementSystem.storage.requests.ProductRequestBody;
import aad.project.InventoryManagementSystem.utils.storage.entity.RequestAuthUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    // Create a new product
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody String productRequestBody,  @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws InvalidAuthRequest {
        User user = RequestAuthUtils.getUser(authorizationHeader);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ProductRequestBody requestBody = objectMapper.readValue(productRequestBody, ProductRequestBody.class);
            Product product = requestBody.mapToProduct();
            product.save();
            return new ResponseEntity<>(product, HttpStatus.CREATED);
        } catch (IOException e) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Retrieve a product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id,  @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws InvalidAuthRequest {
        User user = RequestAuthUtils.getUser(authorizationHeader);
        Product product = new Product(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    // Retrieve all products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts( @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws InvalidAuthRequest {
        User user = RequestAuthUtils.getUser(authorizationHeader);
        List<Product> products = Product.ProductDAO.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // Update an existing product
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable String id, @RequestBody String productRequestBody, @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws InvalidAuthRequest {
        User user = RequestAuthUtils.getUser(authorizationHeader);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ProductRequestBody requestBody = objectMapper.readValue(productRequestBody, ProductRequestBody.class);
            Product product = new Product(id);
            product.setName(requestBody.getName());
            product.setDescription(requestBody.getDescription());
            product.setPrice(requestBody.getPrice());
            product.setQuantity(requestBody.getQuantity());
            product.update();
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Delete a product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id, @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws InvalidAuthRequest {
        User user = RequestAuthUtils.getUser(authorizationHeader);
        Product product = new Product(id);
        product.delete();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
