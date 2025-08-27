package dev.vortsu.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class updateStudentUserPasswordDTO {

    @Min(value = 1)
    private Long id;
    @Size(min = 3, max = 20)
    private String name;
    @Size(min = 3, max = 20)
    private String surname;
    @Min(value = 15)
    private Integer age;
    private String password;
    private String userName;
}
