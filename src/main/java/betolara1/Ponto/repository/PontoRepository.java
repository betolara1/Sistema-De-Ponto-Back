package betolara1.Ponto.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import betolara1.Ponto.model.Ponto;

@Repository
public interface PontoRepository extends JpaRepository<Ponto, Long> {
    Optional<Ponto> findByColaboradorId_Id(Long id);

    Optional<Ponto> findByColaboradorIdUpdate_Id(Long id);

    // Busca tudo que foi criado entre o início do dia (00:00:00) e o fim (23:59:59)
    Page<Ponto> findByPontoBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
}
