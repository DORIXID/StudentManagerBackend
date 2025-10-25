package dev.vortsu.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Builder
@Setter @Getter
public class AuthUserDTO implements UserDetails {

    private String password;
    private String username;

    private List<SimpleGrantedAuthority> authorities;
}