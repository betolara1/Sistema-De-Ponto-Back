package betolara1.Ponto.dto;

import betolara1.Ponto.model.Fotos;

public record FotosDTO(
    Long id,
    byte[] foto,
    Long colaboradorId
) {

    public FotosDTO(Fotos foto){
        this(
            foto.getId(),
            foto.getFoto(),
            foto.getColaboradorId() != null ? foto.getColaboradorId().getId() : null
        );
    }
}
