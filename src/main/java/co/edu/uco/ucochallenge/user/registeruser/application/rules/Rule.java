package co.edu.uco.ucochallenge.user.registeruser.application.rules;

public interface Rule {

        String getName();

        boolean evaluate(RuleContext context);

        default Rule and(final Rule other) {
                return CompositeRule.of(String.format("%s_AND_%s", getName(), other.getName()), this, other);
        }

        default Rule or(final Rule other) {
                return new Rule() {

                        @Override
                        public String getName() {
                                return String.format("%s_OR_%s", Rule.this.getName(), other.getName());
                        }

                        @Override
                        public boolean evaluate(final RuleContext context) {
                                final boolean firstResult = Rule.this.evaluate(context);
                                if (firstResult) {
                                        return true;
                                }
                                return other.evaluate(context);
                        }
                };
        }
}
