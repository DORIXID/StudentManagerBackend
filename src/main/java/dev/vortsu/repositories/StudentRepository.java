package dev.vortsu.repositories;

import dev.vortsu.entity.Student;
import dev.vortsu.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Page<Student> findByNameContainingIgnoreCase(String name, Pageable pagable);

    //TODO: Прошерстить весь код и узнать как работает ВСЕ
    //как пейдж переиспользовать узнать про темплейты
    //    @JPQL
    //    //TODO как пейдж работает почитать
    Page<Student> findBySurnameContainingIgnoreCase(String surname, Pageable pagable);

    Page<Student> findByAge(int age, Pageable pageable);

    Optional<Student> findByUser_UserName(String username);

    Optional<Student> findByUser(User user);
}