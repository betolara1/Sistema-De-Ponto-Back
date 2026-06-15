package betolara1.Ponto.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SaveFotosRequest {
    @NotNull(message = "A imagem é obrigatório.")
    private byte[] foto;

    @NotNull(message = "Id do colaborador é obrigatório.")
    private Long colaboradorId;
}
