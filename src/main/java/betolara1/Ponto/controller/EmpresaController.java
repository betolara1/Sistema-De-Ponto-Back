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

import betolara1.Ponto.dto.EmpresaDTO;
import betolara1.Ponto.dto.request.SaveEmpresaRequest;
import betolara1.Ponto.dto.request.UpdateEmpresaRequest;
import betolara1.Ponto.model.Empresa;
import betolara1.Ponto.service.EmpresaService;
import betolara1.Ponto.utils.PaginationUtils;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/empresa")
public class EmpresaController {
    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService){
        this.empresaService = empresaService;
    }

    @SuppressWarnings("null")
    @GetMapping
    public ResponseEntity<Page<EmpresaDTO>> getAll( @RequestParam(defaultValue="0") int page, 
                                                    @RequestParam(defaultValue="10") int size,
                                                    @RequestParam(defaultValue = "dateCreated") String sortBy,
                                                    @RequestParam(defaultValue = "desc") String direction
    ){
        Pageable pageable = PaginationUtils.createPageable(page, size, sortBy, direction);
        return ResponseEntity.ok(empresaService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaDTO> getById(@PathVariable @NonNull Long id){
        return ResponseEntity.ok(empresaService.findById(id));
    }

    @GetMapping(params = "active")
    public ResponseEntity<Page<EmpresaDTO>> getActive(  @RequestParam("active") Boolean isActive,
                                                        @RequestParam(defaultValue="0") int page, 
                                                        @RequestParam(defaultValue="10") int size,
                                                        @RequestParam(defaultValue = "dateCreated") String sortBy,
                                                        @RequestParam(defaultValue = "desc") @NonNull String direction
    ){
        Pageable pageable = PaginationUtils.createPageable(page, size, sortBy, direction);
        return ResponseEntity.ok(empresaService.findByIsActive(isActive, pageable));
    }

    @GetMapping(params = "dateCreated")
    public ResponseEntity<Page<EmpresaDTO>> getDateCreated( @RequestParam("dateCreated") String dateString,
                                                            @RequestParam(defaultValue="0") int page, 
                                                            @RequestParam(defaultValue="10") int size,
                                                            @RequestParam(defaultValue = "dateCreated") String sortBy,
                                                            @RequestParam(defaultValue = "desc") @NonNull String direction
    ){
        
        // Chamada estática: Classe.metodo()
        Pageable pageable = PaginationUtils.createPageable(page, size, sortBy, direction);
        return ResponseEntity.ok(empresaService.getByDateCreated(dateString, pageable));
    }

    @GetMapping(params = "dataUpdated")
    public ResponseEntity<Page<EmpresaDTO>> getDateUpdated( @RequestParam("dataUpdated") String dateString,                                                 
                                                            @RequestParam(defaultValue="0") int page, 
                                                            @RequestParam(defaultValue="10") int size,
                                                            @RequestParam(defaultValue = "dateCreated") String sortBy,
                                                            @RequestParam(defaultValue = "desc") @NonNull String direction
    ){
        
        // Chamada estática: Classe.metodo()
        Pageable pageable = PaginationUtils.createPageable(page, size, sortBy, direction);
        return ResponseEntity.ok(empresaService.getByDateUpdated(dateString, pageable));
    }

    @GetMapping(params = "nome")
    public ResponseEntity<EmpresaDTO> getNome(@RequestParam("nome") String nome){
        return ResponseEntity.ok(empresaService.findByName(nome));
    }

    @GetMapping(params = "cnpj")
    public ResponseEntity<EmpresaDTO> getCNPJ(@RequestParam("cnpj") String cnpj){
        return ResponseEntity.ok(empresaService.findByCNPJ(cnpj));
    }

    @PostMapping
    public ResponseEntity<EmpresaDTO> saveEmpresa(@Valid @RequestBody SaveEmpresaRequest request){
        Empresa empresa = empresaService.save(request);
        EmpresaDTO empresaDTO = new EmpresaDTO(empresa);

        return ResponseEntity.status(HttpStatus.OK).body(empresaDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpresaDTO> updateEmpresa(@Valid @RequestBody UpdateEmpresaRequest request, @PathVariable Long id){
        Empresa empresa = empresaService.update(id, request);
        EmpresaDTO empresaDTO = new EmpresaDTO(empresa);

        return ResponseEntity.status(HttpStatus.OK).body(empresaDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> disableEmpresa(@PathVariable Long id){
        empresaService.disable(id);
        return ResponseEntity.noContent().build();
    }    
}
