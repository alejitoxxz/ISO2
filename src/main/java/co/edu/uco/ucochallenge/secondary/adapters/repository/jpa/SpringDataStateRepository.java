package co.edu.uco.ucochallenge.secondary.adapters.repository.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.StateEntity;

public interface SpringDataStateRepository extends JpaRepository<StateEntity, UUID> {

        List<StateEntity> findByCountryId(UUID countryId);
}
