package co.edu.uco.ucochallenge.crosscutting.secret;

/**
 * Abstraction that exposes secret resolution regardless of the underlying vault.
 */
public interface SecretProvider {
    String get(String name);
}
