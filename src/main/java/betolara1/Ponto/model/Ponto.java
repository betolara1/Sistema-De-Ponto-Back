package betolara1.Ponto.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "ponto")
public class Ponto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Colaboradores colaboradorId;
    private Colaboradores colaboradorIdUpdate;

    private LocalDateTime pontoCreate;
    private LocalDateTime pontoUpdated;

    @PrePersist
    protected void onCreate() {
        pontoCreate = LocalDateTime.now();
    }
}
