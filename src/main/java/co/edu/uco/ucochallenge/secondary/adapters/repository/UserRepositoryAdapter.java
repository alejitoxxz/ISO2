package co.edu.uco.ucochallenge.secondary.adapters.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import co.edu.uco.ucochallenge.crosscuting.helper.UUIDHelper;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.CityEntity;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.IdTypeEntity;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.UserEntity;
import co.edu.uco.ucochallenge.secondary.adapters.repository.jpa.SpringDataUserRepository;
import co.edu.uco.ucochallenge.user.confirmcontact.application.port.ConfirmUserContactRepositoryPort;
import co.edu.uco.ucochallenge.user.findusers.application.port.FindUsersByFilterRepositoryPort;
import co.edu.uco.ucochallenge.user.findusers.application.usecase.domain.FindUsersByFilterResponseDomain;
import co.edu.uco.ucochallenge.user.findusers.application.usecase.domain.UserSummaryDomain;
import co.edu.uco.ucochallenge.user.registeruser.application.port.RegisterUserRepositoryPort;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.ExistingUserSnapshotDomain;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.RegisterUserDomain;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;

@Repository
public class UserRepositoryAdapter implements RegisterUserRepositoryPort, FindUsersByFilterRepositoryPort,
                ConfirmUserContactRepositoryPort {

        private final SpringDataUserRepository repository;

        @PersistenceContext
        private EntityManager entityManager;

        public UserRepositoryAdapter(final SpringDataUserRepository repository) {
                this.repository = repository;
        }

        @Override
        public boolean existsById(final UUID id) {
                return repository.existsById(id);
        }

        @Override
        public Optional<ExistingUserSnapshotDomain> findByIdentification(final UUID idType, final String idNumber) {
                return repository.findByIdTypeIdAndIdNumber(idType, idNumber)
                                .map(this::mapToSnapshot);
        }

        @Override
        public Optional<ExistingUserSnapshotDomain> findByEmail(final String email) {
                return repository.findByEmail(email)
                                .map(this::mapToSnapshot);
        }

        @Override
        public Optional<ExistingUserSnapshotDomain> findByMobileNumber(final String mobileNumber) {
                return repository.findByMobileNumber(mobileNumber)
                                .map(this::mapToSnapshot);
        }

        @Override
        public void save(final RegisterUserDomain domain) {
                repository.save(mapToEntity(domain));
        }

        @Override
        public FindUsersByFilterResponseDomain findAll(final int page, final int size) {
                final var pageResult = repository.findAll(PageRequest.of(page, size));

                final var users = pageResult.getContent().stream()
                                .map(this::mapToUserSummary)
                                .toList();

                return FindUsersByFilterResponseDomain.builder()
                                .users(users)
                                .page(pageResult.getNumber())
                                .size(pageResult.getSize())
                                .totalElements(pageResult.getTotalElements())
                                .totalPages(pageResult.getTotalPages())
                                .build();
        }

        @Override
        public boolean confirmEmail(final UUID id) {
                return repository.findById(id)
                                .map(entity -> {
                                        entity.confirmEmail();
                                        repository.save(entity);
                                        return true;
                                })
                                .orElse(false);
        }

        @Override
        public boolean confirmMobileNumber(final UUID id) {
                return repository.findById(id)
                                .map(entity -> {
                                        entity.confirmMobileNumber();
                                        repository.save(entity);
                                        return true;
                                })
                                .orElse(false);
        }

        private ExistingUserSnapshotDomain mapToSnapshot(final UserEntity entity) {
                return ExistingUserSnapshotDomain.builder()
                                .id(entity.getId())
                                .firstName(entity.getFirstName())
                                .firstSurname(entity.getFirstSurname())
                                .email(entity.getEmail())
                                .mobileNumber(entity.getMobileNumber())
                                .build();
        }

        private UserSummaryDomain mapToUserSummary(final UserEntity entity) {
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

        private UserEntity mapToEntity(final RegisterUserDomain domain) {
                final var idTypeId = domain.getIdType();
                final var cityId = domain.getHomeCity();

                if (UUIDHelper.getDefault().equals(idTypeId)) {
                        throw new EntityNotFoundException("IdType identifier must not be empty");
                }

                if (UUIDHelper.getDefault().equals(cityId)) {
                        throw new EntityNotFoundException("City identifier must not be empty");
                }

                final IdTypeEntity idTypeRef = entityManager.getReference(IdTypeEntity.class, idTypeId);
                final CityEntity cityRef = entityManager.getReference(CityEntity.class, cityId);

                return new UserEntity.Builder()
                                .id(domain.getId())
                                .idType(idTypeRef)
                                .idNumber(domain.getIdNumber())
                                .firstName(domain.getFirstName())
                                .secondName(domain.getSecondName())
                                .firstSurname(domain.getFirstSurname())
                                .secondSurname(domain.getSecondSurname())
                                .homeCity(cityRef)
                                .email(domain.getEmail())
                                .mobileNumber(domain.getMobileNumber())
                                .emailConfirmed(domain.isEmailConfirmed())
                                .mobileNumberConfirmed(domain.isMobileNumberConfirmed())
                                .build();
        }
}
