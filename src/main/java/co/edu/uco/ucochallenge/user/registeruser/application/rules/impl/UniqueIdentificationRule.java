package co.edu.uco.ucochallenge.user.registeruser.application.rules.impl;

import java.util.Optional;

import org.springframework.stereotype.Component;

import co.edu.uco.ucochallenge.crosscuting.exception.BusinessException;
import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;
import co.edu.uco.ucochallenge.crosscuting.helper.UUIDHelper;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.UserEntity;
import co.edu.uco.ucochallenge.user.registeruser.application.rules.RegisterUserRuleNames;
import co.edu.uco.ucochallenge.user.registeruser.application.rules.Rule;
import co.edu.uco.ucochallenge.user.registeruser.application.rules.RuleContext;

@Component
public class UniqueIdentificationRule implements Rule {

        @Override
        public String getName() {
                return RegisterUserRuleNames.UNIQUE_IDENTIFICATION;
        }

        @Override
        public boolean evaluate(final RuleContext context) {
                var domain = context.getDomain();

                if (UUIDHelper.getDefault().equals(domain.getIdType()) || TextHelper.isEmpty(domain.getIdNumber())) {
                        return true;
                }

                Optional<UserEntity> existingUser = context.getUserRepository()
                                .findByIdTypeIdAndIdNumber(domain.getIdType(), domain.getIdNumber());

                if (existingUser.isPresent()) {
                        var message = String.format(
                                        "A user with identification type %s and number %s already exists with id %s.",
                                        domain.getIdType(), domain.getIdNumber(), existingUser.get().getId());

                        var notificationPort = context.getNotificationPort();
                        notificationPort.notifyAdministrator(message);
                        notificationPort.notifyExecutor(context.getExecutorIdentifier(), message);

                        throw new BusinessException("A user with the provided identification already exists.");
                }

                return true;
        }
}
