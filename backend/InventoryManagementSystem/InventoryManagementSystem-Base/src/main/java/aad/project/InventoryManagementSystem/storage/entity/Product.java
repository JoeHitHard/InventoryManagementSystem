package aad.project.InventoryManagementSystem.storage.entity;

import aad.project.InventoryManagementSystem.storage.dao.AbstractCassandraDAO;
import aad.project.InventoryManagementSystem.utils.storage.entity.ProductUtils;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.Row;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Product {

    public static final String PRODUCTS_TABLE = "products";
    public static boolean DEV = false;

    static {
        ProductDAO productDAO = new ProductDAO();
        productDAO.createTable();
    }

    transient ProductDAO productDAO;
    private String productId;
    private String name;
    private String description;
    private double price;
    private int quantity;

    public Product() {
        this.productDAO = new ProductDAO();
    }

    public Product(String productId) {
        this();
        this.productId = productId;
        productDAO.mapToEntity(productId, this);
    }

    public Product(String productId, String name, String description, double price, int quantity) {
        this();
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }
    public Product(String name, String description, double price, int quantity) {
        this(UUID.randomUUID().toString(), name, description, price, quantity);
    }

    public Product save() {
        productDAO.insert(productId, productId, name, description, price, quantity);
        return this;
    }

    public Product delete() {
        productDAO.delete(productId);
        return this;
    }

    public Product update() {
        productDAO.update(productId, name, description, price, quantity);
        return this;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public static class ProductDAO extends AbstractCassandraDAO<Product> {

        public static final PreparedStatement CREATE_STMT = getCqlSession().prepare("CREATE TABLE IF NOT EXISTS " +
                PRODUCTS_TABLE + " " + "(productId TEXT PRIMARY KEY, name TEXT, description TEXT, price DOUBLE, quantity INT)");

        public static PreparedStatement INSERT_STMT;
        public static PreparedStatement UPDATE_STMT;
        public static PreparedStatement DELETE_STMT;
        public static PreparedStatement SELECT_STMT;

        public static List<Product> getAllProducts() {
            List<Product> productList = new ArrayList<>();
            BoundStatement boundStatement = getCqlSession().prepare("SELECT * FROM " + PRODUCTS_TABLE).bind();
            getCqlSession().execute(boundStatement).forEach(row -> {
                Product product = new Product();
                ProductUtils.mapToProduct(product, row);
                productList.add(product);
            });
            return productList;
        }

        @Override
        public PreparedStatement getCreateStmt() {
            return CREATE_STMT;
        }

        @Override
        public PreparedStatement getInsertStmt() {
            if (INSERT_STMT == null) {
                INSERT_STMT = getCqlSession().prepare("INSERT INTO " + PRODUCTS_TABLE
                        + " (productId, name, description, price, quantity) VALUES (?, ?, ?, ?, ?)");
            }
            return INSERT_STMT;
        }

        @Override
        public PreparedStatement getUpdateStmt() {
            if (UPDATE_STMT == null) {
                UPDATE_STMT = getCqlSession().prepare("UPDATE " + PRODUCTS_TABLE
                        + " SET name = ?, description = ?, price = ?, quantity = ? WHERE productId = ?");
            }
            return UPDATE_STMT;
        }

        @Override
        public PreparedStatement getDeleteStmt() {
            if (DELETE_STMT == null) {
                DELETE_STMT = getCqlSession().prepare("DELETE FROM " + PRODUCTS_TABLE + " WHERE productId = ?");
            }
            return DELETE_STMT;
        }

        @Override
        public PreparedStatement getStmt() {
            if (SELECT_STMT == null) {
                SELECT_STMT = getCqlSession().prepare("SELECT * FROM " + PRODUCTS_TABLE + " WHERE productId = ?");
            }
            return SELECT_STMT;
        }

        @Override
        public Product mapToEntity(String key, Product product) {
            Row row = get(key).one();

            if (row != null) {
                if (product == null) {
                    product = new Product();
                }
                ProductUtils.mapToProduct(product, row);
                return product;
            }
            return null;
        }
    }

}
