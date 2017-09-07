package db;

import org.postgresql.ds.PGPoolingDataSource;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


public class PostgreDataSourceProvider implements Provider<DataSource> {

    private final PgConfig pgConfig;

    @Inject
    public PostgreDataSourceProvider(PgConfig pgConfig) {
        this.pgConfig = pgConfig;
    }


    @Override
    public DataSource get() {
        final PGPoolingDataSource ds = new PGPoolingDataSource();
        ds.setServerName(pgConfig.getHost());
        ds.setPortNumber(pgConfig.getPort());
        ds.setDatabaseName(pgConfig.getDbName());
        ds.setUser(pgConfig.getUsername());
        ds.setPassword(pgConfig.getPassword());
        ds.setMaxConnections(4);
        ds.setInitialConnections(2);
        return ds;
    }
}
