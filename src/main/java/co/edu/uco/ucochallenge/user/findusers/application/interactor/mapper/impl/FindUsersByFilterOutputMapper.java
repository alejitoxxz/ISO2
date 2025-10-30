package co.edu.uco.ucochallenge.user.findusers.application.interactor.mapper.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import co.edu.uco.ucochallenge.application.interactor.mapper.DomainMapper;
import co.edu.uco.ucochallenge.user.findusers.application.interactor.dto.FindUsersByFilterOutputDTO;
import co.edu.uco.ucochallenge.user.findusers.application.interactor.dto.UserSummaryDTO;
import co.edu.uco.ucochallenge.user.findusers.application.usecase.domain.FindUsersByFilterResponseDomain;
import co.edu.uco.ucochallenge.user.findusers.application.usecase.domain.UserSummaryDomain;

@Component
public class FindUsersByFilterOutputMapper
                implements DomainMapper<FindUsersByFilterOutputDTO, FindUsersByFilterResponseDomain> {

        @Override
        public FindUsersByFilterResponseDomain toDomain(final FindUsersByFilterOutputDTO dto) {
                final List<UserSummaryDTO> dtoUsers = dto.getUsers();
                final List<UserSummaryDomain> users = dtoUsers == null
                                ? List.of()
                                : dtoUsers.stream()
                                                .map(this::mapToDomain)
                                                .collect(Collectors.toList());

                return FindUsersByFilterResponseDomain.builder()
                                .users(users)
                                .page(dto.getPage())
                                .size(dto.getSize())
                                .totalElements(dto.getTotalElements())
                                .build();
        }

        @Override
        public FindUsersByFilterOutputDTO toDto(final FindUsersByFilterResponseDomain domain) {
                final List<UserSummaryDTO> users = domain.getUsers()
                                .stream()
                                .map(this::mapToDto)
                                .collect(Collectors.toList());

                final FindUsersByFilterOutputDTO dto = new FindUsersByFilterOutputDTO();
                dto.setUsers(users);
                dto.setPage(domain.getPage());
                dto.setSize(domain.getSize());
                dto.setTotalElements(domain.getTotalElements());
                return dto;
        }

        private UserSummaryDomain mapToDomain(final UserSummaryDTO dto) {
                return UserSummaryDomain.builder()
                                .id(dto.getId())
                                .firstName(dto.getFirstName())
                                .firstSurname(dto.getLastName())
                                .email(dto.getEmail())
                                .build();
        }

        private UserSummaryDTO mapToDto(final UserSummaryDomain domain) {
                final UserSummaryDTO dto = new UserSummaryDTO();
                dto.setId(domain.getId());
                dto.setFirstName(domain.getFirstName());
                dto.setLastName(domain.getFirstSurname());
                dto.setEmail(domain.getEmail());
                return dto;
        }
}
