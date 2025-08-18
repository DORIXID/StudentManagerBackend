package dev.vortsu.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordEncoding {
    private final org.springframework.security.crypto.password.PasswordEncoder encoder;

    private final String prefix = "";

    public String encode(String password) {
        return password == null ? null : (prefix + encoder.encode(password));
    }

    public boolean matches(String raw, String hash) {
        return raw != null && hash != null && encoder.matches(raw, hash);
    }
}
