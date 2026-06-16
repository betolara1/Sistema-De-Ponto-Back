package betolara1.Ponto.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.jvnet.hk2.annotations.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public PontoDTO findById(Long id){
        Ponto ponto = pontoRepository.findById(id).orElseThrow(() -> new NotFoundException("ID não encontrado. "));
    
        return new PontoDTO(ponto);
    }

    @Transactional(readOnly = true)
    public PontoDTO findByColaboradorId(Long id){
        Ponto ponto = pontoRepository.findByColaboradorId(id).orElseThrow(() -> new NotFoundException("ID do colaborador não encontrado. "));
    
        return new PontoDTO(ponto);
    }

    @Transactional(readOnly = true)
    public PontoDTO findByColaboradorIdUpdater(Long id){
        Ponto ponto = pontoRepository.findByColaboradorUpdate(id).orElseThrow(() -> new NotFoundException("ID do responsável não localizado. "));
        
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
        Page<Ponto> ponto = pontoRepository.findByDateCreatedBetween(startDay, endDay, pageable);

        // 4. Verifica se algum módulo pai foi encontrado
        if(ponto.isEmpty()){
            throw new NotFoundException("Nenhum tamanho encontrado na data: " + dateString);
        }

        // 5. Retorna os módulos pais
        return ponto.map(PontoDTO::new);
    }

    // Método para buscar produtos por data de atualização
    @Transactional(readOnly = true)
    public Page<PontoDTO> getByPontoUpdated(String dateString, Pageable pageable){
        // 1. Converte a String para LocalDate (apenas data)
        LocalDate date = DateUtils.parseDate(dateString);

        // 2. Cria o início do dia (00:00:00) e o fim do dia (23:59:59)
        LocalDateTime startDay = date.atStartOfDay();
        LocalDateTime endDay = date.atTime(LocalTime.MAX);

        // 3. Chama o repositório com o intervalo
        Page<Ponto> ponto = pontoRepository.findByDateUpdatedBetween(startDay, endDay, pageable);

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
    public Ponto update(UpdatePontoRequest request, Long id){
        Ponto ponto = new Ponto();

        Colaboradores colab = colaboradoresRepository.findById(id).orElseThrow(() -> new NotFoundException("Colaborador não encontrado. "));

        if(request.getColaboradorIdUpdate() != null){
            ponto.setColaboradorIdUpdate(colab);
        }

        if(request.getPontoUpdated() != null){
            ponto.setPontoUpdated(request.getPontoUpdated());
        }

        Ponto update = pontoRepository.save(ponto);
        log.info("Ponto do colaborador {} foi alterado.", colab.getNomeColaborador());

        return update;
    }
}
