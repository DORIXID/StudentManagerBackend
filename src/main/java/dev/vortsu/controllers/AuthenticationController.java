package dev.vortsu.controllers;

import dev.vortsu.dto.auth.JwtRequest;
import dev.vortsu.dto.auth.JwtResponse;
import dev.vortsu.service.auth.JwtUserDetailsService;
import dev.vortsu.service.auth.TokenManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class AuthenticationController {

    private final JwtUserDetailsService userDetailsService;
    @Autowired
    AuthenticationManager authenticationManager;
    private final TokenManager tokenManager;

    @PostMapping("api/login/new")
    public JwtResponse createToken(@RequestBody JwtRequest request) throws Exception {
            log.debug("\n\nПопытка создать токен аутентификации");
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
            log.debug("\n\nПытаемся аутентифицировать полученный токен");
            authenticationManager.authenticate(token);
            log.debug("\n\nТокен создан");
        }
        catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        }
        catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        log.debug("\n\nпользователь найден"+userDetails.getUsername() + " " + userDetails.getPassword());
        return new JwtResponse(tokenManager.generateJwtToken(userDetails));
    }
}
