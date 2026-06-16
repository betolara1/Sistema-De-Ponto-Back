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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import betolara1.Ponto.dto.PontoDTO;
import betolara1.Ponto.dto.request.SavePontoRequest;
import betolara1.Ponto.dto.request.UpdatePontoRequest;
import betolara1.Ponto.model.Ponto;
import betolara1.Ponto.service.PontoService;
import betolara1.Ponto.utils.PaginationUtils;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/ponto")
public class PontoController {
    private final PontoService pontoService;

    public PontoController(PontoService pontoService) {
        this.pontoService = pontoService;
    }

    @GetMapping
    public ResponseEntity<Page<PontoDTO>> getAll(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateCreated") String sortBy,
            @RequestParam(defaultValue = "desc") @NonNull String direction) {
        Pageable pageable = PaginationUtils.createPageable(page, size, sortBy, direction);

        return ResponseEntity.ok(pontoService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PontoDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(pontoService.findById(id));
    }

    @GetMapping(params = "colabId")
    public ResponseEntity<PontoDTO> getColaboradorId(@RequestParam Long id) {
        return ResponseEntity.ok(pontoService.findByColaboradorId(id));
    }

    @GetMapping(params = "colabUpdater")
    public ResponseEntity<PontoDTO> getColaboradorIdUpdater(@RequestParam Long id) {
        return ResponseEntity.ok(pontoService.findByColaboradorIdUpdater(id));
    }

    @GetMapping(params = "dateCreated")
    public ResponseEntity<Page<PontoDTO>> getDateCreated(@RequestParam String dateString,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateCreated") String sortBy,
            @RequestParam(defaultValue = "desc") @NonNull String direction) {
        Pageable pageable = PaginationUtils.createPageable(page, size, sortBy, direction);
        return ResponseEntity.ok(pontoService.getByPontoCreated(dateString, pageable));
    }

    @GetMapping(params = "dataUpdated")
    public ResponseEntity<Page<PontoDTO>> getDateUpdated(@RequestParam String dateString,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateCreated") String sortBy,
            @RequestParam(defaultValue = "desc") @NonNull String direction) {
        Pageable pageable = PaginationUtils.createPageable(page, size, sortBy, direction);
        return ResponseEntity.ok(pontoService.getByPontoUpdated(dateString, pageable));
    }

    @PostMapping
    public ResponseEntity<PontoDTO> savePonto(@Valid @RequestBody SavePontoRequest request){
        Ponto ponto = pontoService.save(request);
        PontoDTO pontoDTO = new PontoDTO(ponto);

        return ResponseEntity.status(HttpStatus.OK).body(pontoDTO);
    }

    @PutMapping
    public ResponseEntity<PontoDTO> updatePonto(@Valid @RequestBody UpdatePontoRequest request, Long id){
        Ponto ponto = pontoService.update(request, id);
        PontoDTO pontoDTO = new PontoDTO(ponto);

        return ResponseEntity.status(HttpStatus.OK).body(pontoDTO);
    }
}
