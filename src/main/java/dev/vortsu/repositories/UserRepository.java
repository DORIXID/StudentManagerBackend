package dev.vortsu.repositories;

import dev.vortsu.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @EntityGraph(attributePaths = {"password"})
    Optional<UserEntity> findByUserName(String userName);

    Optional<UserEntity> findById(Long id);
}
