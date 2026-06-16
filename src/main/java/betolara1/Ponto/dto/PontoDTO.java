package betolara1.Ponto.dto;

import betolara1.Ponto.model.Ponto;

public record PontoDTO(
    Long id,
    Long colaboradorId,
    Long colaboradorIdUpdate
){
    public PontoDTO (Ponto ponto){
        this(
            ponto.getId(),
            ponto.getColaboradorId() != null ? ponto.getColaboradorId().getId() : null, // para não retornar NullPointerException 
            ponto.getColaboradorIdUpdate() != null ? ponto.getColaboradorIdUpdate().getId() : null // para não retornar NullPointerException 
        );
    }
}
