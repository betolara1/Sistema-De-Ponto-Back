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

import betolara1.Ponto.dto.FotosDTO;
import betolara1.Ponto.dto.request.SaveFotosRequest;
import betolara1.Ponto.dto.request.UpdateFotosRequest;
import betolara1.Ponto.model.Colaboradores;
import betolara1.Ponto.model.Fotos;
import betolara1.Ponto.repository.ColaboradoresRepository;
import betolara1.Ponto.repository.FotosRepository;
import jakarta.ws.rs.NotFoundException;

@ExtendWith(MockitoExtension.class)
public class FotosServiceTest {

    @Mock
    private FotosRepository fotosRepository;

    @Mock
    private ColaboradoresRepository colaboradoresRepository;

    @InjectMocks
    private FotosService fotosService;

    private Colaboradores colaborador;
    private Fotos foto;

    @BeforeEach
    void setUp() {
        colaborador = new Colaboradores();
        colaborador.setId(1L);
        colaborador.setNomeColaborador("Colaborador Teste");

        foto = new Fotos();
        foto.setId(1L);
        foto.setFoto(new byte[]{1, 2, 3});
        foto.setColaboradorId(colaborador);
        foto.setDateCreated(LocalDateTime.now());
        foto.setDateUpdated(LocalDateTime.now());
    }

    @Test
    void findAll_ShouldReturnPageOfFotosDTO() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Fotos> page = new PageImpl<>(Collections.singletonList(foto));

        when(fotosRepository.findAll(pageable)).thenReturn(page);

        Page<FotosDTO> result = fotosService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertArrayEquals(new byte[]{1, 2, 3}, result.getContent().get(0).foto());
        assertEquals(1L, result.getContent().get(0).colaboradorId());
        verify(fotosRepository, times(1)).findAll(pageable);
    }

    @Test
    void findAll_WhenEmpty_ShouldThrowNotFoundException() {
        Pageable pageable = PageRequest.of(0, 10);
        when(fotosRepository.findAll(pageable)).thenReturn(Page.empty());

        assertThrows(NotFoundException.class, () -> fotosService.findAll(pageable));
    }

    @Test
    void findById_WhenIdExists_ShouldReturnFotosDTO() {
        when(fotosRepository.findById(1L)).thenReturn(Optional.of(foto));

        FotosDTO result = fotosService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertArrayEquals(new byte[]{1, 2, 3}, result.foto());
        verify(fotosRepository, times(1)).findById(1L);
    }

    @Test
    void findById_WhenIdDoesNotExist_ShouldThrowNotFoundException() {
        when(fotosRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> fotosService.findById(2L));
        verify(fotosRepository, times(1)).findById(2L);
    }

    @Test
    void findByColaboradorId_WhenExists_ShouldReturnFotosDTO() {
        when(fotosRepository.findByColaboradorId_Id(1L)).thenReturn(Optional.of(foto));

        FotosDTO result = fotosService.findByColaboradorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals(1L, result.colaboradorId());
        verify(fotosRepository, times(1)).findByColaboradorId_Id(1L);
    }

    @Test
    void findByColaboradorId_WhenDoesNotExist_ShouldThrowNotFoundException() {
        when(fotosRepository.findByColaboradorId_Id(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> fotosService.findByColaboradorId(2L));
        verify(fotosRepository, times(1)).findByColaboradorId_Id(2L);
    }

    @Test
    void save_ShouldSaveAndReturnFoto() {
        SaveFotosRequest request = new SaveFotosRequest();
        request.setColaboradorId(1L);
        request.setFoto(new byte[]{4, 5, 6});

        when(colaboradoresRepository.findById(1L)).thenReturn(Optional.of(colaborador));
        when(fotosRepository.save(any(Fotos.class))).thenAnswer(invocation -> {
            Fotos input = invocation.getArgument(0);
            input.setId(10L);
            return input;
        });

        Fotos result = fotosService.save(request);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertArrayEquals(new byte[]{4, 5, 6}, result.getFoto());
        assertEquals(colaborador, result.getColaboradorId());
        verify(colaboradoresRepository, times(1)).findById(1L);
        verify(fotosRepository, times(1)).save(any(Fotos.class));
    }

    @Test
    void save_WhenColaboradorDoesNotExist_ShouldThrowNotFoundException() {
        SaveFotosRequest request = new SaveFotosRequest();
        request.setColaboradorId(2L);
        request.setFoto(new byte[]{4, 5, 6});

        when(colaboradoresRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> fotosService.save(request));
        verify(colaboradoresRepository, times(1)).findById(2L);
        verify(fotosRepository, never()).save(any(Fotos.class));
    }

    @Test
    void update_WhenColaboradorExists_ShouldUpdateAndReturnFoto() {
        UpdateFotosRequest request = new UpdateFotosRequest();
        request.setColaboradorId(1L);
        request.setFoto(new byte[]{7, 8, 9});

        when(fotosRepository.findById(1L)).thenReturn(Optional.of(foto));
        when(colaboradoresRepository.findById(1L)).thenReturn(Optional.of(colaborador));
        when(fotosRepository.save(any(Fotos.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Fotos result = fotosService.update(request, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertArrayEquals(new byte[]{7, 8, 9}, result.getFoto());
        assertEquals(colaborador, result.getColaboradorId());
        verify(fotosRepository, times(1)).findById(1L);
        verify(colaboradoresRepository, times(1)).findById(1L);
        verify(fotosRepository, times(1)).save(any(Fotos.class));
    }

    @Test
    void update_WhenColaboradorDoesNotExist_ShouldThrowNotFoundException() {
        UpdateFotosRequest request = new UpdateFotosRequest();
        request.setColaboradorId(2L);
        request.setFoto(new byte[]{7, 8, 9});

        when(fotosRepository.findById(1L)).thenReturn(Optional.of(foto));
        when(colaboradoresRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> fotosService.update(request, 1L));
        verify(fotosRepository, times(1)).findById(1L);
        verify(colaboradoresRepository, times(1)).findById(2L);
        verify(fotosRepository, never()).save(any(Fotos.class));
    }

    @Test
    void update_WhenColaboradorIdIsNull_ShouldUpdateAndReturnFotoWithoutChangingColaborador() {
        UpdateFotosRequest request = new UpdateFotosRequest();
        request.setFoto(new byte[]{7, 8, 9});

        when(fotosRepository.findById(1L)).thenReturn(Optional.of(foto));
        when(fotosRepository.save(any(Fotos.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Fotos result = fotosService.update(request, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertArrayEquals(new byte[]{7, 8, 9}, result.getFoto());
        assertEquals(colaborador, result.getColaboradorId());
        verify(colaboradoresRepository, never()).findById(anyLong());
        verify(fotosRepository, times(1)).findById(1L);
        verify(fotosRepository, times(1)).save(any(Fotos.class));
    }
}
