package co.edu.uco.ucochallenge.secondary.adapters.notification;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.notificationapi.NotificationApi;
import com.notificationapi.model.NotificationRequest;
import com.notificationapi.model.User;

import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.VerificationCodeEntity;
import co.edu.uco.ucochallenge.secondary.adapters.repository.jpa.VerificationCodeRepository;
import co.edu.uco.ucochallenge.user.registeruser.application.port.ContactConfirmationPort;

@Component
public class NotificationContactConfirmationAdapter implements ContactConfirmationPort {

    private static final int CODE_UPPER_BOUND = 1_000_000;

    private final NotificationApi api;
    private final VerificationCodeRepository codeRepository;
    private final SecureRandom random = new SecureRandom();

    public NotificationContactConfirmationAdapter(NotificationApi api, VerificationCodeRepository codeRepository) {
        this.api = api;
        this.codeRepository = codeRepository;
    }

    @Override
    public void confirmEmail(String email) {
        sendConfirmation(email, null, "Confirma tu correo electrónico - UCO Challenge");
    }

    @Override
    public void confirmMobileNumber(String mobileNumber) {
        sendConfirmation(null, mobileNumber, "Confirma tu número móvil - UCO Challenge");
    }

    @Transactional
    private void sendConfirmation(String email, String number, String subject) {
        try {
            String contact = email != null ? email : number;
            String code = String.format("%06d", random.nextInt(CODE_UPPER_BOUND));

            codeRepository.findByContact(contact).ifPresent(codeRepository::delete);
            codeRepository.save(new VerificationCodeEntity(contact, code, LocalDateTime.now().plusMinutes(15)));

            User user = new User(contact)
                    .setEmail(email)
                    .setNumber(number);

            Map<String, Object> mergeTags = new HashMap<>();
            mergeTags.put("name", contact);
            mergeTags.put("confirmationCode", code);
            mergeTags.put("currentYear", "2025");
            mergeTags.put("comment", subject);

            NotificationRequest request = new NotificationRequest("uco_notification", user)
                    .setTemplateId("template_one")
                    .setMergeTags(mergeTags);

            String response = api.send(request);
            System.out.println("[NotificationAPI] Sent code " + code + " to " + contact + " | Response: " + response);
        } catch (Exception e) {
            System.err.println("[NotificationAPI] Error sending code: " + e.getMessage());
        }
    }
}
