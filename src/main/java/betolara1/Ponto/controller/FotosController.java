package betolara1.Ponto.controller;

import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import betolara1.Ponto.dto.FotosDTO;
import betolara1.Ponto.dto.request.SaveFotosRequest;
import betolara1.Ponto.dto.request.UpdateFotosRequest;
import betolara1.Ponto.model.Fotos;
import betolara1.Ponto.service.FotosService;
import betolara1.Ponto.utils.PaginationUtils;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/fotos")
public class FotosController {

    private final FotosService fotosService;

    public FotosController(FotosService fotosService){
        this.fotosService = fotosService;
    }

    @GetMapping
    public ResponseEntity<Page<FotosDTO>> getAll(@RequestParam(defaultValue="0") int page, 
                                 @RequestParam(defaultValue="10") int size,
                                 @RequestParam(defaultValue = "dateCreated") String sortBy,
                                 @RequestParam(defaultValue = "desc") @NonNull String direction
    ){
        Pageable pageable = PaginationUtils.createPageable(page, size, sortBy, direction);

        return ResponseEntity.ok(fotosService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FotosDTO> getById(@PathVariable Long id){
        return ResponseEntity.ok(fotosService.findById(id));
    }

    @GetMapping(params = "colaboradorId")
    public ResponseEntity<FotosDTO> getColaboradorId(@RequestParam Long id){
        return ResponseEntity.ok(fotosService.findByColaboradorId(id));
    }

    @PostMapping
    public ResponseEntity<FotosDTO> getSaveFotos(@Valid @RequestBody SaveFotosRequest request){
        Fotos foto = fotosService.save(request);
        FotosDTO fotoDTO = new FotosDTO(foto);

        return ResponseEntity.status(HttpStatus.OK).body(fotoDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FotosDTO> getUpdateFotos(@Valid @RequestBody UpdateFotosRequest request, Long id){
        Fotos foto = fotosService.update(request, id);
        FotosDTO fotoDTO = new FotosDTO(foto);

        return ResponseEntity.status(HttpStatus.OK).body(fotoDTO);
    }

}