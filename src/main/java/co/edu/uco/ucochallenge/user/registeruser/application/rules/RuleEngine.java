package co.edu.uco.ucochallenge.user.registeruser.application.rules;

import org.springframework.stereotype.Service;

@Service
public class RuleEngine {

        private final RuleRegistry ruleRegistry;

        public RuleEngine(final RuleRegistry ruleRegistry) {
                this.ruleRegistry = ruleRegistry;
        }

        public void applyRules(final RuleContext context, final String... ruleNames) {
                for (final String ruleName : ruleNames) {
                        final Rule rule = ruleRegistry.getRule(ruleName);
                        if (rule == null) {
                                throw new IllegalArgumentException("Unknown rule: " + ruleName);
                        }
                        rule.evaluate(context);
                }
        }
}
