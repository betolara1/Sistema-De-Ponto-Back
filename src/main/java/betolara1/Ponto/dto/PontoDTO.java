package betolara1.Ponto.dto;

import betolara1.Ponto.model.Ponto;

public record PontoDTO(
    Long id,
    Long colaboradorId,
    Long colaboradorIdUpdate
){
    public PontoDTO (Ponto ponto){
        this(
            ponto.getColaboradorId().getId(),
            ponto.getColaboradorIdUpdate().getId(),
            ponto.getId()
        );
    }
}
