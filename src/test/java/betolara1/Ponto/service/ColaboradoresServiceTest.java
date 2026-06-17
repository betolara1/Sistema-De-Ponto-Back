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

import betolara1.Ponto.dto.ColaboradoresDTO;
import betolara1.Ponto.dto.request.SaveColaboradoresRequest;
import betolara1.Ponto.dto.request.UpdateColaboradoresRequest;
import betolara1.Ponto.model.Colaboradores;
import betolara1.Ponto.model.Empresa;
import betolara1.Ponto.repository.ColaboradoresRepository;
import betolara1.Ponto.repository.EmpresaRepository;
import jakarta.ws.rs.NotFoundException;

@ExtendWith(MockitoExtension.class)
public class ColaboradoresServiceTest {

    @Mock
    private ColaboradoresRepository colaboradoresRepository;

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @InjectMocks
    private ColaboradoresService colaboradoresService;

    private Colaboradores colab;
    private Empresa empresa;

    @BeforeEach
    void setUp() {
        empresa = new Empresa();
        empresa.setId(1L);
        empresa.setNomeEmpresa("Empresa Teste");
        empresa.setCnpj("12345678000199");
        empresa.setIsActive(true);

        colab = new Colaboradores();
        colab.setId(1L);
        colab.setNomeColaborador("Colaborador Teste");
        colab.setCpf("12345678909");
        colab.setSenha("senha123");
        colab.setEmpresaId(empresa);
        colab.setIsActive(true);
        colab.setDateCreated(LocalDateTime.now());
        colab.setDateUpdated(LocalDateTime.now());
    }

    @Test
    void findAll_ShouldReturnPageOfColaboradoresDTO() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Colaboradores> page = new PageImpl<>(Collections.singletonList(colab));
        
        when(colaboradoresRepository.findAll(pageable)).thenReturn(page);

