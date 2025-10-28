package co.edu.uco.ucochallenge.primary.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.ucochallenge.user.registeruser.application.interactor.RegisterUserInteractor;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto.RegisterUserInputDTO;
import co.edu.uco.ucochallenge.user.findusers.application.interactor.FindUsersByFilterInteractor;
import co.edu.uco.ucochallenge.user.findusers.application.interactor.dto.FindUsersByFilterInputDTO;
import co.edu.uco.ucochallenge.user.findusers.application.interactor.dto.FindUsersByFilterOutputDTO;

@RestController
@RequestMapping("/uco-challenge/api/v1")
public class UserController {

        private final RegisterUserInteractor registerUserInteractor;
        private final FindUsersByFilterInteractor findUsersByFilterInteractor;

        public UserController(final RegisterUserInteractor registerUserInteractor,
                        final FindUsersByFilterInteractor findUsersByFilterInteractor) {
                this.registerUserInteractor = registerUserInteractor;
                this.findUsersByFilterInteractor = findUsersByFilterInteractor;
        }

        @PostMapping
        public ResponseEntity<String> registerUser(@RequestBody RegisterUserInputDTO dto) {
		var message = "User registered successfully";
		var nomalizeDto = RegisterUserInputDTO.normalize(dto.idType(), dto.idNumber(), dto.firstName(), dto.secondName(), dto.firstSurname(), dto.secondSurname(), dto.homeCity(), dto.email(), dto.mobileNumber());
                registerUserInteractor.execute(nomalizeDto);
                return new ResponseEntity<>(message, HttpStatus.CREATED);
        }

        @GetMapping("/users")
        public ResponseEntity<FindUsersByFilterOutputDTO> getUsers(@RequestParam(name = "page", required = false) final Integer page,
                        @RequestParam(name = "size", required = false) final Integer size) {
                final var normalizedInput = FindUsersByFilterInputDTO.normalize(page, size);
                final var response = findUsersByFilterInteractor.execute(normalizedInput);
                return ResponseEntity.ok(response);
        }
}
