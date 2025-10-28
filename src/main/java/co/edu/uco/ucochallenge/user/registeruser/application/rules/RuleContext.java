package co.edu.uco.ucochallenge.user.registeruser.application.rules;

import java.util.Objects;

import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;
import co.edu.uco.ucochallenge.secondary.ports.repository.UserRepository;
import co.edu.uco.ucochallenge.user.registeruser.application.port.ContactConfirmationPort;
import co.edu.uco.ucochallenge.user.registeruser.application.port.NotificationPort;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.RegisterUserDomain;

public class RuleContext {

        private final RegisterUserDomain domain;
        private final UserRepository userRepository;
        private final NotificationPort notificationPort;
        private final ContactConfirmationPort contactConfirmationPort;
        private final String executorIdentifier;

        private RuleContext(final Builder builder) {
                this.domain = Objects.requireNonNull(builder.domain, "RegisterUserDomain is required");
                this.userRepository = Objects.requireNonNull(builder.userRepository, "UserRepository is required");
                this.notificationPort = Objects.requireNonNull(builder.notificationPort, "NotificationPort is required");
                this.contactConfirmationPort = Objects.requireNonNull(builder.contactConfirmationPort,
                                "ContactConfirmationPort is required");
                this.executorIdentifier = TextHelper.getDefaultWithTrim(builder.executorIdentifier);
        }

        public static Builder builder() {
                return new Builder();
        }

        public RegisterUserDomain getDomain() {
                return domain;
        }

        public UserRepository getUserRepository() {
                return userRepository;
        }

        public NotificationPort getNotificationPort() {
                return notificationPort;
        }

        public ContactConfirmationPort getContactConfirmationPort() {
                return contactConfirmationPort;
        }

        public String getExecutorIdentifier() {
                return executorIdentifier;
        }

        public static final class Builder {
                private RegisterUserDomain domain;
                private UserRepository userRepository;
                private NotificationPort notificationPort;
                private ContactConfirmationPort contactConfirmationPort;
                private String executorIdentifier = TextHelper.getDefault();

                public Builder domain(final RegisterUserDomain domain) {
                        this.domain = domain;
                        return this;
                }

                public Builder userRepository(final UserRepository userRepository) {
                        this.userRepository = userRepository;
                        return this;
                }

                public Builder notificationPort(final NotificationPort notificationPort) {
                        this.notificationPort = notificationPort;
                        return this;
                }

                public Builder contactConfirmationPort(final ContactConfirmationPort contactConfirmationPort) {
                        this.contactConfirmationPort = contactConfirmationPort;
                        return this;
                }

                public Builder executorIdentifier(final String executorIdentifier) {
                        this.executorIdentifier = executorIdentifier;
                        return this;
                }

                public RuleContext build() {
                        return new RuleContext(this);
                }
        }
}
