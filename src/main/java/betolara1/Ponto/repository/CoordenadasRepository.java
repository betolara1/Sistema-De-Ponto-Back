package betolara1.Ponto.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import betolara1.Ponto.model.Coordenadas;

@Repository
public interface CoordenadasRepository extends JpaRepository<Coordenadas, Long> {
    // Busca tudo que foi criado entre o início do dia (00:00:00) e o fim (23:59:59)
    Page<Coordenadas> findByDateCreatedBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
    
    // Busca tudo que foi atualizado entre o início do dia (00:00:00) e o fim (23:59:59)
    Page<Coordenadas> findByDateUpdatedBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    Optional<Coordenadas> findByEmpresaIdId(Long id);
}