        Page<ColaboradoresDTO> result = colaboradoresService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Colaborador Teste", result.getContent().get(0).nomeColaborador());
        verify(colaboradoresRepository, times(1)).findAll(pageable);
    }

    @Test
    void findAll_WhenEmpty_ShouldThrowNotFoundException() {
        Pageable pageable = PageRequest.of(0, 10);
        when(colaboradoresRepository.findAll(pageable)).thenReturn(Page.empty());

        assertThrows(NotFoundException.class, () -> colaboradoresService.findAll(pageable));
    }

    @Test
    void findById_WhenIdExists_ShouldReturnColaboradoresDTO() {
        when(colaboradoresRepository.findById(1L)).thenReturn(Optional.of(colab));

        ColaboradoresDTO result = colaboradoresService.findById(1L);

        assertNotNull(result);
        assertEquals("Colaborador Teste", result.nomeColaborador());
        verify(colaboradoresRepository, times(1)).findById(1L);
    }

    @Test
    void findById_WhenIdDoesNotExist_ShouldThrowNotFoundException() {
        when(colaboradoresRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> colaboradoresService.findById(2L));
        verify(colaboradoresRepository, times(1)).findById(2L);
    }

    @Test
    void findByNome_WhenFound_ShouldReturnColaboradoresDTO() {
        when(colaboradoresRepository.findByNomeColaboradorContainingIgnoreCase("Teste")).thenReturn(Optional.of(colab));

        ColaboradoresDTO result = colaboradoresService.findByNome("Teste");

        assertNotNull(result);
        assertEquals("Colaborador Teste", result.nomeColaborador());
        verify(colaboradoresRepository, times(1)).findByNomeColaboradorContainingIgnoreCase("Teste");
    }

    @Test
    void findByNome_WhenNotFound_ShouldThrowNotFoundException() {
        when(colaboradoresRepository.findByNomeColaboradorContainingIgnoreCase("NaoExiste")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> colaboradoresService.findByNome("NaoExiste"));
    }

    @Test
    void findByCpf_WhenFound_ShouldReturnColaboradoresDTO() {
        when(colaboradoresRepository.findByCpfContainingIgnoreCase("12345678909")).thenReturn(Optional.of(colab));

        ColaboradoresDTO result = colaboradoresService.findByCpf("12345678909");

        assertNotNull(result);
        assertEquals("Colaborador Teste", result.nomeColaborador());
    }

    @Test
    void findByCpf_WhenNotFound_ShouldThrowNotFoundException() {
        when(colaboradoresRepository.findByCpfContainingIgnoreCase("00000000000")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> colaboradoresService.findByCpf("00000000000"));
    }

    @Test
    void findByEmpresaId_WhenFound_ShouldReturnColaboradoresDTO() {
        when(colaboradoresRepository.findByEmpresaId_Id(1L)).thenReturn(Optional.of(colab));

        ColaboradoresDTO result = colaboradoresService.findByEmpresaId(1L);

        assertNotNull(result);
        assertEquals("Colaborador Teste", result.nomeColaborador());
    }

    @Test
    void findByEmpresaId_WhenNotFound_ShouldThrowNotFoundException() {
        when(colaboradoresRepository.findByEmpresaId_Id(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> colaboradoresService.findByEmpresaId(2L));
    }

    @Test
    void findByIsActive_ShouldReturnPageOfColaboradoresDTO() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Colaboradores> page = new PageImpl<>(Collections.singletonList(colab));

        when(colaboradoresRepository.findByIsActive(true, pageable)).thenReturn(page);

        Page<ColaboradoresDTO> result = colaboradoresService.findByIsActive(true, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertTrue(result.getContent().get(0).isActive());
    }

    @Test
    void findByIsActive_WhenEmpty_ShouldThrowNotFoundException() {
        Pageable pageable = PageRequest.of(0, 10);
        when(colaboradoresRepository.findByIsActive(true, pageable)).thenReturn(Page.empty());

        assertThrows(NotFoundException.class, () -> colaboradoresService.findByIsActive(true, pageable));
    }

    @Test
    void getByDateCreated_WhenFound_ShouldReturnPageOfColaboradoresDTO() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Colaboradores> page = new PageImpl<>(Collections.singletonList(colab));

        when(colaboradoresRepository.findByDateCreatedBetween(any(), any(), eq(pageable))).thenReturn(page);

        Page<ColaboradoresDTO> result = colaboradoresService.getByDateCreated("2026-06-12", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getByDateCreated_WhenNotFound_ShouldThrowNotFoundException() {
        Pageable pageable = PageRequest.of(0, 10);
        when(colaboradoresRepository.findByDateCreatedBetween(any(), any(), eq(pageable))).thenReturn(Page.empty());

        assertThrows(NotFoundException.class, () -> colaboradoresService.getByDateCreated("2026-06-12", pageable));
    }

    @Test
    void getByDateUpdated_WhenFound_ShouldReturnPageOfColaboradoresDTO() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Colaboradores> page = new PageImpl<>(Collections.singletonList(colab));

        when(colaboradoresRepository.findByDateUpdatedBetween(any(), any(), eq(pageable))).thenReturn(page);

        Page<ColaboradoresDTO> result = colaboradoresService.getByDateUpdated("2026-06-12", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getByDateUpdated_WhenNotFound_ShouldThrowNotFoundException() {
        Pageable pageable = PageRequest.of(0, 10);
        when(colaboradoresRepository.findByDateUpdatedBetween(any(), any(), eq(pageable))).thenReturn(Page.empty());

        assertThrows(NotFoundException.class, () -> colaboradoresService.getByDateUpdated("2026-06-12", pageable));
    }

    @Test
    void save_ShouldSaveAndReturnColaborador() {
        SaveColaboradoresRequest request = new SaveColaboradoresRequest();
        request.setNomeColaborador("Novo Colaborador");
        request.setCpf("98765432100");
        request.setSenha("novaSenha");
        request.setEmpresaId(1L);
        request.setIsActive(true);

        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(colaboradoresRepository.save(any(Colaboradores.class))).thenReturn(colab);

        Colaboradores result = colaboradoresService.save(request);

        assertNotNull(result);
        verify(empresaRepository, times(1)).findById(1L);
        verify(colaboradoresRepository, times(1)).save(any(Colaboradores.class));
    }

    @Test
    void update_ShouldUpdateAndReturnColaborador() {
        UpdateColaboradoresRequest request = new UpdateColaboradoresRequest();
        request.setNomeColaborador("Nome Atualizado");

        when(colaboradoresRepository.findById(1L)).thenReturn(Optional.of(colab));
        when(colaboradoresRepository.save(any(Colaboradores.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Colaboradores result = colaboradoresService.update(1L, request);

        assertNotNull(result);
        assertEquals("Nome Atualizado", result.getNomeColaborador());
        verify(colaboradoresRepository, times(1)).save(any(Colaboradores.class));
    }

    @Test
    void delete_ShouldDisableColaborador() {
        when(colaboradoresRepository.findById(1L)).thenReturn(Optional.of(colab));
        when(colaboradoresRepository.save(any(Colaboradores.class))).thenAnswer(invocation -> invocation.getArgument(0));

        colaboradoresService.delete(1L);

        assertFalse(colab.getIsActive());
        verify(colaboradoresRepository, times(1)).findById(1L);
        verify(colaboradoresRepository, times(1)).save(any(Colaboradores.class));
    }

    @Test
    void delete_WhenNotFound_ShouldThrowNotFoundException() {
        when(colaboradoresRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> colaboradoresService.delete(99L));
        verify(colaboradoresRepository, never()).save(any(Colaboradores.class));
    }
}
