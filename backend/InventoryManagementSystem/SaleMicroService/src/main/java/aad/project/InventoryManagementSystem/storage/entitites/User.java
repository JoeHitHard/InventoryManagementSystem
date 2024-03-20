package aad.project.InventoryManagementSystem.storage.entitites;


import aad.project.InventoryManagementSystem.storage.dao.AbstractCassandraDAO;
import aad.project.InventoryManagementSystem.utils.storage.entity.UserUtils;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.Row;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class User {

    public static final String USERS_TABLE = "users";
    public static boolean DEV = false;

    static {
        UsersDAO usersDAO = new UsersDAO();
        usersDAO.createTable();
    }

    transient UsersDAO usersDAO;
    private String userId;
    private String name;
    private String emailId;
    private String password;
    private Role role;
    private long lastSeen;

    public User() {
        this.usersDAO = new UsersDAO();
    }

    public User(String userId) {
        this();
        this.userId = userId;
        usersDAO.mapToEntity(userId, this);
    }

    public User(String userId, String name, String emailId, Role role, long lastSeen, String password) {
        this();
        this.userId = userId;
        UserUtils.assignVariables(this, name, emailId, role, lastSeen, password);
    }


    public User save() {
        usersDAO.insert(userId, userId, name, emailId, role.toString(), lastSeen, password);
        return this;
    }

    public User delete() {
        usersDAO.delete(userId);
        return this;
    }

    public User update() {
        usersDAO.update(userId, userId, name, emailId, role.toString(), lastSeen, password);
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(long lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean validate(String password) {
        return password.equals(this.password);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("userId='" + userId + "'")
                .add("name='" + name + "'")
                .add("emailId='" + emailId + "'")
                .add("role=" + role)
                .add("lastSeen=" + lastSeen)
                .add("password=" + (DEV ? password : "HIDDEN(enable DEV)"))
                .toString();
    }

    public enum Role {
        admin, employee
    }

    public static class UsersDAO extends AbstractCassandraDAO<User> {

        public static final PreparedStatement CREATE_STMT = getCqlSession().prepare("CREATE TABLE IF NOT EXISTS " +
                USERS_TABLE + " " + "(userId TEXT PRIMARY KEY, name TEXT, emailId TEXT, role TEXT, lastSeen BIGINT, password TEXT)");
        public static PreparedStatement INSERT_STMT;

        public static PreparedStatement UPDATE_STMT;

        public static PreparedStatement DELETE_STMT;

        public static PreparedStatement SELECT_STMT;

        public static List<User> getAllUsers() {
            List<User> userList = new ArrayList<>();
            BoundStatement boundStatement = getCqlSession().prepare("SELECT * FROM " + USERS_TABLE).bind();
            getCqlSession().execute(boundStatement).forEach(row -> {
                User user = new User();
                user.userId = row.getString("userId");
                UserUtils.assignVariables(user, row.getString("name"), row.getString("emailId"),
                        Role.valueOf(row.getString("role")), row.getLong("lastSeen"),
                        "HIDDEN");
                userList.add(user);
            });
            return userList;
        }

        @Override
        public PreparedStatement getCreateStmt() {
            return CREATE_STMT;
        }

        @Override
        public PreparedStatement getInsertStmt() {
            if (INSERT_STMT == null) {
                INSERT_STMT = getCqlSession().prepare("INSERT INTO " + USERS_TABLE
                        + " (userId, name, emailId, role, lastSeen, password) VALUES (?, ?, ?, ?, ?, ?)");
            }
            return INSERT_STMT;
        }

        @Override
        public PreparedStatement getUpdateStmt() {
            if (UPDATE_STMT == null) {
                UPDATE_STMT = getCqlSession().prepare("UPDATE " + USERS_TABLE
                        + " SET name = ?, emailId = ?, role = ?, lastSeen = ?, password = ? WHERE userId = ?");
            }
            return UPDATE_STMT;
        }

        @Override
        public PreparedStatement getDeleteStmt() {
            if (DELETE_STMT == null) {
                DELETE_STMT = getCqlSession().prepare("DELETE FROM " + USERS_TABLE + " WHERE userId = ?");
            }
            return DELETE_STMT;
        }

        @Override
        public PreparedStatement getStmt() {
            if (SELECT_STMT == null) {
                SELECT_STMT = getCqlSession().prepare("SELECT * FROM " + USERS_TABLE + " WHERE userId = ?");
            }
            return SELECT_STMT;
        }

        @Override
        public User mapToEntity(String key, User user) {
            Row row = get(key).one();

            if (row != null) {
                if (user == null) {
                    user = new User();
                }
                user.userId = row.getString("userId");
                UserUtils.assignVariables(user, row.getString("name"), row.getString("emailId"),
                        Role.valueOf(row.getString("role")), row.getLong("lastSeen"),
                        row.getString("password"));
                return user;
            }
            return null;
        }
    }
}
