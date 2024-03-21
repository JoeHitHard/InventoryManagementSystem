package aad.project.InventoryManagementSystem.storage.entitites;

import aad.project.InventoryManagementSystem.storage.dao.AbstractCassandraDAO;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.Row;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Sale {

    public static final String SALES_TABLE = "sales";
    public static boolean DEV = false;

    static {
        SalesDAO salesDAO = new SalesDAO();
        salesDAO.createTable();
    }

    transient SalesDAO salesDAO;
    private String saleId;
    private String productId;
    private int quantitySold;
    private Long saleDate;

    public Sale() {
        this.salesDAO = new SalesDAO();
    }

    public Sale(String saleId) {
        this();
        this.saleId = saleId;
        salesDAO.mapToEntity(saleId.toString(), this);
    }

    public Sale(String saleId, String productId, int quantitySold, Long saleDate) {
        this();
        this.saleId = saleId;
        this.productId = productId;
        this.quantitySold = quantitySold;
        this.saleDate = saleDate;
    }

    public Sale(String productId, int quantitySold, Long saleDate) {
        this(UUID.randomUUID().toString(), productId, quantitySold, saleDate);
    }

    public Sale save() {
        salesDAO.insert(saleId.toString(), saleId.toString(), productId.toString(), quantitySold, saleDate);
        return this;
    }

    public Sale delete() {
        salesDAO.delete(saleId.toString());
        return this;
    }

    public Sale update() {
        salesDAO.update(saleId.toString(), productId.toString(), quantitySold, saleDate);
        return this;
    }

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public Long getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Long saleDate) {
        this.saleDate = saleDate;
    }

    public static class SalesDAO extends AbstractCassandraDAO<Sale> {

        public static final PreparedStatement CREATE_STMT = getCqlSession().prepare("CREATE TABLE IF NOT EXISTS " +
                SALES_TABLE + " " + "(saleId TEXT PRIMARY KEY, productId TEXT, quantitySold INT, saleDate BIGINT)");

        public static PreparedStatement INSERT_STMT;
        public static PreparedStatement UPDATE_STMT;
        public static PreparedStatement DELETE_STMT;
        public static PreparedStatement SELECT_STMT;

        public static List<Sale> getAllSales() {
            List<Sale> saleList = new ArrayList<>();
            BoundStatement boundStatement = getCqlSession().prepare("SELECT * FROM " + SALES_TABLE).bind();
            getCqlSession().execute(boundStatement).forEach(row -> {
                Sale sale = new Sale();
                sale.saleId = row.getString("saleId");
                sale.productId = row.getString("productId");
                sale.quantitySold = row.getInt("quantitySold");
                sale.saleDate = row.getLong("saleDate");
                saleList.add(sale);
            });
            return saleList;
        }

        @Override
        public PreparedStatement getCreateStmt() {
            return CREATE_STMT;
        }

        @Override
        public PreparedStatement getInsertStmt() {
            if (INSERT_STMT == null) {
                INSERT_STMT = getCqlSession().prepare("INSERT INTO " + SALES_TABLE
                        + " (saleId, productId, quantitySold, saleDate) VALUES (?, ?, ?, ?)");
            }
            return INSERT_STMT;
        }

        @Override
        public PreparedStatement getUpdateStmt() {
            if (UPDATE_STMT == null) {
                UPDATE_STMT = getCqlSession().prepare("UPDATE " + SALES_TABLE
                        + " SET productId = ?, quantitySold = ?, saleDate = ? WHERE saleId = ?");
            }
            return UPDATE_STMT;
        }

        @Override
        public PreparedStatement getDeleteStmt() {
            if (DELETE_STMT == null) {
                DELETE_STMT = getCqlSession().prepare("DELETE FROM " + SALES_TABLE + " WHERE saleId = ?");
            }
            return DELETE_STMT;
        }

        @Override
        public PreparedStatement getStmt() {
            if (SELECT_STMT == null) {
                SELECT_STMT = getCqlSession().prepare("SELECT * FROM " + SALES_TABLE + " WHERE saleId = ?");
            }
            return SELECT_STMT;
        }

        @Override
        public Sale mapToEntity(String key, Sale sale) {
            Row row = get(key).one();

            if (row != null) {
                if (sale == null) {
                    sale = new Sale();
                }
                sale.saleId = row.getString("saleId");
                sale.productId = row.getString("productId");
                sale.quantitySold = row.getInt("quantitySold");
                sale.saleDate = row.getLong("saleDate");
                return sale;
            }
            return null;
        }
    }
}
