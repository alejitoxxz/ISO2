package co.edu.uco.ucochallenge.crosscutting.config;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures the Azure Key Vault SecretClient using DefaultAzureCredential.
 */
@Configuration
public class KeyVaultConfig {

    @Bean
    public SecretClient secretClient(@Value("${azure.keyvault.url}") String vaultUrl) {
        return new SecretClientBuilder()
                .vaultUrl(vaultUrl)
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();
    }
}
