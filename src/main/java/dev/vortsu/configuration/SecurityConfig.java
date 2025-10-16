package dev.vortsu.configuration;

import dev.vortsu.entity.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    //todo jwt зафигачить
    @Bean
    public org.springframework.security.crypto.password.PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/api/login/**")
                .permitAll().requestMatchers("/api/base/students/**")
                .hasAnyAuthority(Role.STUDENT.name(), Role.ADMIN.name(), Role.TEACHER.name())
                .anyRequest().authenticated())
                .httpBasic(httpBasic -> httpBasic.authenticationEntryPoint((request, response, authException) -> response.setStatus(HttpStatus.UNAUTHORIZED.value()))).csrf(csrf -> csrf.disable()).cors(cors -> cors.disable());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        manager.setUsersByUsernameQuery(

                "SELECT user_name, p.password AS password, enabled FROM users u INNER JOIN passwords p ON u.password_id = p.id WHERE user_name=?");
        manager.setAuthoritiesByUsernameQuery("SELECT user_name, role FROM users WHERE user_name=?");
        return manager;
    }

}