package co.edu.uco.ucochallenge.secondary.adapters.repository.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.VerificationCodeEntity;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCodeEntity, UUID> {
    Optional<VerificationCodeEntity> findByContact(String contact);
}
