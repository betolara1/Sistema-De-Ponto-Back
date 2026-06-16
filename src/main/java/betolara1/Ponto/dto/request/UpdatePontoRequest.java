package betolara1.Ponto.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdatePontoRequest {
    @NotNull(message =  "ID do Colaborador que alterou ponto é obrigatório.")
    private Long colaboradorIdUpdate;

    private LocalDateTime ponto;
}
