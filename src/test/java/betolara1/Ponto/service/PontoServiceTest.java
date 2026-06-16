package betolara1.Ponto.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import betolara1.Ponto.dto.PontoDTO;
import betolara1.Ponto.dto.request.SavePontoRequest;
import betolara1.Ponto.dto.request.UpdatePontoRequest;
import betolara1.Ponto.model.Colaboradores;
import betolara1.Ponto.model.Ponto;
import betolara1.Ponto.repository.ColaboradoresRepository;
import betolara1.Ponto.repository.PontoRepository;
import jakarta.ws.rs.NotFoundException;

@ExtendWith(MockitoExtension.class)
public class PontoServiceTest {

    @Mock
    private PontoRepository pontoRepository;

    @Mock
    private ColaboradoresRepository colaboradoresRepository;

    @InjectMocks
    private PontoService pontoService;

    private Colaboradores colab;
    private Colaboradores colabUpdate;
    private Ponto ponto;

    @BeforeEach
    void setUp() {
        colab = new Colaboradores();
        colab.setId(1L);
        colab.setNomeColaborador("Colaborador Teste");

        colabUpdate = new Colaboradores();
        colabUpdate.setId(2L);
        colabUpdate.setNomeColaborador("Responsavel Teste");

        ponto = new Ponto();
        ponto.setId(10L);
        ponto.setColaboradorId(colab);
        ponto.setColaboradorIdUpdate(colabUpdate);
        ponto.setPontoCreate(LocalDateTime.now());
        ponto.setPontoUpdated(LocalDateTime.now());
    }

    @Test
    void findAll_ShouldReturnPageOfPontoDTO() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Ponto> page = new PageImpl<>(Collections.singletonList(ponto));

        when(pontoRepository.findAll(pageable)).thenReturn(page);

