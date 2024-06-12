package hdm.stuttgart.geekslist.Database;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Bean()
    @Primary
    public DataSource dataSource() {
        String postgres = System.getenv("GEEKSLIST_ConnectionStrings__BackendDb");

        var builder = DataSourceBuilder.create();
        if (postgres != null && !postgres.trim().isEmpty()) {
            builder.url("jdbc:postgresql://"+ postgres);
        } else {
            builder.driverClassName("org.sqlite.JDBC"); // aparently there might be issues if not set explicitly
            builder.url("jdbc:sqlite://data/geekslist.db");
        }

        return builder.build();
    }

}

