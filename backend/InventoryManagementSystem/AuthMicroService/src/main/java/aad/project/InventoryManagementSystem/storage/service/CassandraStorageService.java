package aad.project.InventoryManagementSystem.storage.service;

import aad.project.InventoryManagementSystem.config.props.AppProperties;
import aad.project.InventoryManagementSystem.controller.AuthController;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.metadata.EndPoint;
import com.datastax.oss.driver.internal.core.channel.ClusterNameMismatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component
public class CassandraStorageService {
    private static CqlSession cqlSession;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    public static String getKEYSPACE() {
        return AppProperties.CASS_KEYSPACE;
    }

    public static void init() {
        if (cqlSession == null) {
            try {
                cqlSession = CqlSession.builder().withKeyspace(getKEYSPACE())
                        .withLocalDatacenter("datacenter1")
                        .addContactPoint(new InetSocketAddress("localhost", 9042)).build();
            } catch (Exception e) {
                logger.error("{}", e.getCause(), e);
                throw e;
            }
        }
    }

    public static CqlSession getCqlSession() {
        if (cqlSession == null) {
            init();
        }
        return cqlSession;
    }

    public static void main(String[] args) {
        init();
        ResultSet execute = cqlSession.execute("Select * from users");
        for (Row row : execute) {
            System.out.println(row.getFormattedContents());
        }
    }

}
