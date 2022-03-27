package br.com.monktec;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.HashMap;
import java.util.Map;

public class CadastroTestLifecycleManager implements QuarkusTestResourceLifecycleManager {

    public static final String JDBC_URL = "quarkus.datasource.jdbc.url";
    public static final String DATASOURCE_USERNAME = "quarkus.datasource.username";
    public static final String DATASOURCE_PASSWORD = "quarkus.datasource.password";

    private static PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer("postgres:12.2");

    @Override
    public Map<String, String> start() {
        POSTGRES.start();

        Map<String,String> propriedades = new HashMap<>();

        propriedades.put(JDBC_URL, POSTGRES.getJdbcUrl());
        propriedades.put(DATASOURCE_USERNAME, POSTGRES.getUsername());
        propriedades.put(DATASOURCE_PASSWORD, POSTGRES.getPassword());

        return propriedades;
    }

    @Override
    public void stop() {
        if (POSTGRES != null) {
            POSTGRES.stop();
        }
    }
}
