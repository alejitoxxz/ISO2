package co.edu.uco.ucochallenge.user.findusers.application.interactor.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.ucochallenge.user.findusers.application.interactor.FindUsersByFilterInteractor;
import co.edu.uco.ucochallenge.user.findusers.application.interactor.dto.FindUsersByFilterInputDTO;
import co.edu.uco.ucochallenge.user.findusers.application.interactor.dto.FindUsersByFilterOutputDTO;
import co.edu.uco.ucochallenge.user.findusers.application.usecase.FindUsersByFilterUseCase;
import co.edu.uco.ucochallenge.user.findusers.application.usecase.domain.FindUsersByFilterInputDomain;
import co.edu.uco.ucochallenge.user.findusers.application.usecase.domain.UserSummaryDomain;
import org.springframework.transaction.annotation.Transactional;  // ✅


@Service
@Transactional(readOnly = true)
public class FindUsersByFilterInteractorImpl implements FindUsersByFilterInteractor {

        private final FindUsersByFilterUseCase useCase;

        public FindUsersByFilterInteractorImpl(final FindUsersByFilterUseCase useCase) {
                this.useCase = useCase;
        }

        @Override
        public FindUsersByFilterOutputDTO execute(final FindUsersByFilterInputDTO dto) {
                final var normalizedInput = FindUsersByFilterInputDTO.normalize(dto.page(), dto.size());
                final var domain = FindUsersByFilterInputDomain.builder()
                                .page(normalizedInput.page())
                                .size(normalizedInput.size())
                                .build();

                final var responseDomain = useCase.execute(domain);

                final var users = responseDomain.getUsers().stream()
                                .map(this::mapToDto)
                                .toList();

                final var page = FindUsersByFilterOutputDTO.UsersPage.of(users,
                                responseDomain.getPage(),
                                responseDomain.getSize(),
                                responseDomain.getTotalElements(),
                                responseDomain.getTotalPages());

                return FindUsersByFilterOutputDTO.of(page);
        }

        private FindUsersByFilterOutputDTO.UserDTO mapToDto(final UserSummaryDomain domain) {
                return new FindUsersByFilterOutputDTO.UserDTO(
                                domain.getId(),
                                domain.getIdType(),
                                domain.getIdNumber(),
                                domain.getFirstName(),
                                domain.getSecondName(),
                                domain.getFirstSurname(),
                                domain.getSecondSurname(),
                                domain.getHomeCity(),
                                domain.getEmail(),
                                domain.getMobileNumber(),
                                domain.isEmailConfirmed(),
                                domain.isMobileNumberConfirmed());
        }
}
