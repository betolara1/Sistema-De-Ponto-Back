package betolara1.Ponto.dto;

import java.time.LocalDateTime;

import betolara1.Ponto.model.Coordenadas;

public record CoordenadasDTO(
    Long id,
    String latitude,
    String longitude,

    Long empresaId,

    LocalDateTime dateCreated, 
    LocalDateTime dateUpdated
) {

    public CoordenadasDTO(Coordenadas cood){
        this(
            cood.getId(),
            cood.getLatitude(),
            cood.getLongitude(),
            cood.getEmpresaId() != null ? cood.getEmpresaId().getId() : null,
            cood.getDateCreated(),
            cood.getDateUpdated()
        );
    }
    
}
