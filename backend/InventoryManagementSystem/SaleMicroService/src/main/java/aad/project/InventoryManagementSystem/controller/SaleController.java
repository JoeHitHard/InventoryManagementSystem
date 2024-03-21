package aad.project.InventoryManagementSystem.controller;

import aad.project.InventoryManagementSystem.config.scurity.exceptions.InvalidAuthRequest;
import aad.project.InventoryManagementSystem.storage.entity.Sale;
import aad.project.InventoryManagementSystem.storage.entity.User;
import aad.project.InventoryManagementSystem.storage.requests.SaleRequestBody;
import aad.project.InventoryManagementSystem.utils.storage.entity.RequestAuthUtils;
import aad.project.InventoryManagementSystem.utils.storage.entity.SaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sales")
public class SaleController {
    private static final Logger logger = LoggerFactory.getLogger(SaleController.class);


    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping
    public ResponseEntity<Sale> recordSale(@RequestBody String saleJson, @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws InvalidAuthRequest {
        User user = RequestAuthUtils.getUser(authorizationHeader);
        try {
            SaleRequestBody saleRequestBody = objectMapper.readValue(saleJson, SaleRequestBody.class);
            Sale sale = SaleUtils.getSale(UUID.randomUUID().toString(), saleRequestBody);
            Sale createdSale = sale.save();
            return new ResponseEntity<>(createdSale, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sale> getSaleById(@PathVariable String id, @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws InvalidAuthRequest {
        User user = RequestAuthUtils.getUser(authorizationHeader);
        Sale sale = new Sale(id);
        if (sale.getSaleId() != null) {
            return new ResponseEntity<>(sale, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Sale>> getAllSales( @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws InvalidAuthRequest {
        User user = RequestAuthUtils.getUser(authorizationHeader);
        List<Sale> sales = Sale.SalesDAO.getAllSales();
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sale> updateSale(@PathVariable String id, @RequestBody String saleJson,  @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws InvalidAuthRequest {
        User user = RequestAuthUtils.getUser(authorizationHeader);
        try {
            SaleRequestBody saleRequestBody = objectMapper.readValue(saleJson, SaleRequestBody.class);
            Sale sale = SaleUtils.getSale(id, saleRequestBody);
            Sale updatedSale = sale.update();
            return new ResponseEntity<>(updatedSale, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("{}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable String id, @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws InvalidAuthRequest {
        User user = RequestAuthUtils.getUser(authorizationHeader);
        Sale sale = new Sale(id);
        try {
            if (sale.getSaleId() != null) {
                sale.delete();
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Error deleting sale: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
