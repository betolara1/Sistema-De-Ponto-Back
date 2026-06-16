package betolara1.Ponto.dto;

import java.time.LocalDateTime;

import betolara1.Ponto.model.Colaboradores;

public record ColaboradoresDTO(
    Long id,
    String nomeColaborador,
    String cpf,
    Long empresaId,
    Boolean isActive,
    LocalDateTime dateCreated,
    LocalDateTime dateUpdated
) {

    public ColaboradoresDTO(Colaboradores col){
        this(
            col.getId(),
            col.getNomeColaborador(),
            col.getCpf(),
            col.getEmpresaId() != null ? col.getEmpresaId().getId() : null,
            col.getIsActive(),
            col.getDateCreated(),
            col.getDateUpdated()
        );
    }

}
