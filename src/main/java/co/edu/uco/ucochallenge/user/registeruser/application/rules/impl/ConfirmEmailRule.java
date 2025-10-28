package co.edu.uco.ucochallenge.user.registeruser.application.rules.impl;

import org.springframework.stereotype.Component;

import co.edu.uco.ucochallenge.user.registeruser.application.rules.RegisterUserRuleNames;
import co.edu.uco.ucochallenge.user.registeruser.application.rules.Rule;
import co.edu.uco.ucochallenge.user.registeruser.application.rules.RuleContext;

@Component
public class ConfirmEmailRule implements Rule {

        @Override
        public String getName() {
                return RegisterUserRuleNames.CONFIRM_EMAIL;
        }

        @Override
        public boolean evaluate(final RuleContext context) {
                var domain = context.getDomain();

                if (domain.hasEmail()) {
                        context.getContactConfirmationPort().confirmEmail(domain.getEmail());
                        domain.markEmailConfirmed();
                }

                return true;
        }
}
