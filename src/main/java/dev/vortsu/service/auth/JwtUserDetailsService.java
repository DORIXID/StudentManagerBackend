package dev.vortsu.service.auth;

import dev.vortsu.dto.AuthUserDTO;
import dev.vortsu.entity.UserEntity;
import dev.vortsu.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    //Метод по поиску пользователя по username и возвращению егов виде объекта AuthUser, который поддерживает интерфейс UserDetails
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUserName(username)
                .orElseThrow(
                () -> new UsernameNotFoundException(String.format("User %s not found", username))
        );
        //вот тут прикол был с прокчи затычкой - сущности password

        return AuthUserDTO.builder()
                .username(user.getUserName())
                .password(user.getPassword().getPassword())
                .authorities(List.of(new SimpleGrantedAuthority(user.getRole().name())))
                .build();
    }
}
