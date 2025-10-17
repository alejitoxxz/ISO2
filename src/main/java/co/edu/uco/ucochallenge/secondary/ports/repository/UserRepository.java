package co.edu.uco.ucochallenge.secondary.ports.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.UserEntity;



public interface UserRepository extends JpaRepository<UserEntity, UUID> {

}
