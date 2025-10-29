package co.edu.uco.ucochallenge.crosscutting.secret;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Example component showing how any domain service could consume secrets.
 * The method demonstrateUsage can be wired wherever needed without changing business logic.
 */
@Component
@Profile("dev")
public class DomainSecretUsageExample {

    private final SecretProvider secretProvider;

    public DomainSecretUsageExample(SecretProvider secretProvider) {
        this.secretProvider = secretProvider;
    }

    public String demonstrateUsage() {
        String password = secretProvider.get("db-password");
        // Domain service logic would use the password variable here (e.g., building a connection string).
        return password;
    }
}
