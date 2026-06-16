package betolara1.Ponto.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import betolara1.Ponto.dto.CoordenadasDTO;
import betolara1.Ponto.dto.request.SaveCoordenadasRequest;
import betolara1.Ponto.dto.request.UpdateCoordenadasRequest;
import betolara1.Ponto.model.Coordenadas;
import betolara1.Ponto.model.Empresa;
import betolara1.Ponto.repository.CoordenadasRepository;
import betolara1.Ponto.repository.EmpresaRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CoordenadasService {
    private final CoordenadasRepository coordenadasRepository;
    private final EmpresaRepository empresaRepository;

    public CoordenadasService (CoordenadasRepository coordenadasRepository, EmpresaRepository empresaRepository){
        this.coordenadasRepository = coordenadasRepository;
        this.empresaRepository = empresaRepository;
    }

    @Transactional(readOnly = true)
    public Page<CoordenadasDTO> findAll(Pageable pageable){
        Page<Coordenadas> coord = coordenadasRepository.findAll(pageable);

        if(coord.isEmpty()){
            throw new NotFoundException("Não há nenhum registro.");
        }

        return coord.map(CoordenadasDTO:: new);
    }

    @Transactional(readOnly = true)
    public CoordenadasDTO findByEmpresaId(Long empresaId){
        Coordenadas coord = coordenadasRepository.findByEmpresaIdId(empresaId)
            .orElseThrow(() -> new NotFoundException("Coordenadas não encontradas para a empresa com ID: " + empresaId));

        return new CoordenadasDTO(coord);
    }

    @Transactional
    public Coordenadas save(SaveCoordenadasRequest request){
        Coordenadas coord = new Coordenadas();

        Empresa empresaId = empresaRepository.findById(request.getEmpresaId()).orElseThrow(() -> new NotFoundException("O ID:" +request.getEmpresaId()+ " da Empresa não foi encontrado "));

        coord.setLatitude(request.getLatitude());
        coord.setLongitude(request.getLongitude());
        coord.setEmpresaId(empresaId);

        Coordenadas saved = coordenadasRepository.save(coord);
        log.info("Coordenadas salvo com sucesso.");

        return saved;
    }

    @Transactional
    public Coordenadas update(UpdateCoordenadasRequest request, Long id){
        Coordenadas coord = coordenadasRepository.findById(id).orElseThrow(() -> new NotFoundException("Coordenadas com ID " + id + " não encontradas"));

        if(request.getEmpresaId() != null){

            Empresa empresaId = empresaRepository.findById(request.getEmpresaId()).orElseThrow(() -> new NotFoundException("O ID: " + request.getEmpresaId() + " da Empresa não foi encontrado"));
            coord.setEmpresaId(empresaId);
        }

        if(request.getLatitude() != null){
            coord.setLatitude(request.getLatitude());
        }

        if(request.getLongitude() != null){
            coord.setLongitude(request.getLongitude());
        }

        Coordenadas updated = coordenadasRepository.save(coord);
        log.info("Coordenadas {} foram alteradas. ", id);

        return updated;
    }
}
