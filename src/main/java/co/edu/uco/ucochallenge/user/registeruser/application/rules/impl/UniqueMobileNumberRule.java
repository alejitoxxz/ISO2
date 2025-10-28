package co.edu.uco.ucochallenge.user.registeruser.application.rules.impl;

import java.util.Optional;

import org.springframework.stereotype.Component;

import co.edu.uco.ucochallenge.crosscuting.exception.BusinessException;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.UserEntity;
import co.edu.uco.ucochallenge.user.registeruser.application.rules.RegisterUserRuleNames;
import co.edu.uco.ucochallenge.user.registeruser.application.rules.Rule;
import co.edu.uco.ucochallenge.user.registeruser.application.rules.RuleContext;

@Component
public class UniqueMobileNumberRule implements Rule {

        @Override
        public String getName() {
                return RegisterUserRuleNames.UNIQUE_MOBILE_NUMBER;
        }

        @Override
        public boolean evaluate(final RuleContext context) {
                var domain = context.getDomain();

                if (!domain.hasMobileNumber()) {
                        return true;
                }

                Optional<UserEntity> existingUser = context.getUserRepository()
                                .findByMobileNumber(domain.getMobileNumber());

                if (existingUser.isPresent()) {
                        var user = existingUser.get();
                        var ownerMessage = String.format(
                                        "Hola %s %s, se intentó registrar una nueva cuenta utilizando tu número %s.",
                                        user.getFirstName(), user.getFirstSurname(), domain.getMobileNumber());
                        var executorMessage = String.format(
                                        "El número móvil %s ya pertenece al usuario %s %s con id %s.",
                                        domain.getMobileNumber(), user.getFirstName(), user.getFirstSurname(), user.getId());

                        var notificationPort = context.getNotificationPort();
                        notificationPort.notifyMobileOwner(domain.getMobileNumber(), ownerMessage);
                        notificationPort.notifyExecutor(context.getExecutorIdentifier(), executorMessage);

                        throw new BusinessException("A user with the provided mobile number already exists.");
                }

                return true;
        }
}
