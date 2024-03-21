package aad.project.InventoryManagementSystem.utils.storage.entity;

import aad.project.InventoryManagementSystem.storage.entitites.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class UserUtils {
    public static void assignVariables(User entity, String name, String emailId,
                                       User.Role role, long lastSeen, String password) {
        entity.setName(name);
        entity.setEmailId(emailId);
        entity.setRole(role);
        entity.setLastSeen(lastSeen);
        entity.setPassword(password);
    }

    public static List<User> getAllUsers() {
        return User.UsersDAO.getAllUsers();
    }

    public static User getUser(JSONObject userJson) throws Exception {
        String userId = userJson.getString("userId");
        User existingUser = new User(userId);
        if (existingUser.getRole() != null) {
            throw new Exception("User ID " + userId + " already exists");
        }
        return new User(userId,
                userJson.getString("name"),
                userJson.getString("emailId"),
                User.Role.valueOf(userJson.getString("role")),
                System.currentTimeMillis() / 1000, userJson.getString("password"));
    }

    public static JSONObject toJson(User user) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("userId", user.getUserId());
        json.put("name", user.getName());
        json.put("emailId", user.getEmailId());
        json.put("role", user.getRole().toString());
        json.put("lastSeen", user.getLastSeen());
        return json;
    }
}
