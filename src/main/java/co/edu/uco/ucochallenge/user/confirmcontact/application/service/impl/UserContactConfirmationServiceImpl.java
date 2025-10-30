package co.edu.uco.ucochallenge.user.confirmcontact.application.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.ucochallenge.crosscuting.exception.NotFoundException;
import co.edu.uco.ucochallenge.user.confirmcontact.application.port.ConfirmUserContactRepositoryPort;
import co.edu.uco.ucochallenge.user.confirmcontact.application.service.UserContactConfirmationService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserContactConfirmationServiceImpl implements UserContactConfirmationService {

        private static final String USER_NOT_FOUND_MESSAGE = "User not found";

        private final ConfirmUserContactRepositoryPort repositoryPort;

        public UserContactConfirmationServiceImpl(final ConfirmUserContactRepositoryPort repositoryPort) {
                this.repositoryPort = repositoryPort;
        }

        @Override
        public void confirmEmail(final UUID id) {
                final boolean updated = repositoryPort.confirmEmail(id);
                if (!updated) {
                        throw new NotFoundException(USER_NOT_FOUND_MESSAGE);
                }
        }

        @Override
        public void confirmMobile(final UUID id) {
                final boolean updated = repositoryPort.confirmMobileNumber(id);
                if (!updated) {
                        throw new NotFoundException(USER_NOT_FOUND_MESSAGE);
                }
        }
}
