package co.edu.uco.ucochallenge.crosscutting.secret;

import com.azure.core.exception.HttpResponseException;
import com.azure.core.exception.ResourceNotFoundException;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Default implementation of SecretProvider backed by Azure Key Vault.
 */
@Service
public class KeyVaultService implements SecretProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeyVaultService.class);
    private final SecretClient secretClient;

    public KeyVaultService(SecretClient secretClient) {
        this.secretClient = secretClient;
    }

    public String getSecretValue(String name) {
        return tryGetSecretValue(name)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Secret '" + name + "' was not found in Azure Key Vault."));
    }

    public Optional<String> tryGetSecretValue(String name) {
        try {
            KeyVaultSecret secret = secretClient.getSecret(name);
            return Optional.ofNullable(secret.getValue());
        } catch (ResourceNotFoundException notFound) {
            LOGGER.warn("Secret '{}' was not found in Azure Key Vault.", name);
            return Optional.empty();
        } catch (HttpResponseException httpException) {
            throw new IllegalStateException(
                    "Failed to retrieve secret '" + name + "' from Azure Key Vault. Response code: "
                            + httpException.getResponse().getStatusCode(),
                    httpException);
        }
    }

    @Override
    public String get(String name) {
        return getSecretValue(name);
    }
}
