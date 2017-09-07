package db;

/**
 * Created by Михаил on 08.01.2017.
 */
public interface PgConfig {
    String getHost();

    int getPort();

    String getDbName();

    String getUsername();

    String getPassword();
}
