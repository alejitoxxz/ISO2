package co.edu.uco.ucochallenge.user.registeruser.application.rules.impl;

import java.util.UUID;

import org.springframework.stereotype.Component;

import co.edu.uco.ucochallenge.crosscuting.helper.UUIDHelper;
import co.edu.uco.ucochallenge.user.registeruser.application.rules.RegisterUserRuleNames;
import co.edu.uco.ucochallenge.user.registeruser.application.rules.Rule;
import co.edu.uco.ucochallenge.user.registeruser.application.rules.RuleContext;

@Component
public class UniqueUserIdRule implements Rule {

        @Override
        public String getName() {
                return RegisterUserRuleNames.UNIQUE_USER_ID;
        }

        @Override
        public boolean evaluate(final RuleContext context) {
                var domain = context.getDomain();
                var repository = context.getUserRepository();

                UUID candidateId = domain.getId();
                if (UUIDHelper.getDefault().equals(candidateId)) {
                        candidateId = UUID.randomUUID();
                        domain.updateId(candidateId);
                }

                while (repository.existsById(candidateId)) {
                        candidateId = UUID.randomUUID();
                        domain.updateId(candidateId);
                }

                return true;
        }
}
