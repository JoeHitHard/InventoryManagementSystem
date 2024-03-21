package aad.project.InventoryManagementSystem.storage.requests;

public class SaleRequestBody {

    private String productId;
    private int quantitySold;
    private Long saleDate;

    // Constructor, getters, and setters


    public SaleRequestBody() {
    }

    public SaleRequestBody(String productId, int quantitySold, Long saleDate) {
        this.productId = productId;
        this.quantitySold = quantitySold;
        this.saleDate = saleDate;
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
}
