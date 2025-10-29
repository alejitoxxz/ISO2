package co.edu.uco.ucochallenge.crosscutting.config;

import co.edu.uco.ucochallenge.crosscutting.secret.SecretProvider;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Example on how to bind a DataSource password from Key Vault.
 * Callers may annotate {@link #dataSourceWithSecret(DataSourceProperties, SecretProvider)} with {@code @Bean}
 * when the application needs to bootstrap a DataSource using the vault secret.
 */
@Configuration
public class KeyVaultDataSourceConfigExample {

    protected DataSource dataSourceWithSecret(
            DataSourceProperties properties, SecretProvider secretProvider) {
        String password = secretProvider.get("db-password");
        return properties
                .initializeDataSourceBuilder()
                .password(password)
                .build();
    }
}
