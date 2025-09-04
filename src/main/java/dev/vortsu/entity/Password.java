package dev.vortsu.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "passwords")
public class Password {

    //TODO: добавить дтохам приписку Entity
    private String password;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
}