        Page<PontoDTO> result = pontoService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        // In PontoDTO, the fields map as:
        // first field = ponto.getColaboradorId().getId() -> 1L
        // second field = ponto.getColaboradorIdUpdate().getId() -> 2L
        // third field = ponto.getId() -> 10L
        assertEquals(1L, result.getContent().get(0).id());
        assertEquals(2L, result.getContent().get(0).colaboradorId());
        assertEquals(10L, result.getContent().get(0).colaboradorIdUpdate());
        verify(pontoRepository, times(1)).findAll(pageable);
    }

    @Test
    void findById_WhenIdExists_ShouldReturnPontoDTO() {
        when(pontoRepository.findById(10L)).thenReturn(Optional.of(ponto));

        PontoDTO result = pontoService.findById(10L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals(2L, result.colaboradorId());
        assertEquals(10L, result.colaboradorIdUpdate());
        verify(pontoRepository, times(1)).findById(10L);
    }

    @Test
    void findById_WhenIdDoesNotExist_ShouldThrowNotFoundException() {
        when(pontoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> pontoService.findById(99L));
        verify(pontoRepository, times(1)).findById(99L);
    }

    @Test
    void findByColaboradorId_WhenFound_ShouldReturnPontoDTO() {
        when(pontoRepository.findByColaboradorId(1L)).thenReturn(Optional.of(ponto));

        PontoDTO result = pontoService.findByColaboradorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(pontoRepository, times(1)).findByColaboradorId(1L);
    }

    @Test
    void findByColaboradorId_WhenNotFound_ShouldThrowNotFoundException() {
        when(pontoRepository.findByColaboradorId(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> pontoService.findByColaboradorId(99L));
    }

    @Test
    void findByColaboradorIdUpdater_WhenFound_ShouldReturnPontoDTO() {
        when(pontoRepository.findByColaboradorUpdate(2L)).thenReturn(Optional.of(ponto));

        PontoDTO result = pontoService.findByColaboradorIdUpdater(2L);

        assertNotNull(result);
        assertEquals(2L, result.colaboradorId());
        verify(pontoRepository, times(1)).findByColaboradorUpdate(2L);
    }

    @Test
    void findByColaboradorIdUpdater_WhenNotFound_ShouldThrowNotFoundException() {
        when(pontoRepository.findByColaboradorUpdate(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> pontoService.findByColaboradorIdUpdater(99L));
    }

    @Test
    void getByPontoCreated_WhenFound_ShouldReturnPageOfPontoDTO() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Ponto> page = new PageImpl<>(Collections.singletonList(ponto));

        when(pontoRepository.findByDateCreatedBetween(any(), any(), eq(pageable))).thenReturn(page);

        Page<PontoDTO> result = pontoService.getByPontoCreated("2026-06-16", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(pontoRepository, times(1)).findByDateCreatedBetween(any(), any(), eq(pageable));
    }

    @Test
    void getByPontoCreated_WhenEmpty_ShouldThrowNotFoundException() {
        Pageable pageable = PageRequest.of(0, 10);
        when(pontoRepository.findByDateCreatedBetween(any(), any(), eq(pageable))).thenReturn(Page.empty());

        assertThrows(NotFoundException.class, () -> pontoService.getByPontoCreated("2026-06-16", pageable));
    }

    @Test
    void getByPontoUpdated_WhenFound_ShouldReturnPageOfPontoDTO() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Ponto> page = new PageImpl<>(Collections.singletonList(ponto));

        when(pontoRepository.findByDateUpdatedBetween(any(), any(), eq(pageable))).thenReturn(page);

        Page<PontoDTO> result = pontoService.getByPontoUpdated("2026-06-16", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(pontoRepository, times(1)).findByDateUpdatedBetween(any(), any(), eq(pageable));
    }

    @Test
    void getByPontoUpdated_WhenEmpty_ShouldThrowNotFoundException() {
        Pageable pageable = PageRequest.of(0, 10);
        when(pontoRepository.findByDateUpdatedBetween(any(), any(), eq(pageable))).thenReturn(Page.empty());

        assertThrows(NotFoundException.class, () -> pontoService.getByPontoUpdated("2026-06-16", pageable));
    }

    @Test
    void save_ShouldSaveAndReturnPonto() {
        SavePontoRequest request = new SavePontoRequest();
        request.setColaboradorId(1L);

        when(colaboradoresRepository.findById(1L)).thenReturn(Optional.of(colab));
        when(pontoRepository.save(any(Ponto.class))).thenAnswer(invocation -> {
            Ponto saved = invocation.getArgument(0);
            saved.setId(10L);
            return saved;
        });

        Ponto result = pontoService.save(request);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals(colab, result.getColaboradorId());
        verify(colaboradoresRepository, times(1)).findById(1L);
        verify(pontoRepository, times(1)).save(any(Ponto.class));
    }

    @Test
    void save_WhenColaboradorNotFound_ShouldThrowNotFoundException() {
        SavePontoRequest request = new SavePontoRequest();
        request.setColaboradorId(99L);

        when(colaboradoresRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> pontoService.save(request));
        verify(pontoRepository, never()).save(any(Ponto.class));
    }

    @Test
    void update_ShouldUpdateAndReturnPonto() {
        UpdatePontoRequest request = new UpdatePontoRequest();
        request.setColaboradorIdUpdate(2L);
        LocalDateTime updateTime = LocalDateTime.now().plusHours(1);
        request.setPontoUpdated(updateTime);

        // Under the hood, PontoService.update finds the collaborator by the ID parameter passed to the method (not request.colaboradorIdUpdate!)
        when(colaboradoresRepository.findById(1L)).thenReturn(Optional.of(colab));
        when(pontoRepository.save(any(Ponto.class))).thenAnswer(invocation -> {
            Ponto saved = invocation.getArgument(0);
            saved.setId(10L);
            return saved;
        });

        Ponto result = pontoService.update(request, 1L);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        // Since request.getColaboradorIdUpdate() is not null, setColaboradorIdUpdate is called with the found collaborator (colab)
        assertEquals(colab, result.getColaboradorIdUpdate());
        assertEquals(updateTime, result.getPontoUpdated());
        verify(colaboradoresRepository, times(1)).findById(1L);
        verify(pontoRepository, times(1)).save(any(Ponto.class));
    }

    @Test
    void update_WhenColaboradorNotFound_ShouldThrowNotFoundException() {
        UpdatePontoRequest request = new UpdatePontoRequest();
        when(colaboradoresRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> pontoService.update(request, 99L));
        verify(pontoRepository, never()).save(any(Ponto.class));
    }
}
