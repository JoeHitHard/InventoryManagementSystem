package aad.project.InventoryManagementSystem.utils.storage.entity;

import aad.project.InventoryManagementSystem.storage.entity.Sale;
import aad.project.InventoryManagementSystem.storage.requests.SaleRequestBody;

public class SaleUtils {
    public static Sale getSale(String id, SaleRequestBody saleRequestBody) {
        return new Sale(id, saleRequestBody.getProductId(), saleRequestBody.getQuantitySold(), saleRequestBody.getSaleDate());
    }
}
