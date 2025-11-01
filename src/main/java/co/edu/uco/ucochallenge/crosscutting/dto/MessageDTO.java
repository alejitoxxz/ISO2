package co.edu.uco.ucochallenge.crosscutting.dto;

public record MessageDTO(String code, String english, String spanish, String technical) {

    public String getUserMessage() {
        if (spanish != null && !spanish.isBlank()) {
            return spanish;
        }
        if (english != null && !english.isBlank()) {
            return english;
        }
        if (technical != null && !technical.isBlank()) {
            return technical;
        }
        return code;
    }
}
