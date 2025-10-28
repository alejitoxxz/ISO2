package co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.ucochallenge.application.Void;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.CityEntity;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.IdTypeEntity;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.UserEntity;
import co.edu.uco.ucochallenge.secondary.ports.repository.UserRepository;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.RegisterUserUseCase;
import co.edu.uco.ucochallenge.user.registeruser.application.port.ContactConfirmationPort;
import co.edu.uco.ucochallenge.user.registeruser.application.port.NotificationPort;
import co.edu.uco.ucochallenge.user.registeruser.application.rules.RegisterUserRuleNames;
import co.edu.uco.ucochallenge.user.registeruser.application.rules.RuleContext;
import co.edu.uco.ucochallenge.user.registeruser.application.rules.RuleEngine;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.RegisterUserDomain;

@Service
public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

        private static final String EXECUTOR_IDENTIFIER = "REGISTER_USER_USE_CASE";

        private final UserRepository repository;
        private final RuleEngine ruleEngine;
        private final NotificationPort notificationPort;
        private final ContactConfirmationPort contactConfirmationPort;

        public RegisterUserUseCaseImpl(final UserRepository repository, final RuleEngine ruleEngine,
                        final NotificationPort notificationPort,
                        final ContactConfirmationPort contactConfirmationPort) {
                this.repository = repository;
                this.ruleEngine = ruleEngine;
                this.notificationPort = notificationPort;
                this.contactConfirmationPort = contactConfirmationPort;
        }

        @Override
        public Void execute(final RegisterUserDomain domain) {
                var context = RuleContext.builder()
                                .domain(domain)
                                .userRepository(repository)
                                .notificationPort(notificationPort)
                                .contactConfirmationPort(contactConfirmationPort)
                                .executorIdentifier(EXECUTOR_IDENTIFIER)
                                .build();

                ruleEngine.applyRules(context,
                                RegisterUserRuleNames.UNIQUE_USER_ID,
                                RegisterUserRuleNames.UNIQUE_IDENTIFICATION,
                                RegisterUserRuleNames.UNIQUE_EMAIL,
                                RegisterUserRuleNames.UNIQUE_MOBILE_NUMBER,
                                RegisterUserRuleNames.CONFIRM_EMAIL,
                                RegisterUserRuleNames.CONFIRM_MOBILE_NUMBER);

                var userEntity = mapToEntity(domain);
                repository.save(userEntity);
                return Void.returnVoid();
        }

        private UserEntity mapToEntity(final RegisterUserDomain domain) {
                var idTypeEntity = new IdTypeEntity.Builder()
                                .id(domain.getIdType())
                                .build();

                var cityEntity = new CityEntity.Builder()
                                .id(domain.getHomeCity())
                                .build();

                return new UserEntity.Builder()
                                .id(domain.getId())
                                .idType(idTypeEntity)
                                .idNumber(domain.getIdNumber())
                                .firstName(domain.getFirstName())
                                .secondName(domain.getSecondName())
                                .firstSurname(domain.getFirstSurname())
                                .secondSurname(domain.getSecondSurname())
                                .homeCity(cityEntity)
                                .email(domain.getEmail())
                                .mobileNumber(domain.getMobileNumber())
                                .emailConfirmed(domain.isEmailConfirmed())
                                .mobileNumberConfirmed(domain.isMobileNumberConfirmed())
                                .build();
        }
}
