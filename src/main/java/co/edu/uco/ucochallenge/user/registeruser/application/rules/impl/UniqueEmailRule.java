package co.edu.uco.ucochallenge.user.registeruser.application.rules.impl;

import java.util.Optional;

import org.springframework.stereotype.Component;

import co.edu.uco.ucochallenge.crosscuting.exception.BusinessException;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.UserEntity;
import co.edu.uco.ucochallenge.user.registeruser.application.rules.RegisterUserRuleNames;
import co.edu.uco.ucochallenge.user.registeruser.application.rules.Rule;
import co.edu.uco.ucochallenge.user.registeruser.application.rules.RuleContext;

@Component
public class UniqueEmailRule implements Rule {

        @Override
        public String getName() {
                return RegisterUserRuleNames.UNIQUE_EMAIL;
        }

        @Override
        public boolean evaluate(final RuleContext context) {
                var domain = context.getDomain();

                if (!domain.hasEmail()) {
                        return true;
                }

                Optional<UserEntity> existingUser = context.getUserRepository().findByEmail(domain.getEmail());

                if (existingUser.isPresent()) {
                        var user = existingUser.get();
                        var ownerMessage = String.format(
                                        "Hola %s %s, se intentó registrar una nueva cuenta utilizando tu correo %s.",
                                        user.getFirstName(), user.getFirstSurname(), domain.getEmail());
                        var executorMessage = String.format(
                                        "El correo %s ya pertenece al usuario %s %s con id %s.",
                                        domain.getEmail(), user.getFirstName(), user.getFirstSurname(), user.getId());

                        var notificationPort = context.getNotificationPort();
                        notificationPort.notifyEmailOwner(domain.getEmail(), ownerMessage);
                        notificationPort.notifyExecutor(context.getExecutorIdentifier(), executorMessage);

                        throw new BusinessException("A user with the provided email already exists.");
                }

                return true;
        }
}
