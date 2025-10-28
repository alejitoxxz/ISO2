package co.edu.uco.ucochallenge.user.registeruser.application.rules;

import java.util.Arrays;

import org.springframework.stereotype.Service;

@Service
public class RuleEngine {

        private final RuleRegistry ruleRegistry;

        public RuleEngine(final RuleRegistry ruleRegistry) {
                this.ruleRegistry = ruleRegistry;
        }

        public void applyRules(final RuleContext context, final String... ruleNames) {
                if (ruleNames == null || ruleNames.length == 0) {
                        return;
                }

                final Rule[] rules = Arrays.stream(ruleNames)
                                .map(ruleName -> {
                                        final Rule rule = ruleRegistry.getRule(ruleName);
                                        if (rule == null) {
                                                throw new IllegalArgumentException("Unknown rule: " + ruleName);
                                        }
                                        return rule;
                                })
                                .toArray(Rule[]::new);

                final var compositeRule = CompositeRule.of(String.join("_", ruleNames), rules);
                compositeRule.evaluate(context);
        }
}
