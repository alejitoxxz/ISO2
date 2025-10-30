package co.edu.uco.ucochallenge.secondary.adapters.repository.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.CityEntity;

public interface SpringDataCityRepository extends JpaRepository<CityEntity, UUID> {

        List<CityEntity> findByStateId(UUID stateId);
}
