package betolara1.Ponto.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SaveCoordenadasRequest {
    
    @NotNull(message = "Latitude é obrigatório.")
    private String latitude;
    
    @NotNull(message = "Longitude é obrigatório.")
    private String longitude;

    @NotNull(message = "Id da empresa é obrigatório.")
    private Long empresaId;
}
