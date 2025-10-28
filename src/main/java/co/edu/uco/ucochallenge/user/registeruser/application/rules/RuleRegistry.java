package co.edu.uco.ucochallenge.user.registeruser.application.rules;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class RuleRegistry {

        private final Map<String, Rule> rulesMap = new HashMap<>();

        public RuleRegistry(final List<Rule> rules) {
                for (final Rule rule : rules) {
                        rulesMap.put(rule.getName(), rule);
                }
        }

        public Rule getRule(final String ruleName) {
                return rulesMap.get(ruleName);
        }
}
