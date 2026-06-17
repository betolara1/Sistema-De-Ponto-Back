package betolara1.Ponto.dto.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class UpdateCoordenadasRequest {
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Long empresaId;
}
