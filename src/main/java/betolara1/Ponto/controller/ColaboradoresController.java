package betolara1.Ponto.controller;

import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import betolara1.Ponto.dto.ColaboradoresDTO;
import betolara1.Ponto.dto.request.SaveColaboradoresRequest;
import betolara1.Ponto.dto.request.UpdateColaboradoresRequest;
import betolara1.Ponto.model.Colaboradores;
import betolara1.Ponto.service.ColaboradoresService;
import betolara1.Ponto.utils.PaginationUtils;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/colaboradores")
public class ColaboradoresController {
    private final ColaboradoresService colaboradoresService;

    public ColaboradoresController(ColaboradoresService colaboradoresService){
        this.colaboradoresService = colaboradoresService;
    }

    @GetMapping
    public ResponseEntity<Page<ColaboradoresDTO>> getAll( @RequestParam(defaultValue="0") int page, 
                                                        @RequestParam(defaultValue="10") int size,
                                                        @RequestParam(defaultValue = "dateCreated") String sortBy,
                                                        @RequestParam(defaultValue = "desc") String direction
    ){
        Pageable pageable = PaginationUtils.createPageable(page, size, sortBy, direction);

        return ResponseEntity.ok(colaboradoresService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColaboradoresDTO> getById(@PathVariable Long id){
        return ResponseEntity.ok(colaboradoresService.findById(id));
    }

    @GetMapping(params = "nome")
    public ResponseEntity<ColaboradoresDTO> getByNome(@RequestParam String nome){
        return ResponseEntity.ok(colaboradoresService.findByNome(nome));
    }

    @GetMapping(params = "cpf")
    public ResponseEntity<ColaboradoresDTO> getByCpf(@RequestParam String cpf){
        return ResponseEntity.ok(colaboradoresService.findByCpf(cpf));
    }

    @GetMapping(params = "empresaId")
    public ResponseEntity<ColaboradoresDTO> getByEmpresaId(@RequestParam Long empresaId){
        return ResponseEntity.ok(colaboradoresService.findByEmpresaId(empresaId));
    }

    @GetMapping(params = "active")
    public ResponseEntity<Page<ColaboradoresDTO>> getActive(@RequestParam Boolean isActive,
                                                            @RequestParam(defaultValue="0") int page, 
                                                            @RequestParam(defaultValue="10") int size,
                                                            @RequestParam(defaultValue = "dateCreated") String sortBy,
                                                            @RequestParam(defaultValue = "desc") @NonNull String direction
    ){
        Pageable pageable = PaginationUtils.createPageable(page, size, sortBy, direction);
        return ResponseEntity.ok(colaboradoresService.findByIsActive(isActive, pageable));
    }

    @GetMapping(params = "dateCreated")
    public ResponseEntity<Page<ColaboradoresDTO>> getDateCreated(@RequestParam String dateString,
                                                                @RequestParam(defaultValue="0") int page, 
                                                                @RequestParam(defaultValue="10") int size,
                                                                @RequestParam(defaultValue = "dateCreated") String sortBy,
                                                                @RequestParam(defaultValue = "desc") @NonNull String direction
    ){
        
        // Chamada estática: Classe.metodo()
        Pageable pageable = PaginationUtils.createPageable(page, size, sortBy, direction);
        return ResponseEntity.ok(colaboradoresService.getByDateCreated(dateString, pageable));
    }

    @GetMapping(params = "dataUpdated")
    public ResponseEntity<Page<ColaboradoresDTO>> getDateUpdated(@RequestParam String dateString,                                                 
                                                                @RequestParam(defaultValue="0") int page, 
                                                                @RequestParam(defaultValue="10") int size,
                                                                @RequestParam(defaultValue = "dateCreated") String sortBy,
                                                                @RequestParam(defaultValue = "desc") @NonNull String direction
    ){
        
        // Chamada estática: Classe.metodo()
        Pageable pageable = PaginationUtils.createPageable(page, size, sortBy, direction);
        return ResponseEntity.ok(colaboradoresService.getByDateUpdated(dateString, pageable));
    }

    @PostMapping
    public ResponseEntity<ColaboradoresDTO> saveColaboradores(@Valid @RequestBody SaveColaboradoresRequest request){
        Colaboradores colab = colaboradoresService.save(request);
        ColaboradoresDTO colabDTO = new ColaboradoresDTO(colab);

        return ResponseEntity.status(HttpStatus.OK).body(colabDTO);
    }

    @PutMapping
    public ResponseEntity<ColaboradoresDTO> updateColaboradores(@Valid @RequestBody UpdateColaboradoresRequest request, long id){
        Colaboradores colab = colaboradoresService.update(id, request);
        ColaboradoresDTO colabDTO = new ColaboradoresDTO(colab);

        return ResponseEntity.status(HttpStatus.OK).body(colabDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> disableColaborador(@PathVariable Long id){
        colaboradoresService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
