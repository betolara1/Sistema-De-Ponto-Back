package betolara1.Ponto.dto.request;

import lombok.Data;

@Data
public class UpdateColaboradoresRequest {
    private String nomeColaborador;
    private String senha;
    private String cpf;
    private Long empresaId;
    private Boolean isActive = true;
}
