package co.edu.uco.ucochallenge.user.registeruser.application.rules;

public interface Rule {

        String getName();

        boolean evaluate(RuleContext context);
}
