package betolara1.Ponto.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import betolara1.Ponto.model.Colaboradores;

@Repository
public interface ColaboradoresRepository extends JpaRepository<Colaboradores, Long>{
    // Busca tudo que foi criado entre o início do dia (00:00:00) e o fim (23:59:59)
    Page<Colaboradores> findByDateCreatedBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
    
    // Busca tudo que foi atualizado entre o início do dia (00:00:00) e o fim (23:59:59)
    Page<Colaboradores> findByDateUpdatedBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    Optional<Colaboradores> findByNomeColaboradorContainingIgnoreCase(String nome);

    Optional<Colaboradores> findByCpfContainingIgnoreCase(String cpf);

    Optional<Colaboradores> findByEmpresaId_Id(Long empresaId);

    Page<Colaboradores> findByIsActive(Boolean isActive, Pageable pageable);
}
