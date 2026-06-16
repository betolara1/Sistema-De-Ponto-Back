package betolara1.Ponto.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import betolara1.Ponto.dto.PontoDTO;
import betolara1.Ponto.dto.request.SavePontoRequest;
import betolara1.Ponto.dto.request.UpdatePontoRequest;
import betolara1.Ponto.model.Colaboradores;
import betolara1.Ponto.model.Ponto;
import betolara1.Ponto.repository.ColaboradoresRepository;
import betolara1.Ponto.repository.PontoRepository;
import betolara1.Ponto.utils.DateUtils;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PontoService {
    private final PontoRepository pontoRepository;
    private final ColaboradoresRepository colaboradoresRepository;

    public PontoService(PontoRepository pontoRepository, ColaboradoresRepository colaboradoresRepository){
        this.pontoRepository = pontoRepository;
        this.colaboradoresRepository = colaboradoresRepository;
    }

    @Transactional(readOnly = true)
    public Page<PontoDTO> findAll(Pageable pageable){
        Page<Ponto> ponto = pontoRepository.findAll(pageable);

        return ponto.map(PontoDTO::new);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "Ponto", key = "#id")
    public PontoDTO findById(Long id){
        Ponto ponto = pontoRepository.findById(id).orElseThrow(() -> new NotFoundException("ID não encontrado. "));
    
        return new PontoDTO(ponto);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "Ponto", key = "#colabId")
    public PontoDTO findByColaboradorId(Long colabId){
        Ponto ponto = pontoRepository.findByColaboradorId_Id(colabId).orElseThrow(() -> new NotFoundException("ID do colaborador não encontrado. "));
    
        return new PontoDTO(ponto);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "Ponto", key = "#responsalvel")
    public PontoDTO findByColaboradorIdUpdater(Long responsalvel){
        Ponto ponto = pontoRepository.findByColaboradorIdUpdate_Id(responsalvel).orElseThrow(() -> new NotFoundException("ID do responsável não localizado. "));
        
        return new PontoDTO(ponto);
    }

    // Método para buscar produtos por data de criação
    @Transactional(readOnly = true)
    public Page<PontoDTO> getByPontoCreated(String dateString, Pageable pageable){
        // 1. Converte a String para LocalDate (apenas data)
        LocalDate date = DateUtils.parseDate(dateString);

        // 2. Cria o início do dia (00:00:00) e o fim do dia (23:59:59)
        LocalDateTime startDay = date.atStartOfDay();
        LocalDateTime endDay = date.atTime(LocalTime.MAX);

        // 3. Chama o repositório com o intervalo
        Page<Ponto> ponto = pontoRepository.findByPontoBetween(startDay, endDay, pageable);

        // 4. Verifica se algum módulo pai foi encontrado
        if(ponto.isEmpty()){
            throw new NotFoundException("Nenhum tamanho encontrado na data: " + dateString);
        }

        // 5. Retorna os módulos pais
        return ponto.map(PontoDTO::new);
    }

    @Transactional
    public Ponto save(SavePontoRequest request){
        Ponto ponto = new Ponto();

        Colaboradores colab = colaboradoresRepository.findById(request.getColaboradorId()).orElseThrow(() -> new NotFoundException("Colaborador não encontrado. "));

        ponto.setColaboradorId(colab);

        Ponto save = pontoRepository.save(ponto);
        log.info("Ponto {} salvo com sucesso. " +request.getColaboradorId());

        return save;
    }

    @Transactional
    @CacheEvict(value = "Ponto", allEntries = true) 
    public Ponto update(UpdatePontoRequest request, Long idPonto){
        Ponto ponto = pontoRepository.findById(idPonto).orElseThrow(() -> new NotFoundException("Ponto não encontrado."));

        Colaboradores responsavel = colaboradoresRepository.findById(request.getColaboradorIdUpdate()).orElseThrow(() -> new NotFoundException("Responsável pela alteração não encontrado."));
            
        ponto.setColaboradorIdUpdate(responsavel);

        ponto.setPonto(request.getPonto());

        Ponto update = pontoRepository.save(ponto);
        log.info("Ponto de ID {} foi alterado pelo responsável {}.", ponto.getId(), responsavel.getNomeColaborador());

        return update;
    }
}
