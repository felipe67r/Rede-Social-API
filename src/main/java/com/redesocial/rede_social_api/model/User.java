package com.redesocial.rede_social_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Usuário não pode ser vazio")
    @Size(min = 3, max = 30, message = "Nome de usuário deve ter entre 3 e 30 caracteres")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "A senha não pode ser vazia")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "Email não pode ser vazio")
    @Email(message = "Formato de email inválido")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Primeiro nome não pode ser vazio")
    @Size(max = 50, message = "Primeiro nome não pode ser maior que 50 caracteres")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Último nome não pode ser vazio")
    @Size(max = 100, message = "Último nome nao pode ser maior que 50 caracteres")
    @Column(nullable = false)
    private String lastName;
}