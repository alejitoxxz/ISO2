package co.edu.uco.ucochallenge.user.findusers.application.interactor.dto;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import co.edu.uco.ucochallenge.application.Response;
import co.edu.uco.ucochallenge.crosscuting.helper.ObjectHelper;
import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;
import co.edu.uco.ucochallenge.crosscuting.helper.UUIDHelper;

public final class FindUsersByFilterOutputDTO extends Response<FindUsersByFilterOutputDTO.UsersPage> {

        private FindUsersByFilterOutputDTO(final boolean returnData, final UsersPage data) {
                super(returnData, data);
        }

        public static FindUsersByFilterOutputDTO of(final UsersPage data) {
                return new FindUsersByFilterOutputDTO(true, data);
        }

        public static FindUsersByFilterOutputDTO empty() {
                return new FindUsersByFilterOutputDTO(false, UsersPage.empty());
        }

        public record UsersPage(List<UserDTO> users, int page, int size, long totalElements, int totalPages) {

                public UsersPage {
                        users = List.copyOf(ObjectHelper.getDefault(users, Collections.emptyList()));
                }

                public static UsersPage of(final List<UserDTO> users, final int page, final int size,
                                final long totalElements, final int totalPages) {
                        return new UsersPage(users, page, size, totalElements, totalPages);
                }

                private static UsersPage empty() {
                        return new UsersPage(Collections.emptyList(), 0, 0, 0, 0);
                }
        }

        public record UserDTO(UUID id, UUID idType, String idNumber, String firstName, String secondName,
                        String firstSurname, String secondSurname, UUID homeCity, String email, String mobileNumber,
                        boolean emailConfirmed, boolean mobileNumberConfirmed) {

                public UserDTO {
                        id = UUIDHelper.getDefault(id);
                        idType = UUIDHelper.getDefault(idType);
                        idNumber = TextHelper.getDefaultWithTrim(idNumber);
                        firstName = TextHelper.getDefaultWithTrim(firstName);
                        secondName = TextHelper.getDefaultWithTrim(secondName);
                        firstSurname = TextHelper.getDefaultWithTrim(firstSurname);
                        secondSurname = TextHelper.getDefaultWithTrim(secondSurname);
                        homeCity = UUIDHelper.getDefault(homeCity);
                        email = TextHelper.getDefaultWithTrim(email);
                        mobileNumber = TextHelper.getDefaultWithTrim(mobileNumber);
                }
        }
}
