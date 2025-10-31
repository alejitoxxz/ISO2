package co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.impl;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.ucochallenge.crosscuting.exception.BusinessException;
import co.edu.uco.ucochallenge.crosscuting.exception.DomainValidationException;
import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;
import co.edu.uco.ucochallenge.crosscuting.helper.UUIDHelper;
import co.edu.uco.ucochallenge.secondary.ports.repository.CityRepository;
import co.edu.uco.ucochallenge.secondary.ports.repository.CountryRepository;
import co.edu.uco.ucochallenge.secondary.ports.repository.IdTypeRepository;
import co.edu.uco.ucochallenge.secondary.ports.repository.StateRepository;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.RegisterUserUseCase;
import co.edu.uco.ucochallenge.user.registeruser.application.port.ContactConfirmationPort;
import co.edu.uco.ucochallenge.user.registeruser.application.port.RegisterUserRepositoryPort;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.RegisterUserDomain;

@Service
@Transactional
public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

        private static final Logger LOGGER = LoggerFactory.getLogger(RegisterUserUseCaseImpl.class);
        private static final int MAX_ID_GENERATION_ATTEMPTS = 5;

        private final RegisterUserRepositoryPort repositoryPort;
        private final ContactConfirmationPort contactConfirmationPort;
        private final IdTypeRepository idTypeRepository;
        private final CountryRepository countryRepository;
        private final StateRepository stateRepository;
        private final CityRepository cityRepository;

        public RegisterUserUseCaseImpl(final RegisterUserRepositoryPort repositoryPort,
                        final ContactConfirmationPort contactConfirmationPort,
                        final IdTypeRepository idTypeRepository,
                        final CountryRepository countryRepository,
                        final StateRepository stateRepository,
                        final CityRepository cityRepository) {
                this.repositoryPort = repositoryPort;
                this.contactConfirmationPort = contactConfirmationPort;
                this.idTypeRepository = idTypeRepository;
                this.countryRepository = countryRepository;
                this.stateRepository = stateRepository;
                this.cityRepository = cityRepository;
        }

        @Override
        public RegisterUserDomain execute(final RegisterUserDomain domain) {
                ensureContactInformation(domain);
                resolveIdentificationType(domain);
                validateLocation(domain);
                ensureUniqueUserId(domain);
                ensureUniqueIdentification(domain);
                ensureUniqueEmail(domain);
                ensureUniqueMobile(domain);

                repositoryPort.save(domain);

                sendConfirmations(domain);

                return domain;
        }

        private void ensureContactInformation(final RegisterUserDomain domain) {
                if (!domain.hasEmail() && !domain.hasMobileNumber()) {
                        throw new DomainValidationException("register.user.validation.contact.required");
                }
        }

        private void resolveIdentificationType(final RegisterUserDomain domain) {
                if (!UUIDHelper.getDefault().equals(domain.getIdType())) {
                        if (!idTypeRepository.existsById(domain.getIdType())) {
                                throw new DomainValidationException("register.user.validation.idtype.required");
                        }
                        return;
                }

                if (TextHelper.isEmpty(domain.getIdTypeCode())) {
                        throw new DomainValidationException("register.user.validation.idtype.required");
                }

                final var idType = idTypeRepository.findByCode(domain.getIdTypeCode())
                                .orElseThrow(() -> new DomainValidationException("register.user.validation.idtype.required"));

                domain.updateIdType(idType.getId());
        }

        private void validateLocation(final RegisterUserDomain domain) {
                if (isMissing(domain.getCountryId())
                                || !countryRepository.existsById(domain.getCountryId())) {
                        throw new DomainValidationException("register.user.validation.country.required");
                }

                if (isMissing(domain.getDepartmentId())
                                || !stateRepository.existsById(domain.getDepartmentId())) {
                        throw new DomainValidationException("register.user.validation.department.required");
                }

                if (isMissing(domain.getHomeCity())
                                || !cityRepository.existsById(domain.getHomeCity())) {
                        throw new DomainValidationException("register.user.validation.city.required");
                }
        }

        private void ensureUniqueUserId(final RegisterUserDomain domain) {
                UUID candidateId = domain.getId();
                int attempts = 0;

                while (repositoryPort.existsById(candidateId) && attempts < MAX_ID_GENERATION_ATTEMPTS) {
                        candidateId = UUID.randomUUID();
                        attempts++;
                }

                if (repositoryPort.existsById(candidateId)) {
                        LOGGER.warn("Unable to generate a unique user id after {} attempts", attempts);
                        throw new BusinessException("register.user.identifier.unavailable");
                }

                if (!candidateId.equals(domain.getId())) {
                        domain.updateId(candidateId);
                }
        }

        private void ensureUniqueIdentification(final RegisterUserDomain domain) {
                repositoryPort.findByIdentification(domain.getIdType(), domain.getIdNumber())
                                .ifPresent(existing -> {
                                        throw new BusinessException("register.user.identification.duplicated");
                                });
        }

        private void ensureUniqueEmail(final RegisterUserDomain domain) {
                if (!domain.hasEmail()) {
                        return;
                }

                repositoryPort.findByEmail(domain.getEmail())
                                .ifPresent(existing -> {
                                        throw new BusinessException("register.user.email.duplicated");
                                });
        }

        private void ensureUniqueMobile(final RegisterUserDomain domain) {
                if (!domain.hasMobileNumber()) {
                        return;
                }

                repositoryPort.findByMobileNumber(domain.getMobileNumber())
                                .ifPresent(existing -> {
                                        throw new BusinessException("register.user.mobile.duplicated");
                                });
        }

        private void sendConfirmations(final RegisterUserDomain domain) {
                if (domain.hasEmail()) {
                        contactConfirmationPort.confirmEmail(domain.getEmail());
                        domain.markEmailConfirmed();
                }

                if (domain.hasMobileNumber()) {
                        contactConfirmationPort.confirmMobileNumber(domain.getMobileNumber());
                        domain.markMobileNumberConfirmed();
                }
        }

        private boolean isMissing(final UUID value) {
                return value == null || UUIDHelper.getDefault().equals(value);
        }
}
