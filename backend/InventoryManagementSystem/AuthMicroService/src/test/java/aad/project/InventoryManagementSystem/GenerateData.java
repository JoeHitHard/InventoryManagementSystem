package aad.project.InventoryManagementSystem;

import aad.project.InventoryManagementSystem.storage.entity.Product;
import aad.project.InventoryManagementSystem.storage.entity.Sale;
import aad.project.InventoryManagementSystem.storage.entity.User;
import com.github.javafaker.Faker;

import java.util.concurrent.TimeUnit;

public class GenerateData {
    public static void main(String[] args) {
        User user = new User("admin", "admin", "admin@add", User.Role.admin, System.currentTimeMillis(), "admin");
        user.save();
        Faker faker = new Faker();

        // Generate and save random data for products
        for (int i = 0; i < 10; i++) {
            Product product = new Product();
            product.setProductId(faker.number().digits(5));
            product.setName(faker.commerce().productName());
            product.setDescription(faker.lorem().sentence());
            product.setPrice(faker.number().randomDouble(2, 1, 100));
            product.setQuantity(faker.number().numberBetween(1, 100));
            product.save(); // Assuming you have a save method in your Product class to save to the database
        }

        // Generate and save random data for sales
        for (int i = 0; i < 10; i++) {
            Sale sale = new Sale();
            sale.setSaleId(faker.number().digits(5));
            sale.setProductId(faker.number().digits(5));
            sale.setQuantitySold(faker.number().numberBetween(1, 20));
            sale.setSaleDate(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(faker.number().numberBetween(1, 30)));
            sale.save(); // Assuming you have a save method in your Sale class to save to the database
        }
    }
}
