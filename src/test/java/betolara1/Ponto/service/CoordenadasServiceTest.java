package betolara1.Ponto.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
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

import betolara1.Ponto.dto.CoordenadasDTO;
import betolara1.Ponto.dto.request.SaveCoordenadasRequest;
import betolara1.Ponto.dto.request.UpdateCoordenadasRequest;
import betolara1.Ponto.model.Coordenadas;
import betolara1.Ponto.model.Empresa;
import betolara1.Ponto.repository.CoordenadasRepository;
import betolara1.Ponto.repository.EmpresaRepository;
import jakarta.ws.rs.NotFoundException;

@ExtendWith(MockitoExtension.class)
public class CoordenadasServiceTest {

    @Mock
    private CoordenadasRepository coordenadasRepository;

    @Mock
    private EmpresaRepository empresaRepository;

    @InjectMocks
    private CoordenadasService coordenadasService;

    private Empresa empresa;
    private Coordenadas coordenadas;

    @BeforeEach
    void setUp() {
        empresa = new Empresa();
        empresa.setId(1L);
        empresa.setNomeEmpresa("Empresa Teste");

        coordenadas = new Coordenadas();
        coordenadas.setId(10L);
        coordenadas.setLatitude(new BigDecimal("-23.55052"));
        coordenadas.setLongitude(new BigDecimal("-46.633308"));
        coordenadas.setEmpresaId(empresa);
        coordenadas.setDateCreated(LocalDateTime.now());
        coordenadas.setDateUpdated(LocalDateTime.now());
    }

    @Test
    void findAll_ShouldReturnPageOfCoordenadasDTO() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Coordenadas> page = new PageImpl<>(Collections.singletonList(coordenadas));

        when(coordenadasRepository.findAll(pageable)).thenReturn(page);

        Page<CoordenadasDTO> result = coordenadasService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(new BigDecimal("-23.55052"), result.getContent().get(0).latitude());
        assertEquals(1L, result.getContent().get(0).empresaId());
        verify(coordenadasRepository, times(1)).findAll(pageable);
    }

    @Test
    void findAll_WhenEmpty_ShouldThrowNotFoundException() {
        Pageable pageable = PageRequest.of(0, 10);
        when(coordenadasRepository.findAll(pageable)).thenReturn(Page.empty());

        assertThrows(NotFoundException.class, () -> coordenadasService.findAll(pageable));
    }

    @Test
    void findByEmpresaId_WhenFound_ShouldReturnCoordenadasDTO() {
        when(coordenadasRepository.findByEmpresaIdId(1L)).thenReturn(Optional.of(coordenadas));

        CoordenadasDTO result = coordenadasService.findByEmpresaId(1L);

        assertNotNull(result);
        assertEquals(new BigDecimal("-23.55052"), result.latitude());
        assertEquals(1L, result.empresaId());
        verify(coordenadasRepository, times(1)).findByEmpresaIdId(1L);
    }

    @Test
    void findByEmpresaId_WhenNotFound_ShouldThrowNotFoundException() {
        when(coordenadasRepository.findByEmpresaIdId(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> coordenadasService.findByEmpresaId(2L));
    }

    @Test
    void save_ShouldSaveAndReturnCoordenadas() {
        SaveCoordenadasRequest request = new SaveCoordenadasRequest();
        request.setLatitude("-22.9068");
        request.setLongitude("-43.1729");
        request.setEmpresaId(1L);

        Coordenadas savedCoordenadas = new Coordenadas();
        savedCoordenadas.setId(11L);
        savedCoordenadas.setLatitude(new BigDecimal(request.getLatitude()));
        savedCoordenadas.setLongitude(new BigDecimal(request.getLongitude()));
        savedCoordenadas.setEmpresaId(empresa);

        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));
        when(coordenadasRepository.save(any(Coordenadas.class))).thenReturn(savedCoordenadas);

        Coordenadas result = coordenadasService.save(request);

        assertNotNull(result);
        assertEquals(11L, result.getId());
        assertEquals(new BigDecimal("-22.9068"), result.getLatitude());
        assertEquals(empresa, result.getEmpresaId());
        verify(empresaRepository, times(1)).findById(1L);
        verify(coordenadasRepository, times(1)).save(any(Coordenadas.class));
    }

    @Test
    void save_WhenEmpresaNotFound_ShouldThrowNotFoundException() {
        SaveCoordenadasRequest request = new SaveCoordenadasRequest();
        request.setEmpresaId(2L);

        when(empresaRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> coordenadasService.save(request));
        verify(coordenadasRepository, never()).save(any(Coordenadas.class));
    }

    @Test
    void update_ShouldUpdateAndReturnCoordenadas() {
        UpdateCoordenadasRequest request = new UpdateCoordenadasRequest();
        request.setLatitude(new BigDecimal("-25.4284"));
        request.setLongitude(new BigDecimal("-49.2733"));
        request.setEmpresaId(1L);

        Coordenadas updatedCoordenadas = new Coordenadas();
        updatedCoordenadas.setId(10L);
        updatedCoordenadas.setLatitude(request.getLatitude());
        updatedCoordenadas.setLongitude(request.getLongitude());
        updatedCoordenadas.setEmpresaId(empresa);

        when(coordenadasRepository.findById(10L)).thenReturn(Optional.of(coordenadas));
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));
        when(coordenadasRepository.save(any(Coordenadas.class))).thenReturn(updatedCoordenadas);

        Coordenadas result = coordenadasService.update(request, 10L);

        assertNotNull(result);
        assertEquals(new BigDecimal("-25.4284"), result.getLatitude());
        assertEquals(empresa, result.getEmpresaId());
        verify(coordenadasRepository, times(1)).findById(10L);
        verify(empresaRepository, times(1)).findById(1L);
        verify(coordenadasRepository, times(1)).save(any(Coordenadas.class));
    }

    @Test
    void update_WhenCoordenadasNotFound_ShouldThrowNotFoundException() {
        UpdateCoordenadasRequest request = new UpdateCoordenadasRequest();
        when(coordenadasRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> coordenadasService.update(request, 100L));
    }

    @Test
    void update_WhenNewEmpresaNotFound_ShouldThrowNotFoundException() {
        UpdateCoordenadasRequest request = new UpdateCoordenadasRequest();
        request.setEmpresaId(99L);

        when(coordenadasRepository.findById(10L)).thenReturn(Optional.of(coordenadas));
        when(empresaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> coordenadasService.update(request, 10L));
    }
}
