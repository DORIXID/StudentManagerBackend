package dev.vortsu.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.vortsu.entity.Role;
import jakarta.validation.constraints.Size;
import lombok.*;


@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private Long id;
    @Size(min = 3, max = 20)
    private String userName;
    @JsonIgnore
    private Long passwordId;
    private Role role;
    private boolean enabled;
    private Long roleId;
}
