package co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.ucochallenge.application.Void;
import co.edu.uco.ucochallenge.crosscuting.exception.BusinessException;
import co.edu.uco.ucochallenge.crosscuting.notification.Notification;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.RegisterUserUseCase;
import co.edu.uco.ucochallenge.user.registeruser.application.port.ContactConfirmationPort;
import co.edu.uco.ucochallenge.user.registeruser.application.port.NotificationPort;
import co.edu.uco.ucochallenge.user.registeruser.application.port.RegisterUserRepositoryPort;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.RegisterUserDomain;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.validation.RegisterUserDomainValidator;

@Service
public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

        private static final String EXECUTOR_IDENTIFIER = "REGISTER_USER_USE_CASE";

        private final RegisterUserRepositoryPort repositoryPort;
        private final ContactConfirmationPort contactConfirmationPort;
        private final RegisterUserDomainValidator validator;

        public RegisterUserUseCaseImpl(final RegisterUserRepositoryPort repositoryPort,
                        final NotificationPort notificationPort,
                        final ContactConfirmationPort contactConfirmationPort) {
                this.repositoryPort = repositoryPort;
                this.contactConfirmationPort = contactConfirmationPort;
                this.validator = new RegisterUserDomainValidator(repositoryPort, notificationPort, UUID::randomUUID);
        }

        @Override
        public Void execute(final RegisterUserDomain domain) {
                final Notification notification = validator.validate(domain, EXECUTOR_IDENTIFIER);
                if (notification.hasErrors()) {
                        throw new BusinessException(notification.formattedMessages());
                }

                repositoryPort.save(domain);

                if (domain.hasEmail()) {
                        contactConfirmationPort.confirmEmail(domain.getEmail());
                        domain.markEmailConfirmed();
                }

                if (domain.hasMobileNumber()) {
                        contactConfirmationPort.confirmMobileNumber(domain.getMobileNumber());
                        domain.markMobileNumberConfirmed();
                }

                return Void.returnVoid();
        }
}
