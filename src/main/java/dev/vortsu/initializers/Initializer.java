package dev.vortsu.initializers;

import dev.vortsu.entity.*;
import dev.vortsu.repositories.StudentRepository;
import dev.vortsu.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Transactional
public class Initializer implements CommandLineRunner {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        User user;

        if (userRepository.count() == 0) {

            Password password = new Password();
            password.setPassword(encoder.encode("123456"));

            user = new User();
            user.setUserName("student1");
            user.setRole(Role.STUDENT);
            user.setEnabled(true);
            user.setPassword(password);

            userRepository.save(user);

            Student student = new Student();
            student.setUser(user);
            student.setName("Иван");
            student.setSurname("Иванов");
            student.setAge(20);
            studentRepository.save(student);

            for (int i = 2; i <= 5; i++) {

                password = new Password();
                password.setPassword(encoder.encode("student" + i));

                user = new User();
                user.setUserName("student" + i);
                user.setRole(Role.STUDENT);
                user.setEnabled(true);
                user.setPassword(password);
                user = userRepository.save(user);

                student = new Student();
                student.setUser(user);
                student.setName("Студент" + i);
                student.setSurname("Фамилия" + i);
                student.setAge(18 + i);
                studentRepository.save(student);
            }

            password = new Password();
            password.setPassword(encoder.encode("admin"));

            user = new User();
            user.setUserName("admin");
            user.setRole(Role.ADMIN);
            user.setEnabled(true);
            user.setPassword(password);
            userRepository.save(user);

            password = new Password();
            password.setPassword(encoder.encode("teacher"));

            user = new User();
            user.setUserName("teacher");
            user.setRole(Role.TEACHER);
            user.setEnabled(true);
            user.setPassword(password);
            userRepository.save(user);
        }


    }
}
