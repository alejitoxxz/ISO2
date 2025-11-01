package co.edu.uco.ucochallenge.secondary.adapters.notification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.notificationapi.NotificationApi;
import com.notificationapi.model.NotificationRequest;
import com.notificationapi.model.User;

import co.edu.uco.ucochallenge.user.registeruser.application.port.NotificationPort;

@Component
public class NotificationApiAdapter implements NotificationPort {

    private final NotificationApi api;

    public NotificationApiAdapter(NotificationApi api) {
        this.api = api;
    }

    @Override
    public void notifyAdministrator(String message) {
        sendNotification("admin@ucochallenge.edu.co", message, "Administrador");
    }

    @Override
    public void notifyExecutor(String executorIdentifier, String message) {
        if (executorIdentifier == null || executorIdentifier.isBlank()) {
            sendNotification("admin@ucochallenge.edu.co", message, "Ejecutor desconocido");
        } else {
            sendNotification(executorIdentifier, message, "Ejecutor");
        }
    }

    @Override
    public void notifyEmailOwner(String email, String message) {
        sendNotification(email, message, "Confirmación de correo");
    }

    @Override
    public void notifyMobileOwner(String mobileNumber, String message) {
        sendNotification(mobileNumber, message, "Confirmación de número");
    }

    private void sendNotification(String target, String message, String context) {
        try {
            User user = new User(target)
                    .setEmail(target)
                    .setNumber(target != null && target.startsWith("+") ? target : null);

            Map<String, Object> mergeTags = new HashMap<>();
            mergeTags.put("name", target);
            mergeTags.put("confirmationLink", "https://ucochallenge.com/confirm");
            mergeTags.put("currentYear", "2025");
            mergeTags.put("comment", message);

            NotificationRequest request = new NotificationRequest("uco_notification", user)
                    .setTemplateId("template_one")
                    .setMergeTags(mergeTags);

            String response = api.send(request);
            System.out.println("[NotificationAPI] " + context + " notification sent: " + response);
        } catch (Exception e) {
            System.err.println("[NotificationAPI] Error sending notification: " + e.getMessage());
        }
    }
}
