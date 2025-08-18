package dev.vortsu.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PasswordDTO {

    @Size(min = 6, max = 30, message = "Минимальный размер пароля - 6 символов")
    private String password;

    private Long id;

}