package co.edu.uco.ucochallenge.user.confirmcontact.application.service.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.ucochallenge.crosscuting.exception.BusinessException;
import co.edu.uco.ucochallenge.crosscuting.exception.NotFoundException;
import co.edu.uco.ucochallenge.secondary.adapters.repository.jpa.VerificationCodeRepository;
import co.edu.uco.ucochallenge.user.confirmcontact.application.port.ConfirmUserContactRepositoryPort;
import co.edu.uco.ucochallenge.user.confirmcontact.application.service.UserContactConfirmationService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserContactConfirmationServiceImpl implements UserContactConfirmationService {

        private static final String USER_NOT_FOUND_MESSAGE = "User not found";

        private final ConfirmUserContactRepositoryPort userRepository;
        private final VerificationCodeRepository codeRepository;

        public UserContactConfirmationServiceImpl(final ConfirmUserContactRepositoryPort userRepository,
                        final VerificationCodeRepository codeRepository) {
                this.userRepository = userRepository;
                this.codeRepository = codeRepository;
        }

        @Override
        public void confirmEmail(final UUID id) {
                final boolean updated = userRepository.confirmEmail(id);
                if (!updated) {
                        throw new NotFoundException(USER_NOT_FOUND_MESSAGE);
                }
        }

        @Override
        public void confirmMobile(final UUID id) {
                final boolean updated = userRepository.confirmMobileNumber(id);
                if (!updated) {
                        throw new NotFoundException(USER_NOT_FOUND_MESSAGE);
                }
        }

        @Override
        @Transactional
        public void verify(final String contact, final String code) {
                final var stored = codeRepository.findByContact(contact)
                                .filter(entity -> entity.getCode().equals(code)
                                                && entity.getExpiration().isAfter(LocalDateTime.now()))
                                .orElseThrow(() -> new BusinessException("user.contact.verification.invalid"));

                userRepository.confirmEmailOrMobile(contact);
                codeRepository.delete(stored);
        }
}
