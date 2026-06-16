package betolara1.Ponto.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne
    @JoinColumn(name = "id_colaborador")
    private Colaboradores colaboradorId;

    @ManyToOne
    @JoinColumn(name = "id_colaborador_updater")
    private Colaboradores colaboradorIdUpdate;

    private LocalDateTime ponto;

    @PrePersist
    protected void onCreate() {
        ponto = LocalDateTime.now();
    }
}
