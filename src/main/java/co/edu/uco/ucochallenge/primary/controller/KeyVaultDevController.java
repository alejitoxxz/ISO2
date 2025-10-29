package co.edu.uco.ucochallenge.primary.controller;

import co.edu.uco.ucochallenge.crosscutting.secret.SecretProvider;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simple controller to validate Key Vault integration only on the dev profile.
 */
@RestController
@Profile("dev")
@RequestMapping("/dev/kv")
public class KeyVaultDevController {

    private final SecretProvider secretProvider;

    public KeyVaultDevController(SecretProvider secretProvider) {
        this.secretProvider = secretProvider;
    }

    @GetMapping("/{name}")
    public ResponseEntity<String> getSecret(@PathVariable String name) {
        return ResponseEntity.ok(secretProvider.get(name));
    }
}
