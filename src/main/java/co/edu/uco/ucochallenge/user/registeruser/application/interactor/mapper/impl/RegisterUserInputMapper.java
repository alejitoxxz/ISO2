package co.edu.uco.ucochallenge.user.registeruser.application.interactor.mapper.impl;

import java.util.UUID;

import org.springframework.stereotype.Component;

import co.edu.uco.ucochallenge.application.interactor.mapper.DomainMapper;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto.RegisterUserInputDTO;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.RegisterUserDomain;

@Component
public class RegisterUserInputMapper implements DomainMapper<RegisterUserInputDTO, RegisterUserDomain> {

        @Override
        public RegisterUserDomain toDomain(final RegisterUserInputDTO dto) {
                return RegisterUserDomain.builder()
                                .id(UUID.randomUUID())
                                .idType(dto.idType())
                                .idNumber(dto.idNumber())
                                .firstName(dto.firstName())
                                .secondName(dto.secondName())
                                .firstSurname(dto.firstSurname())
                                .secondSurname(dto.secondSurname())
                                .homeCity(dto.homeCity())
                                .email(dto.email())
                                .mobileNumber(dto.mobileNumber())
                                .build();
        }

        @Override
        public RegisterUserInputDTO toDto(final RegisterUserDomain domain) {
                return new RegisterUserInputDTO(domain.getIdType(), domain.getIdNumber(), domain.getFirstName(),
                                domain.getSecondName(), domain.getFirstSurname(), domain.getSecondSurname(),
                                domain.getHomeCity(), domain.getEmail(), domain.getMobileNumber());
        }
}
