package betolara1.Ponto.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import betolara1.Ponto.dto.FotosDTO;
import betolara1.Ponto.dto.request.SaveFotosRequest;
import betolara1.Ponto.dto.request.UpdateFotosRequest;
import betolara1.Ponto.model.Colaboradores;
import betolara1.Ponto.model.Fotos;
import betolara1.Ponto.repository.ColaboradoresRepository;
import betolara1.Ponto.repository.FotosRepository;
import org.springframework.transaction.annotation.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FotosService {
    private final FotosRepository fotosRepository;
    private final ColaboradoresRepository colaboradoresRepository;

    public FotosService(FotosRepository fotosRepository, ColaboradoresRepository colaboradoresRepository){
        this.fotosRepository = fotosRepository;
        this.colaboradoresRepository = colaboradoresRepository;
    }

    @Transactional(readOnly = true)
    public Page<FotosDTO> findAll(Pageable pageable){
        Page<Fotos> fotos = fotosRepository.findAll(pageable);
        
        if(fotos.isEmpty()){
            throw new NotFoundException("Não há nenhuma foto cadastrada.");
        }

        return fotos.map(FotosDTO::new);
    }

    @Transactional(readOnly = true)
    public FotosDTO findById(Long id){
        Fotos fotos = fotosRepository.findById(id).orElseThrow(() -> new NotFoundException("Id "+id+" não encontrado."));

        return new FotosDTO(fotos);
    }

    @Transactional(readOnly = true)
    public FotosDTO findByColaboradorId(Long id){
        Fotos foto = fotosRepository.findByColaboradorId(id).orElseThrow(() -> new NotFoundException("Colaborador com ID "+id+" não encontrado."));

        return new FotosDTO(foto);
    }

    @Transactional
    public Fotos save(SaveFotosRequest request){
        Fotos foto = new Fotos();

        Colaboradores colab = colaboradoresRepository.findById(request.getColaboradorId()).orElseThrow(() -> new NotFoundException("Colaborador com ID " +request.getColaboradorId()+ " não encontrado."));
    
        foto.setColaboradoresId(colab);
        foto.setFoto(request.getFoto());

        Fotos saved = fotosRepository.save(foto);
        log.info("Fotos salva com sucesso.");

        return saved;
    }

    @Transactional
    public Fotos update(UpdateFotosRequest request, Long id){
        Fotos foto = new Fotos();

        if(request.getFoto() != null){
            foto.setFoto(request.getFoto());
        }

        if(request.getColaboradorId() != null){
            Colaboradores colab = colaboradoresRepository.findById(request.getColaboradorId()).orElseThrow(() -> new NotFoundException("ID do colaborador "+request.getColaboradorId()+" não encontrado."));
            foto.setColaboradoresId(colab);
        }

        Fotos update = fotosRepository.save(foto);
        log.info("Foto do ID: {} foi alterada.", id);

        return update;
    }
}
