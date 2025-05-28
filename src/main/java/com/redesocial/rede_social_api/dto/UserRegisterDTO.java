package com.redesocial.rede_social_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "DTO para o registro de um novo usuário")
public class UserRegisterDTO {

    @NotBlank(message = "Nome de usuário é obrigatório")
    @Size(min = 3, max = 50, message = "Nome de usuário deve ter entre 3 e 50 caracteres")
    @Schema(description = "Nome de usuário único para login", example = "novo_usuario_123")
    private String username;

    @Email(message = "Email inválido")
    @NotBlank(message = "Email é obrigatório")
    @Schema(description = "Endereço de email único do usuário", example = "email.novo@example.com")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    @Schema(description = "Senha do usuário (mínimo 6 caracteres)", example = "SenhaSegura123")
    private String password;

    @NotBlank(message = "Primeiro nome é obrigatório")
    @Size(max = 100, message = "Primeiro nome deve ter no máximo 100 caracteres")
    @Schema(description = "Primeiro nome do usuário", example = "Maria")
    private String firstName;

    @NotBlank(message = "Sobrenome é obrigatório")
    @Size(max = 100, message = "Sobrenome deve ter no máximo 100 caracteres")
    @Schema(description = "Sobrenome do usuário", example = "Silva")
    private String lastName;
}