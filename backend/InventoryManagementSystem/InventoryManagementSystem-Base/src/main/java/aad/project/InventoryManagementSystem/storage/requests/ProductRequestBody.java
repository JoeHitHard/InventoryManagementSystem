package aad.project.InventoryManagementSystem.storage.requests;

import aad.project.InventoryManagementSystem.storage.entity.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

// Class representing the request body for creating or updating a product
public class ProductRequestBody {
    private String name;
    private String description;
    private double price;
    private int quantity;

    public ProductRequestBody() {
    }

    public ProductRequestBody(String name, String description, double price, int quantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ProductRequestBody requestBody = objectMapper.readValue("{\"name\":\"sasa\",\"description\":\"Et aut nobis ipsum ducimus est voluptatem voluptatibus.\",\"price\":5.58,\"quantity\":14}", ProductRequestBody.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Helper method to map ProductRequestBody to Product
    public Product mapToProduct() {
        return new Product(getName(), getDescription(),
                getPrice(), getQuantity());
    }
}
