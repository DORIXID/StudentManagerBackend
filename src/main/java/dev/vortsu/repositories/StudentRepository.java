package dev.vortsu.repositories;

import dev.vortsu.entity.StudentEntity;
import dev.vortsu.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
    Page<StudentEntity> findByNameContainingIgnoreCase(String name, Pageable pagable);

    Page<StudentEntity> findBySurnameContainingIgnoreCase(String surname, Pageable pagable);

    Page<StudentEntity> findByAge(int age, Pageable pageable);

    @Query("select s from StudentEntity s join s.user u where u.userName like ?1")
    Optional<StudentEntity> findStudentByUsername(String username);

    Optional<StudentEntity> findByUser(UserEntity user);

    //конструктор запроса почитать. пока фильтрация только по name
    @Query("SELECT s FROM StudentEntity s WHERE s.name ILIKE CONCAT('%', ?1, '%')")
    Page<StudentEntity> findByName(String searchedValue, Pageable pageable);
    //TODO: http (GET POST and other), body, headers http-https, rest, rest api
    //Здесь пока нет String searchedType. Убрал потому, что пока тут затычка на месте searchedType потому что JPQL не позволяет использовать аргументы не как литералы
}