package dev.vortsu.utils;

import dev.vortsu.repositories.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckingForAccess {

    private final StudentRepository studentRepository;

    public boolean authenticationCheck(Long userId, Authentication authentication) {
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals("TEACHER") || authority.getAuthority().equals("ADMIN")) {
                return true;
            }
            Long foundUserId = studentRepository.findStudentByUsername(authentication.getName()).orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found")).getId();
            return foundUserId.equals(userId);
        }
        return true;
    }
}
