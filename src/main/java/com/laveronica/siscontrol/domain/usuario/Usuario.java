package com.laveronica.siscontrol.domain.usuario;

import com.laveronica.siscontrol.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "usuarios")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private Boolean activo;

    public Usuario(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.activo = true;
    }
}
