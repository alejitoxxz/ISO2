package co.edu.uco.ucochallenge.user.confirmcontact.application.service;

import java.util.UUID;

public interface UserContactConfirmationService {

        void confirmEmail(UUID id);

        void confirmMobile(UUID id);

        boolean verifyCode(String contact, String code);
}
