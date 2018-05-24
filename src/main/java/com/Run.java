package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

/**
 * I denne klasse ligger metoden main() der starter hele programmet.
 * @author Mikkel
 */
@SpringBootApplication
public class Run {

    public static void main(String[] args) {
        SpringApplication.run(Run.class, args);
    }

    /**
     * Her bliver datasource defineret, og henter info omkring databasen fra application.properties.
     * Datasource bliver brugt til at Autowire JDBCTemplate i alle klasserne der har forbindelse til databasen.
     */
    @Bean(value = "datasource")
    @ConfigurationProperties("app.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }
}