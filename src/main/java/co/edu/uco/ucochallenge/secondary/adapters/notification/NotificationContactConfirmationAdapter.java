package co.edu.uco.ucochallenge.secondary.adapters.notification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.notificationapi.NotificationApi;
import com.notificationapi.model.NotificationRequest;
import com.notificationapi.model.User;

import co.edu.uco.ucochallenge.user.registeruser.application.port.ContactConfirmationPort;

@Component
public class NotificationContactConfirmationAdapter implements ContactConfirmationPort {

    private final NotificationApi api;

    public NotificationContactConfirmationAdapter(NotificationApi api) {
        this.api = api;
    }

    @Override
    public void confirmEmail(String email) {
        sendConfirmation(email, null, "Confirmación de correo electrónico");
    }

    @Override
    public void confirmMobileNumber(String mobileNumber) {
        sendConfirmation(null, mobileNumber, "Confirmación de número móvil");
    }

    private void sendConfirmation(String email, String number, String subject) {
        try {
            User user = new User(email != null ? email : number)
                    .setEmail(email)
                    .setNumber(number);

            Map<String, Object> mergeTags = new HashMap<>();
            mergeTags.put("name", "Usuario");
            mergeTags.put("confirmationLink", "https://ucochallenge.com/confirm?user=" + user.getIdentifier());
            mergeTags.put("currentYear", "2025");
            mergeTags.put("comment", subject);

            NotificationRequest request = new NotificationRequest("uco_notification", user)
                    .setTemplateId("template_one")
                    .setMergeTags(mergeTags);

            String response = api.send(request);
            System.out.println("[NotificationAPI] Confirmation sent: " + response);
        } catch (Exception e) {
            System.err.println("[NotificationAPI] Error sending confirmation: " + e.getMessage());
        }
    }
}
