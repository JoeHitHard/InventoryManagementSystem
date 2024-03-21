package aad.project.InventoryManagementSystem.storage.entitites;

import aad.project.InventoryManagementSystem.storage.dao.AbstractCassandraDAO;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Token {

    public static final String TOKENS_TABLE = "tokens";
    public static final String TOKEN_TTL = "3600";

    static {
        TokensDAO tokensDAO = new TokensDAO();
        tokensDAO.createTable();
    }

    transient TokensDAO tokensDAO;
    private String userId;
    private String jwtToken;
    private UserRole userRole = null;

    public Token() {
        this.tokensDAO = new TokensDAO();
    }

    public Token(String token) {
        this();
        this.jwtToken = token;
        tokensDAO.mapToEntity(token, this);
    }

    public Token(String userId, String jwtToken, UserRole userRole) {
        this();
        this.userId = userId;
        this.jwtToken = jwtToken;
        this.userRole = userRole;
    }

    public static List<Token> getAllTokens() {
        TokensDAO tokensDAO = new TokensDAO();
        return tokensDAO.getAllTokens();
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("userId", this.userId);
        json.put("jwtToken", this.jwtToken);
        json.put("userRole", this.userRole.toString());
        return json;
    }

    public Token save() {
        tokensDAO.insert(jwtToken, jwtToken, userId, userRole.toString());
        return this;
    }

    public Token delete() {
        tokensDAO.delete(jwtToken);
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Token.class.getSimpleName() + "[", "]")
                .add("userId='" + userId + "'")
                .add("jwtToken='" + jwtToken + "'")
                .add("userRole=" + userRole)
                .toString();
    }

    public enum UserRole {
        Admin, User
    }

    public static class TokensDAO extends AbstractCassandraDAO<Token> {

        public static final PreparedStatement CREATE_STMT = getCqlSession().prepare("CREATE TABLE IF NOT EXISTS " + TOKENS_TABLE + " " + "(userId TEXT, jwtToken TEXT PRIMARY KEY, userRole TEXT)");

        public static PreparedStatement INSERT_STMT;

        public static PreparedStatement DELETE_STMT;

        public static PreparedStatement SELECT_STMT;

        @Override
        public PreparedStatement getCreateStmt() {
            return CREATE_STMT;
        }

        @Override
        public PreparedStatement getInsertStmt() {
            if (INSERT_STMT == null) {
                INSERT_STMT = getCqlSession().prepare("INSERT INTO " + TOKENS_TABLE +
                        " (jwtToken, userId, userRole) VALUES (?, ?, ?) USING TTL " + TOKEN_TTL);
            }
            return INSERT_STMT;
        }

        @Override
        public PreparedStatement getUpdateStmt() {
            return null;
        }

        @Override
        public PreparedStatement getDeleteStmt() {
            if (DELETE_STMT == null) {
                DELETE_STMT = getCqlSession().prepare("DELETE FROM " + TOKENS_TABLE + " WHERE jwtToken = ?");
            }
            return DELETE_STMT;
        }

        @Override
        public PreparedStatement getStmt() {
            if (SELECT_STMT == null) {
                SELECT_STMT = getCqlSession().prepare("SELECT * FROM " + TOKENS_TABLE + " WHERE jwtToken = ?");
            }
            return SELECT_STMT;
        }

        @Override
        public Token mapToEntity(String key, Token token) {
            ResultSet boundStatement = get(key);
            if (boundStatement != null) {
                Row row = boundStatement.one();
                if (row != null) {
                    if (token == null) {
                        token = new Token();
                    }
                    token.userId = row.getString("userId");
                    token.jwtToken = row.getString("jwtToken");
                    token.userRole = UserRole.valueOf(row.getString("userRole"));
                    return token;
                }
            }
            return null;
        }

        public List<Token> getAllTokens() {
            List<Token> tokenList = new ArrayList<>();
            BoundStatement boundStatement = getCqlSession().prepare("SELECT * FROM " + TOKENS_TABLE).bind();
            getCqlSession().execute(boundStatement).forEach(row -> {
                Token token = new Token();
                token.userId = row.getString("userId");
                token.jwtToken = row.getString("jwtToken");
                token.userRole = UserRole.valueOf(row.getString("userRole"));
                tokenList.add(token);
            });
            return tokenList;
        }
    }
}
