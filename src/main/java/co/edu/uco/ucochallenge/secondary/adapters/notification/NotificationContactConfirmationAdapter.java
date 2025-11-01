package co.edu.uco.ucochallenge.secondary.adapters.notification;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
        sendConfirmation(email, null, "Confirma tu correo electrónico - UCO Challenge");
    }

    @Override
    public void confirmMobileNumber(String mobileNumber) {
        sendConfirmation(null, mobileNumber, "Confirma tu número móvil - UCO Challenge");
    }

    private void sendConfirmation(String email, String number, String subject) {
        try {
            User user = new User(email != null ? email : number)
                    .setEmail(email)
                    .setNumber(number);

            String idForLink = (email != null ? email : number);
            String confirmationLink = "https://ucochallenge.com/confirm?user="
                    + URLEncoder.encode(idForLink, StandardCharsets.UTF_8);

            Map<String, Object> mergeTags = new HashMap<>();
            mergeTags.put("name", idForLink);
            mergeTags.put("confirmationLink", confirmationLink);
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
