package betolara1.Ponto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginDTO (
    @NotBlank(message = "CPF é obrigatório.")
    String cpf, 
    
    @NotBlank(message = "Senha é obrigatório.")
    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres.")
    String password
){}
