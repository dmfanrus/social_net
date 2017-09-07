package db;


import javax.inject.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PgConfigProvider implements Provider<PgConfig> {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PgConfigProvider.class);

    @Override
    public PgConfig get() {
        final Properties properties = new Properties();
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("cfg/postgres.properties")){
            properties.load(stream);
        }catch (IOException e){
            log.error("Failed to load file of properties",e);
//            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return new PgConfig() {
            @Override
            public String getHost() {
                return properties.getProperty("postgres.host");
            }

            @Override
            public int getPort() {
                return Integer.parseInt(properties.getProperty("postgres.port"));

            }

            @Override
            public String getDbName() {
                return properties.getProperty("postgres.dbname");

            }

            @Override
            public String getUsername() {
                return properties.getProperty("postgres.username");

            }

            @Override
            public String getPassword() {
                return properties.getProperty("postgres.password");

            }
        };
    }
}
