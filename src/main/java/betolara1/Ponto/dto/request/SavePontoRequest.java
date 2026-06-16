package betolara1.Ponto.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SavePontoRequest {
    @NotNull(message =  "ID do Colaborador que bateu ponto é obrigatório.")
    private Long colaboradorId;
}
