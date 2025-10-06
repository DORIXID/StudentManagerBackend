package dev.vortsu.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordEncoding {
    private final org.springframework.security.crypto.password.PasswordEncoder encoder;

    public String encode(String password) {
        return password.isBlank() ?  null : (encoder.encode(password));
    }
}
