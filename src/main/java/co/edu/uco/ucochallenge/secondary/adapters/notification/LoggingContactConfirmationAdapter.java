package co.edu.uco.ucochallenge.secondary.adapters.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import co.edu.uco.ucochallenge.user.registeruser.application.port.ContactConfirmationPort;

@Component
public class LoggingContactConfirmationAdapter implements ContactConfirmationPort {

        private static final Logger LOGGER = LoggerFactory.getLogger(LoggingContactConfirmationAdapter.class);

        @Override
        public void confirmEmail(final String email) {
                LOGGER.info("[EMAIL CONFIRMATION] Se envió confirmación al correo {}", email);
        }

        @Override
        public void confirmMobileNumber(final String mobileNumber) {
                LOGGER.info("[SMS CONFIRMATION] Se envió confirmación al número {}", mobileNumber);
        }
}
