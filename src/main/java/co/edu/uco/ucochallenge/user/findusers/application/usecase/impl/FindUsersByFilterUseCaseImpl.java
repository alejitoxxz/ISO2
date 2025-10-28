package co.edu.uco.ucochallenge.user.findusers.application.usecase.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import co.edu.uco.ucochallenge.crosscuting.helper.UUIDHelper;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.CityEntity;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.IdTypeEntity;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.UserEntity;
import co.edu.uco.ucochallenge.secondary.ports.repository.UserRepository;
import co.edu.uco.ucochallenge.user.findusers.application.usecase.FindUsersByFilterUseCase;
import co.edu.uco.ucochallenge.user.findusers.application.usecase.domain.FindUsersByFilterInputDomain;
import co.edu.uco.ucochallenge.user.findusers.application.usecase.domain.FindUsersByFilterResponseDomain;
import co.edu.uco.ucochallenge.user.findusers.application.usecase.domain.UserSummaryDomain;
import org.springframework.transaction.annotation.Transactional;  // ✅


@Service
public class FindUsersByFilterUseCaseImpl implements FindUsersByFilterUseCase {

        private final UserRepository userRepository;

        public FindUsersByFilterUseCaseImpl(final UserRepository userRepository) {
                this.userRepository = userRepository;
        }

        @Override
        @Transactional(readOnly = true)
        public FindUsersByFilterResponseDomain execute(final FindUsersByFilterInputDomain domain) {
                final var pageable = PageRequest.of(domain.getPage(), domain.getSize());
                final var pageResult = userRepository.findAll(pageable);

                final var users = pageResult.getContent().stream()
                                .map(this::mapToDomain)
                                .toList();

                return FindUsersByFilterResponseDomain.builder()
                                .users(users)
                                .page(pageResult.getNumber())
                                .size(pageResult.getSize())
                                .totalElements(pageResult.getTotalElements())
                                .totalPages(pageResult.getTotalPages())
                                .build();
        }

        private UserSummaryDomain mapToDomain(final UserEntity entity) {
                final IdTypeEntity idTypeEntity = entity.getIdType();
                final CityEntity cityEntity = entity.getHomeCity();

                return UserSummaryDomain.builder()
                                .id(entity.getId())
                                .idType(idTypeEntity != null ? idTypeEntity.getId() : UUIDHelper.getDefault())
                                .idNumber(entity.getIdNumber())
                                .firstName(entity.getFirstName())
                                .secondName(entity.getSecondName())
                                .firstSurname(entity.getFirstSurname())
                                .secondSurname(entity.getSecondSurname())
                                .homeCity(cityEntity != null ? cityEntity.getId() : UUIDHelper.getDefault())
                                .email(entity.getEmail())
                                .mobileNumber(entity.getMobileNumber())
                                .emailConfirmed(entity.isEmailConfirmed())
                                .mobileNumberConfirmed(entity.isMobileNumberConfirmed())
                                .build();
        }
}
