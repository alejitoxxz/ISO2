package co.edu.uco.ucochallenge.secondary.adapters.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import co.edu.uco.ucochallenge.user.registeruser.application.port.NotificationPort;

@Component
public class LoggingNotificationAdapter implements NotificationPort {

        private static final Logger LOGGER = LoggerFactory.getLogger(LoggingNotificationAdapter.class);

        @Override
        public void notifyAdministrator(final String message) {
                LOGGER.info("[ADMIN NOTIFICATION] {}", message);
        }

        @Override
        public void notifyExecutor(final String executorIdentifier, final String message) {
                if (executorIdentifier == null || executorIdentifier.isBlank()) {
                        LOGGER.info("[EXECUTOR NOTIFICATION] {}", message);
                        return;
                }
                LOGGER.info("[EXECUTOR NOTIFICATION] [{}] {}", executorIdentifier, message);
        }

        @Override
        public void notifyEmailOwner(final String email, final String message) {
                LOGGER.info("[EMAIL NOTIFICATION] ({}) {}", email, message);
        }

        @Override
        public void notifyMobileOwner(final String mobileNumber, final String message) {
                LOGGER.info("[SMS NOTIFICATION] ({}) {}", mobileNumber, message);
        }
}
