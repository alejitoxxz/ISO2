package co.edu.uco.ucochallenge.user.registeruser.application.interactor.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.ucochallenge.application.Void;
import co.edu.uco.ucochallenge.application.interactor.mapper.DomainMapper;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.RegisterUserInteractor;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto.RegisterUserInputDTO;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.RegisterUserUseCase;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.RegisterUserDomain;
import jakarta.transaction.Transactional;

@Transactional
@Service
public class RegisterUserInteractorImpl implements RegisterUserInteractor {

        private final RegisterUserUseCase useCase;
        private final DomainMapper<RegisterUserInputDTO, RegisterUserDomain> mapper;

        public RegisterUserInteractorImpl(final RegisterUserUseCase useCase,
                        final DomainMapper<RegisterUserInputDTO, RegisterUserDomain> mapper) {
                this.useCase = useCase;
                this.mapper = mapper;
        }

        @Override
        public Void execute(final RegisterUserInputDTO dto) {
                var registerUserDomain = mapper.toDomain(dto);
                return useCase.execute(registerUserDomain);
        }

}
