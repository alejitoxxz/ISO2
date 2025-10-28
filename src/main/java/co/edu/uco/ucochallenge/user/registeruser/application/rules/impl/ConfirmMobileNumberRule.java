package co.edu.uco.ucochallenge.user.registeruser.application.rules.impl;

import org.springframework.stereotype.Component;

import co.edu.uco.ucochallenge.user.registeruser.application.rules.RegisterUserRuleNames;
import co.edu.uco.ucochallenge.user.registeruser.application.rules.Rule;
import co.edu.uco.ucochallenge.user.registeruser.application.rules.RuleContext;

@Component
public class ConfirmMobileNumberRule implements Rule {

        @Override
        public String getName() {
                return RegisterUserRuleNames.CONFIRM_MOBILE_NUMBER;
        }

        @Override
        public boolean evaluate(final RuleContext context) {
                var domain = context.getDomain();

                if (domain.hasMobileNumber()) {
                        context.getContactConfirmationPort().confirmMobileNumber(domain.getMobileNumber());
                        domain.markMobileNumberConfirmed();
                }

                return true;
        }
}
