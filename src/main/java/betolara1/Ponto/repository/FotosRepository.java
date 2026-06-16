package betolara1.Ponto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import betolara1.Ponto.model.Fotos;

@Repository
public interface FotosRepository extends JpaRepository<Fotos, Long> {
    Optional<Fotos> findByColaboradorId_Id(Long id);
}
