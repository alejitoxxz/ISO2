package co.edu.uco.ucochallenge.user.registeruser.application.interactor.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.ucochallenge.application.Void;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.RegisterUserInteractor;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto.RegisterUserInputDTO;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.RegisterUserUseCase;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.RegisterUserDomain;
import jakarta.transaction.Transactional;

@Transactional
@Service
public class RegisterUserInteractorImpl implements RegisterUserInteractor {

        private final RegisterUserUseCase useCase;

        public RegisterUserInteractorImpl(final RegisterUserUseCase useCase) {
                this.useCase = useCase;
        }

        @Override
        public Void execute(final RegisterUserInputDTO dto) {
                var registerUserDomain = RegisterUserDomain.builder()
                                .id(UUID.randomUUID())
                                .idType(dto.idType())
                                .idNumber(dto.idNumber())
                                .firstName(dto.firstName())
                                .secondName(dto.secondName())
                                .firstSurname(dto.firstSurname())
                                .secondSurname(dto.secondSurname())
                                .homeCity(dto.homeCity())
                                .email(dto.email())
                                .mobileNumber(dto.mobileNumber())
                                .build();

                return useCase.execute(registerUserDomain);
        }

}
