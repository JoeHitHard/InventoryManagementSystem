package aad.project.InventoryManagementSystem.service;

import aad.project.InventoryManagementSystem.storage.entitites.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    private final String BASE_URL = "http://localhost:8080/api/auth";

    @Autowired
    private RestTemplate restTemplate;

    public User getUser(String userId, String authorizationHeader) {
        String url = BASE_URL + "/get/" + userId;
        return restTemplate.getForObject(url, User.class);
    }

    public String logIn(String userDataString, String role) {
        String url = BASE_URL + "/public/login?role=" + role;
        return restTemplate.postForObject(url, userDataString, String.class);
    }

    public String signUp(String userDataString) {
        String url = BASE_URL + "/public/signUp";
        return restTemplate.postForObject(url, userDataString, String.class);
    }
}
