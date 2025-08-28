package dev.vortsu.utils;

import dev.vortsu.repositories.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChekingForAccess {

    private final StudentRepository studentRepository;

    public boolean authenticationCheck(Long id, Authentication authentication) {
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals("TEACHER") || authority.getAuthority().equals("ADMIN")) {
                return true;
            }
            Long userId = studentRepository.findByUser_UserName(authentication.getName()).orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found")).getId();
            return userId.equals(id);
        }
        return true;
    }
}
