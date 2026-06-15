package betolara1.Ponto.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import betolara1.Ponto.dto.CoordenadasDTO;
import betolara1.Ponto.dto.request.SaveCoordenadasRequest;
import betolara1.Ponto.dto.request.UpdateCoordenadasRequest;
import betolara1.Ponto.model.Coordenadas;
import betolara1.Ponto.service.CoordenadasService;
import betolara1.Ponto.utils.PaginationUtils;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/coordenadas")
public class CoordenadasController {
    private final CoordenadasService coordenadasService;
    
    public CoordenadasController(CoordenadasService coordenadasService){
        this.coordenadasService = coordenadasService;
    }

    @GetMapping
    public ResponseEntity<Page<CoordenadasDTO>> getAll( @RequestParam(defaultValue="0") int page, 
                                                    @RequestParam(defaultValue="10") int size,
                                                    @RequestParam(defaultValue = "dateCreated") String sortBy,
                                                    @RequestParam(defaultValue = "desc") @NonNull String direction
    ){
        Pageable pageable = PaginationUtils.createPageable(page, size, sortBy, direction);
        return ResponseEntity.ok(coordenadasService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoordenadasDTO> getByEmpresaId (@PathVariable Long id){
        return ResponseEntity.ok(coordenadasService.findByEmpresaId(id));
    }

    @PostMapping
    public ResponseEntity<CoordenadasDTO> saveCoordenadas(@Valid @RequestBody SaveCoordenadasRequest request){
        Coordenadas coord = coordenadasService.save(request);

        CoordenadasDTO coordDTO = new CoordenadasDTO(coord);

        return ResponseEntity.status(HttpStatus.CREATED).body(coordDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CoordenadasDTO> updateCoordenadas(@Valid @RequestBody UpdateCoordenadasRequest request, @PathVariable Long id){
        Coordenadas coord = coordenadasService.update(request, id);
        CoordenadasDTO coordDTO = new CoordenadasDTO(coord);

        return ResponseEntity.status(HttpStatus.OK).body(coordDTO);
    }
}
