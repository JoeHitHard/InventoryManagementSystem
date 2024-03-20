package aad.project.InventoryManagementSystem.controller;

import aad.project.InventoryManagementSystem.config.scurity.exceptions.InvalidAuthRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/health")
public class HealthController {

    // Retrieve all products
    @GetMapping
    public ResponseEntity<List<String>> health() throws InvalidAuthRequest {
        return new ResponseEntity<>(List.of("healthy"), HttpStatus.OK);
    }
}
