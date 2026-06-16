package betolara1.Ponto.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import betolara1.Ponto.dto.EmpresaDTO;
import betolara1.Ponto.dto.request.SaveEmpresaRequest;
import betolara1.Ponto.dto.request.UpdateEmpresaRequest;
import betolara1.Ponto.model.Empresa;
import betolara1.Ponto.repository.EmpresaRepository;
import betolara1.Ponto.utils.DateUtils;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmpresaService {
    private final EmpresaRepository empresaRepository;

    public EmpresaService(EmpresaRepository empresaRepository){
        this.empresaRepository = empresaRepository;
    }

    @Transactional(readOnly = true)
    public Page<EmpresaDTO> findAll(@NonNull Pageable pageable){
        Page<Empresa> empresa = empresaRepository.findAll(pageable);

        return empresa.map(EmpresaDTO::new);
    }

    @Transactional(readOnly = true)
    public EmpresaDTO findById(@NonNull Long id){
        Empresa empresa = empresaRepository.findById(id).orElseThrow(() -> new NotFoundException("Id " +id+ " não encontrado"));

        return new EmpresaDTO(empresa);
    }

    @Transactional(readOnly = true)
    public Page<EmpresaDTO> findByIsActive(Boolean isActive, Pageable pageable){
        Page<Empresa> empresa = empresaRepository.findByIsActive(isActive, pageable);

        return empresa.map(EmpresaDTO::new);
    }

    // Método para buscar produtos por data de criação
    @Transactional(readOnly = true)
    public Page<EmpresaDTO> getByDateCreated(String dateString, Pageable pageable){
        // 1. Converte a String para LocalDate (apenas data)
        LocalDate date = DateUtils.parseDate(dateString);

        // 2. Cria o início do dia (00:00:00) e o fim do dia (23:59:59)
        LocalDateTime startDay = date.atStartOfDay();
        LocalDateTime endDay = date.atTime(LocalTime.MAX);

        // 3. Chama o repositório com o intervalo
        Page<Empresa> empresa = empresaRepository.findByDateCreatedBetween(startDay, endDay, pageable);

        // 4. Verifica se algum módulo pai foi encontrado
        if(empresa.isEmpty()){
            throw new NotFoundException("Nenhum tamanho encontrado na data: " + dateString);
        }

        // 5. Retorna os módulos pais
        return empresa.map(EmpresaDTO::new);
    }

    // Método para buscar produtos por data de atualização
    @Transactional(readOnly = true)
    public Page<EmpresaDTO> getByDateUpdated(String dateString, Pageable pageable){
        // 1. Converte a String para LocalDate (apenas data)
        LocalDate date = DateUtils.parseDate(dateString);

        // 2. Cria o início do dia (00:00:00) e o fim do dia (23:59:59)
        LocalDateTime startDay = date.atStartOfDay();
        LocalDateTime endDay = date.atTime(LocalTime.MAX);

        // 3. Chama o repositório com o intervalo
        Page<Empresa> empresa = empresaRepository.findByDateUpdatedBetween(startDay, endDay, pageable);

        // 4. Verifica se algum módulo pai foi encontrado
        if(empresa.isEmpty()){
            throw new NotFoundException("Nenhum tamanho encontrado na data: " + dateString);
        }
        
        // 5. Retorna os módulos pais
        return empresa.map(EmpresaDTO::new);
    }


    @Transactional(readOnly = true)
    public EmpresaDTO findByName(String nome){
        Empresa empresa = empresaRepository.findByNomeEmpresaContainingIgnoreCase(nome);

        if(empresa == null){
            throw new NotFoundException("Nome " +nome+ "não encontrado.");
        }

        return new EmpresaDTO(empresa);
    }

    @Transactional(readOnly = true)
    public EmpresaDTO findByCNPJ(String cnpj){
        Empresa empresa = empresaRepository.findByCnpjContaining(cnpj);

        if(empresa == null){
            throw new NotFoundException("CNPJ "+cnpj+" não encontrado.");
        }

        return new EmpresaDTO(empresa);
    }

    @Transactional
    public Empresa save(SaveEmpresaRequest request){
        Empresa empresa = new Empresa();

        empresa.setBairro(request.getBairro());
        empresa.setCep(request.getCep());
        empresa.setCidade(request.getCidade());
        empresa.setCnpj(request.getCnpj());
        empresa.setEndereco(request.getEndereco());
        empresa.setNomeEmpresa(request.getNomeEmpresa());
        empresa.setIsActive(true);

        Empresa saved = empresaRepository.save(empresa);
        log.info("Empresa {} salvo com sucesso." +request.getNomeEmpresa());

        return saved;
    }

    @Transactional
    public Empresa update(Long id, UpdateEmpresaRequest request){
        Empresa empresa = empresaRepository.findById(id).orElseThrow(() -> new NotFoundException("ID da empresa não encontrado."));

        if(request.getBairro() != null){
            empresa.setBairro(request.getBairro());
        }

        if(request.getCep() != null){
            empresa.setCep(request.getCep());
        }

        if(request.getCidade() != null){
            empresa.setCidade(request.getCidade());
        }

        if(request.getCnpj() != null){
            empresa.setCnpj(request.getCnpj());
        }

        if(request.getEndereco() != null){
            empresa.setEndereco(request.getEndereco());
        }

        if(request.getNomeEmpresa() != null){
            empresa.setNomeEmpresa(request.getNomeEmpresa());
        }

        if(request.getIsActive() != null){
            empresa.setIsActive(request.getIsActive());
        }

        Empresa updated = empresaRepository.save(empresa);
        log.info("Empresa {} foi alterado. ", request.getNomeEmpresa());

        return updated;
    }

    public void disable(Long id){
        Empresa empresa = empresaRepository.findById(id).orElseThrow(() -> new NotFoundException("Empresa não encontrada. "));

        empresa.setIsActive(false);
        empresaRepository.save(empresa);
    }
}
