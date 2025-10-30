package co.edu.uco.ucochallenge.user.findusers.application.interactor.mapper.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import co.edu.uco.ucochallenge.application.interactor.mapper.DomainMapper;
import co.edu.uco.ucochallenge.user.findusers.application.interactor.dto.FindUsersByFilterOutputDTO;
import co.edu.uco.ucochallenge.user.findusers.application.interactor.dto.FindUsersByFilterOutputDTO.UserDTO;
import co.edu.uco.ucochallenge.user.findusers.application.interactor.dto.FindUsersByFilterOutputDTO.UsersPage;
import co.edu.uco.ucochallenge.user.findusers.application.usecase.domain.FindUsersByFilterResponseDomain;
import co.edu.uco.ucochallenge.user.findusers.application.usecase.domain.UserSummaryDomain;

@Component
public class FindUsersByFilterOutputMapper
                implements DomainMapper<FindUsersByFilterOutputDTO, FindUsersByFilterResponseDomain> {

        @Override
        public FindUsersByFilterResponseDomain toDomain(final FindUsersByFilterOutputDTO dto) {
                final List<UserSummaryDomain> users = dto.data().users().stream()
                                .map(this::mapToDomain)
                                .toList();

                return FindUsersByFilterResponseDomain.builder()
                                .users(users)
                                .page(dto.data().page())
                                .size(dto.data().size())
                                .totalElements(dto.data().totalElements())
                                .totalPages(dto.data().totalPages())
                                .build();
        }

        @Override
        public FindUsersByFilterOutputDTO toDto(final FindUsersByFilterResponseDomain domain) {
                final List<UserDTO> users = domain.getUsers().stream()
                                .map(this::mapToDto)
                                .toList();

                final UsersPage page = UsersPage.of(users,
                                domain.getPage(),
                                domain.getSize(),
                                domain.getTotalElements(),
                                domain.getTotalPages());

                return FindUsersByFilterOutputDTO.of(page);
        }

        private UserSummaryDomain mapToDomain(final UserDTO dto) {
                return UserSummaryDomain.builder()
                                .id(dto.id())
                                .idType(dto.idType())
                                .idNumber(dto.idNumber())
                                .firstName(dto.firstName())
                                .secondName(dto.secondName())
                                .firstSurname(dto.firstSurname())
                                .secondSurname(dto.secondSurname())
                                .homeCity(dto.homeCity())
                                .email(dto.email())
                                .mobileNumber(dto.mobileNumber())
                                .emailConfirmed(dto.emailConfirmed())
                                .mobileNumberConfirmed(dto.mobileNumberConfirmed())
                                .build();
        }

        private UserDTO mapToDto(final UserSummaryDomain domain) {
                return new UserDTO(domain.getId(), domain.getIdType(), domain.getIdNumber(), domain.getFirstName(),
                                domain.getSecondName(), domain.getFirstSurname(), domain.getSecondSurname(),
                                domain.getHomeCity(), domain.getEmail(), domain.getMobileNumber(),
                                domain.isEmailConfirmed(), domain.isMobileNumberConfirmed());
        }
}
