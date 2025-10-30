package co.edu.uco.ucochallenge.secondary.adapters.repository.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.CountryEntity;

public interface SpringDataCountryRepository extends JpaRepository<CountryEntity, UUID> {
}
