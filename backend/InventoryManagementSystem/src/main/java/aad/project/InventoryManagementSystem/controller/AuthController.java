package aad.project.InventoryManagementSystem.controller;

import aad.project.InventoryManagementSystem.config.scurity.exceptions.InvalidAuthRequest;
import aad.project.InventoryManagementSystem.storage.entitites.User;
import aad.project.InventoryManagementSystem.utils.storage.entity.RequestAuthUtils;
import aad.project.InventoryManagementSystem.utils.storage.entity.UserUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @GetMapping("/get/{userID}")
    @ResponseBody
    public String getUser(@PathVariable("userID") String userID,
                          @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws Exception {
        User user = RequestAuthUtils.getUser(authorizationHeader);
        return UserUtils.toJson(user).toString();
    }

    @PostMapping("/public/login")
    @ResponseBody
    public String logIn(@RequestBody String userDataString, @RequestParam(value = "role", defaultValue = "User") String role) throws Exception {
        return RequestAuthUtils.login(new JSONObject(userDataString), role);
    }


    @PostMapping("/public/signUp")
    @ResponseBody
    public String signUp(@RequestBody String userDataString) throws Exception {
        User user = UserUtils.getUser(new JSONObject(userDataString));
        user.save();
        return UserUtils.toJson(user).toString();
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ErrorResponse errorResponse = ErrorResponse.builder(ex, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidAuthRequest.class)
    public ResponseEntity<ErrorResponse> handleAuthException(Exception ex) {
        ErrorResponse errorResponse = ErrorResponse.builder(ex, HttpStatus.UNAUTHORIZED, ex.getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
}
