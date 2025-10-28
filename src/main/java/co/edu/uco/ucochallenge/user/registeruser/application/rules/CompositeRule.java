package co.edu.uco.ucochallenge.user.registeruser.application.rules;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import co.edu.uco.ucochallenge.crosscuting.exception.BusinessException;

public final class CompositeRule implements Rule {

        private final String name;
        private final List<Rule> rules;

        private CompositeRule(final String name, final List<Rule> rules) {
                this.name = Objects.requireNonNull(name, "Composite rule name is required");
                this.rules = List.copyOf(rules);
        }

        public static CompositeRule of(final String name, final Rule... rules) {
                if (rules == null || rules.length == 0) {
                        throw new IllegalArgumentException("At least one rule must be provided to compose");
                }

                return new CompositeRule(name, Arrays.stream(rules)
                                .map(rule -> Objects.requireNonNull(rule, "Composite rule elements cannot be null"))
                                .toList());
        }

        @Override
        public String getName() {
                return name;
        }

        @Override
        public boolean evaluate(final RuleContext context) {
                for (final Rule rule : rules) {
                        final boolean result = rule.evaluate(context);
                        if (!result) {
                                throw new BusinessException(
                                                String.format("Rule %s failed during the %s evaluation", rule.getName(), name));
                        }
                }
                return true;
        }
}
