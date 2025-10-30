package co.edu.uco.ucochallenge.user.findusers.application.interactor.dto;

import java.util.UUID;

public class UserSummaryDTO {

        private UUID id;
        private String firstName;
        private String lastName;
        private String email;

        public UUID getId() {
                return id;
        }

        public void setId(final UUID id) {
                this.id = id;
        }

        public String getFirstName() {
                return firstName;
        }

        public void setFirstName(final String firstName) {
                this.firstName = firstName;
        }

        public String getLastName() {
                return lastName;
        }

        public void setLastName(final String lastName) {
                this.lastName = lastName;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(final String email) {
                this.email = email;
        }
}
