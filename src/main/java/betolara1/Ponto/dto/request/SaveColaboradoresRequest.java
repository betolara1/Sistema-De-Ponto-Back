package betolara1.Ponto.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SaveColaboradoresRequest {
    @NotBlank(message = "Nome do colaborador é obrigatorio.")
    private String nomeColaborador;

    @NotBlank(message = "Senha é obrigatorio.")
    private String senha;

    @NotBlank(message = "CPF do colaborador é obrigatorio.")
    private String cpf;

    @NotNull(message = "Id da empresa é obrigatório.")
    private Long empresaId;

    @NotNull
    private Boolean isActive = true;
}
