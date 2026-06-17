package betolara1.Ponto.service;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;

import betolara1.Ponto.dto.ColaboradoresDTO;
import betolara1.Ponto.dto.request.SaveColaboradoresRequest;
import betolara1.Ponto.dto.request.UpdateColaboradoresRequest;
import betolara1.Ponto.model.Colaboradores;
import betolara1.Ponto.model.Empresa;
import betolara1.Ponto.repository.ColaboradoresRepository;
import betolara1.Ponto.repository.EmpresaRepository;
import betolara1.Ponto.utils.DateUtils;
import jakarta.ws.rs.NotFoundException;

import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ColaboradoresService {
    private final ColaboradoresRepository colaboradoresRepository;
    private final EmpresaRepository empresaRepository;
    private final PasswordEncoder passwordEncoder;

    public ColaboradoresService(ColaboradoresRepository colaboradoresRepository, EmpresaRepository empresaRepository, PasswordEncoder passwordEncoder){
        this.colaboradoresRepository = colaboradoresRepository;
        this.empresaRepository = empresaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Page<ColaboradoresDTO> findAll (@NonNull Pageable pageable){
        Page<Colaboradores> colab = colaboradoresRepository.findAll(pageable);
        
        if(colab.isEmpty()){
            throw new NotFoundException("Nenhum colaborador encontrado.");
        }

        return colab.map(ColaboradoresDTO::new);
    }

    // BUSCA POR ID: Salvamos no cache "colaboradores" usando o ID como chave
    @Transactional(readOnly = true)
    @Cacheable(value = "colaboradores", key = "#id")
    public ColaboradoresDTO findById(@NonNull Long id){
        Colaboradores colab = colaboradoresRepository.findById(id).orElseThrow(() -> new NotFoundException("Id " +id+ " não encontrado."));

        return new ColaboradoresDTO(colab);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "colaboradores", key = "#nome")
    public ColaboradoresDTO findByNome(String nome){
        Colaboradores colab = colaboradoresRepository.findByNomeColaboradorContainingIgnoreCase(nome).orElseThrow(() -> new NotFoundException("Não existe nenhum nome "+nome));

        return new ColaboradoresDTO(colab);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "colaboradores", key = "#cpf")
    public ColaboradoresDTO findByCpf(String cpf){
        Colaboradores colab = colaboradoresRepository.findByCpfContainingIgnoreCase(cpf).orElseThrow(() -> new NotFoundException("CPF "+cpf+" Invalido/não encontrado."));

        return new ColaboradoresDTO(colab);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "colaboradores", key = "#empresaId")
    public ColaboradoresDTO findByEmpresaId(Long empresaId){
        Colaboradores colab = colaboradoresRepository.findByEmpresaId_Id(empresaId).orElseThrow(() -> new NotFoundException("Empresa com id "+empresaId+" não encontrado."));

        return new ColaboradoresDTO(colab);
    }

    @Transactional(readOnly = true)
    public Page<ColaboradoresDTO> findByIsActive(Boolean isActive, Pageable pageable){
        Page<Colaboradores> colab = colaboradoresRepository.findByIsActive(isActive, pageable);

        if(colab.isEmpty()){
            throw new NotFoundException("Nenhum colaborador cadastrado.");
        }

        return colab.map(ColaboradoresDTO::new);
    }

    // Método para buscar colaboradores por data de criação
    @Transactional(readOnly = true)
    public Page<ColaboradoresDTO> getByDateCreated(String dateString, Pageable pageable){
        // 1. Converte a String para LocalDate (apenas data)
        LocalDate date = DateUtils.parseDate(dateString);

        // 2. Cria o início do dia (00:00:00) e o fim do dia (23:59:59)
        LocalDateTime startDay = date.atStartOfDay();
        LocalDateTime endDay = date.atTime(LocalTime.MAX);

        // 3. Chama o repositório com o intervalo
        Page<Colaboradores> colab = colaboradoresRepository.findByDateCreatedBetween(startDay, endDay, pageable);

        // 4. Verifica se algum colaborador foi encontrado
        if(colab.isEmpty()){
            throw new NotFoundException("Nenhum colaborador encontrado na data: " + dateString);
        }

        // 5. Retorna os colaboradores
        return colab.map(ColaboradoresDTO::new);
    }

    // Método para buscar colaboradores por data de atualização
    @Transactional(readOnly = true)
    public Page<ColaboradoresDTO> getByDateUpdated(String dateString, Pageable pageable){
        // 1. Converte a String para LocalDate (apenas data)
        LocalDate date = DateUtils.parseDate(dateString);

        // 2. Cria o início do dia (00:00:00) e o fim do dia (23:59:59)
        LocalDateTime startDay = date.atStartOfDay();
        LocalDateTime endDay = date.atTime(LocalTime.MAX);

        // 3. Chama o repositório com o intervalo
        Page<Colaboradores> colab = colaboradoresRepository.findByDateUpdatedBetween(startDay, endDay, pageable);

        // 4. Verifica se algum colaborador foi encontrado
        if(colab.isEmpty()){
            throw new NotFoundException("Nenhum colaborador encontrado na data: " + dateString);
        }
        
        // 5. Retorna os colaboradores
        return colab.map(ColaboradoresDTO::new);
    }

    @Transactional
    public Colaboradores save(SaveColaboradoresRequest request){
        Colaboradores colab = new Colaboradores();
        Empresa empresaId = empresaRepository.findById(request.getEmpresaId()).orElseThrow(() -> new NotFoundException("Id "+request.getEmpresaId()+ " não encontrado."));

        colab.setCpf(request.getCpf());
        colab.setEmpresaId(empresaId);
        colab.setIsActive(request.getIsActive());
        colab.setNomeColaborador(request.getNomeColaborador());
        colab.setSenha(passwordEncoder.encode(request.getSenha()));

        Colaboradores save = colaboradoresRepository.save(colab);
        log.info("Colaborador salvo com sucesso");

        return save;
    }


    //ATUALIZAR REGISTRO: Usamos @CacheEvict para apagar o cache antigo desse ID.
    // Na próxima vez que o findById for chamado, ele pegará o valor atualizado do banco.
    @Transactional
    @CacheEvict(value = "colaboradores", allEntries = true) 
    public Colaboradores update(Long id, UpdateColaboradoresRequest request){
        Colaboradores colab = colaboradoresRepository.findById(id).orElseThrow(() -> new NotFoundException("Colaborador com ID "+id+" Não foi encontrado."));
    
        if(request.getIsActive() != null){
            colab.setIsActive(request.getIsActive());
        }

        if(request.getCpf() != null){
            colab.setCpf(request.getCpf());
        }

        if(request.getEmpresaId() != null){
            Empresa empresaId = empresaRepository.findById(request.getEmpresaId()).orElseThrow(() -> new NotFoundException("ID " +request.getEmpresaId()+ " não encontrado."));
            colab.setEmpresaId(empresaId);
        }

        if(request.getNomeColaborador() != null){
            colab.setNomeColaborador(request.getNomeColaborador());
        }

        if (request.getSenha() != null) {
            colab.setSenha(passwordEncoder.encode(request.getSenha())); // <-- Criptografa aqui
        }

        Colaboradores update = colaboradoresRepository.save(colab);
        log.info("Colaborador {} foi alterado seu cadastro.", id);

        return update;
    }


    // DELETAR/DESATIVAR REGISTRO: Limpa o cache para que ninguém consulte um colaborador inativo desatualizado.
    @Transactional
    @CacheEvict(value = "colaboradores", allEntries = true) 
    public void delete(Long id){
        Colaboradores colab = colaboradoresRepository.findById(id).orElseThrow(() -> new NotFoundException("Colaborador não encontrado."));

        colab.setIsActive(false);
        colaboradoresRepository.save(colab);
    }
}