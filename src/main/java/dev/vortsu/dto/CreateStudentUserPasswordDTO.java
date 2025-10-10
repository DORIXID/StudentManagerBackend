package dev.vortsu.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateStudentUserPasswordDTO {
    @Size(min = 3, max = 20)
    private String name;
    @Size(min = 3, max = 20)
    private String surname;
    @Min(value = 15)
    private Integer age;
    @Size(min = 6, max = 30, message = "Минимальный размер пароля - 6 символов")
    private String password;
    @Size(min = 3, max = 30)
    private String userName;
}