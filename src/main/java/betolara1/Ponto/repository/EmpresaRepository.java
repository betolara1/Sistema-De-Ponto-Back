package betolara1.Ponto.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import betolara1.Ponto.model.Empresa;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long>{
    // Busca tudo que foi criado entre o início do dia (00:00:00) e o fim (23:59:59)
    Page<Empresa> findByDateCreatedBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
    
    // Busca tudo que foi atualizado entre o início do dia (00:00:00) e o fim (23:59:59)
    Page<Empresa> findByDateUpdatedBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    // busca pelo nome da empresa, se contém e não importa letra maiuscula ou miniscula
    Empresa findByNomeEmpresaContainingIgnoreCase(String nomeEmpresa);

    // busca a empresa pelo cnpj
    Empresa findByCnpjContaining(String cnpj);

    // busca empresas ativas
    Page<Empresa> findByIsActive(Boolean isActive, Pageable pageable);
}
