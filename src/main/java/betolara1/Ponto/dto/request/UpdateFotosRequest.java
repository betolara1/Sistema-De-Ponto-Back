package betolara1.Ponto.dto.request;

import lombok.Data;

@Data
public class UpdateFotosRequest {
    private byte[] foto;

    private Long colaboradorId;
}
