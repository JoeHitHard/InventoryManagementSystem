package aad.project.InventoryManagementSystem.storage.dao;

import aad.project.InventoryManagementSystem.storage.service.CassandraStorageService;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;


public abstract class AbstractCassandraDAO<T> implements DataAccessObject<T> {

    private final CqlSession cqlSession;

    public AbstractCassandraDAO() {
        CassandraStorageService.init();
        cqlSession = CassandraStorageService.getCqlSession();
    }

    public static CqlSession getCqlSession() {
        return CassandraStorageService.getCqlSession();
    }

    @Override
    public ResultSet createTable() {
        BoundStatement bind = getCreateStmt().bind();
        return executeStatement(bind);
    }

    private ResultSet bindAndExecute(Object[] values, PreparedStatement createStmt) {
        BoundStatement bind = createStmt.bind(values);
        return executeStatement(bind);
    }

    private ResultSet executeStatement(BoundStatement bind) {
        return cqlSession.execute(bind);
    }

    @Override
    public ResultSet insert(String key, Object... values) {
        return bindAndExecute(values, getInsertStmt());
    }

    @Override
    public ResultSet update(String key, Object... values) {
        return bindAndExecute(values, getUpdateStmt());
    }

    @Override
    public ResultSet get(String key) {
        return bindAndExecute(new String[]{key}, getStmt());
    }

    @Override
    public ResultSet delete(String key) {
        return bindAndExecute(new String[]{key}, getDeleteStmt());
    }

    @Override
    public ResultSet query(BoundStatement statement) {
        return executeStatement(statement);
    }
}
