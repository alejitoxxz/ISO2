package co.edu.uco.ucochallenge.primary.controller;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import co.edu.uco.ucochallenge.user.confirmcontact.application.service.UserContactConfirmationService;
import co.edu.uco.ucochallenge.user.findusers.application.interactor.FindUsersByFilterInteractor;
import co.edu.uco.ucochallenge.user.findusers.application.interactor.dto.FindUsersByFilterInputDTO;
import co.edu.uco.ucochallenge.user.findusers.application.interactor.dto.FindUsersByFilterOutputDTO;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.RegisterUserInteractor;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto.RegisterUserInputDTO;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto.RegisterUserResponseDTO;
import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("/uco-challenge/api/v1")
public class UserController {

    private final RegisterUserInteractor registerUserInteractor;
    private final FindUsersByFilterInteractor findUsersByFilterInteractor;
    private final UserContactConfirmationService userContactConfirmationService;

    public UserController(final RegisterUserInteractor registerUserInteractor,
                          final FindUsersByFilterInteractor findUsersByFilterInteractor,
                          final UserContactConfirmationService userContactConfirmationService) {
        this.registerUserInteractor = registerUserInteractor;
        this.findUsersByFilterInteractor = findUsersByFilterInteractor;
        this.userContactConfirmationService = userContactConfirmationService;
    }

    @PostMapping("/users")
    public ResponseEntity<RegisterUserResponseDTO> create(@Valid @RequestBody final RegisterUserInputDTO request) {
        final RegisterUserResponseDTO response = registerUserInteractor.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/users")
    public ResponseEntity<FindUsersByFilterOutputDTO> getUsers(
            @RequestParam(name = "page", required = false) final Integer page,
            @RequestParam(name = "size", required = false) final Integer size) {
        final var normalizedInput = FindUsersByFilterInputDTO.normalize(page, size);
        final var response = findUsersByFilterInteractor.execute(normalizedInput);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/users/{id}/confirm-email")
    public ResponseEntity<Void> confirmEmail(@PathVariable final UUID id) {
        userContactConfirmationService.confirmEmail(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/users/{id}/confirm-mobile")
    public ResponseEntity<Void> confirmMobile(@PathVariable final UUID id) {
        userContactConfirmationService.confirmMobile(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/users/verify-code")
    public ResponseEntity<String> verifyCode(@RequestParam String contact, @RequestParam String code) {
        boolean verified = userContactConfirmationService.verifyCode(contact, code);
        if (verified) {
            return ResponseEntity.ok("Verification successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired code");
        }
    }
}
