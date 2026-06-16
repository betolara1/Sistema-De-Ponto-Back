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

import betolara1.Ponto.dto.EmpresaDTO;
import betolara1.Ponto.dto.request.SaveEmpresaRequest;
import betolara1.Ponto.dto.request.UpdateEmpresaRequest;
import betolara1.Ponto.model.Empresa;
import betolara1.Ponto.repository.EmpresaRepository;
import jakarta.ws.rs.NotFoundException;

@ExtendWith(MockitoExtension.class)
public class EmpresaServiceTest {

    @Mock
    private EmpresaRepository empresaRepository;

    @InjectMocks
    private EmpresaService empresaService;

    private Empresa empresa;

    @BeforeEach
    void setUp() {
        empresa = new Empresa();
        empresa.setId(1L);
        empresa.setNomeEmpresa("Empresa Teste");
        empresa.setCnpj("12345678000199");
        empresa.setEndereco("Rua Teste, 123");
        empresa.setCep("12345-678");
        empresa.setBairro("Bairro Teste");
        empresa.setCidade("Cidade Teste");
        empresa.setIsActive(true);
        empresa.setDateCreated(LocalDateTime.now());
        empresa.setDateUpdated(LocalDateTime.now());
    }

    @Test
    void findAll_ShouldReturnPageOfEmpresaDTO() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Empresa> page = new PageImpl<>(Collections.singletonList(empresa));
        
        when(empresaRepository.findAll(pageable)).thenReturn(page);

        Page<EmpresaDTO> result = empresaService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Empresa Teste", result.getContent().get(0).nomeEmpresa());
        verify(empresaRepository, times(1)).findAll(pageable);
    }

    @Test
    void findById_WhenIdExists_ShouldReturnEmpresaDTO() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));

        EmpresaDTO result = empresaService.findById(1L);

        assertNotNull(result);
        assertEquals("Empresa Teste", result.nomeEmpresa());
        verify(empresaRepository, times(1)).findById(1L);
    }

    @Test
    void findById_WhenIdDoesNotExist_ShouldThrowNotFoundException() {
        when(empresaRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> empresaService.findById(2L));
        verify(empresaRepository, times(1)).findById(2L);
    }

    @Test
    void findByIsActive_ShouldReturnPageOfEmpresaDTO() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Empresa> page = new PageImpl<>(Collections.singletonList(empresa));
        
        when(empresaRepository.findByIsActive(true, pageable)).thenReturn(page);

        Page<EmpresaDTO> result = empresaService.findByIsActive(true, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertTrue(result.getContent().get(0).isActive());
        verify(empresaRepository, times(1)).findByIsActive(true, pageable);
    }

    @Test
    void getByDateCreated_WhenFound_ShouldReturnPageOfEmpresaDTO() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Empresa> page = new PageImpl<>(Collections.singletonList(empresa));
        
        when(empresaRepository.findByDateCreatedBetween(any(), any(), eq(pageable))).thenReturn(page);

        Page<EmpresaDTO> result = empresaService.getByDateCreated("2026-06-11", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(empresaRepository, times(1)).findByDateCreatedBetween(any(), any(), eq(pageable));
    }

    @Test
    void getByDateCreated_WhenNotFound_ShouldThrowNotFoundException() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Empresa> emptyPage = Page.empty();
        
        when(empresaRepository.findByDateCreatedBetween(any(), any(), eq(pageable))).thenReturn(emptyPage);

        assertThrows(NotFoundException.class, () -> empresaService.getByDateCreated("2026-06-11", pageable));
    }

    @Test
    void getByDateCreated_WhenInvalidDate_ShouldThrowIllegalArgumentException() {
        Pageable pageable = PageRequest.of(0, 10);
        assertThrows(IllegalArgumentException.class, () -> empresaService.getByDateCreated("data-invalida", pageable));
    }

    @Test
    void getByDateUpdated_WhenFound_ShouldReturnPageOfEmpresaDTO() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Empresa> page = new PageImpl<>(Collections.singletonList(empresa));
        
        when(empresaRepository.findByDateUpdatedBetween(any(), any(), eq(pageable))).thenReturn(page);

        Page<EmpresaDTO> result = empresaService.getByDateUpdated("2026-06-11", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(empresaRepository, times(1)).findByDateUpdatedBetween(any(), any(), eq(pageable));
    }

    @Test
    void getByDateUpdated_WhenNotFound_ShouldThrowNotFoundException() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Empresa> emptyPage = Page.empty();
        
        when(empresaRepository.findByDateUpdatedBetween(any(), any(), eq(pageable))).thenReturn(emptyPage);

        assertThrows(NotFoundException.class, () -> empresaService.getByDateUpdated("2026-06-11", pageable));
    }

    @Test
    void findByName_WhenNameIsNotEmpty_ShouldReturnEmpresaDTO() {
        when(empresaRepository.findByNomeEmpresaContainingIgnoreCase("Teste")).thenReturn(empresa);

        EmpresaDTO result = empresaService.findByName("Teste");

        assertNotNull(result);
        assertEquals("Empresa Teste", result.nomeEmpresa());
        verify(empresaRepository, times(1)).findByNomeEmpresaContainingIgnoreCase("Teste");
    }

    @Test
    void findByName_WhenNameIsEmpty_ShouldThrowNotFoundException() {
        when(empresaRepository.findByNomeEmpresaContainingIgnoreCase("Inexistente")).thenReturn(null);

        assertThrows(NotFoundException.class, () -> empresaService.findByName("Inexistente"));
    }

    @Test
    void findByCNPJ_WhenCnpjIsNotEmpty_ShouldReturnEmpresaDTO() {
        when(empresaRepository.findByCnpjContaining("123456")).thenReturn(empresa);

        EmpresaDTO result = empresaService.findByCNPJ("123456");

        assertNotNull(result);
        assertEquals("Empresa Teste", result.nomeEmpresa());
        verify(empresaRepository, times(1)).findByCnpjContaining("123456");
    }

    @Test
    void findByCNPJ_WhenCnpjIsEmpty_ShouldThrowNotFoundException() {
        when(empresaRepository.findByCnpjContaining("000000")).thenReturn(null);

        assertThrows(NotFoundException.class, () -> empresaService.findByCNPJ("000000"));
    }

    @Test
    void save_ShouldSaveAndReturnEmpresa() {
        SaveEmpresaRequest request = new SaveEmpresaRequest();
        request.setNomeEmpresa("Nova Empresa");
        request.setCnpj("98765432100019");
        request.setEndereco("Rua Nova, 456");
        request.setCep("98765-432");
        request.setBairro("Novo Bairro");
        request.setCidade("Nova Cidade");

        Empresa savedEmpresa = new Empresa();
        savedEmpresa.setId(2L);
        savedEmpresa.setNomeEmpresa(request.getNomeEmpresa());
        savedEmpresa.setCnpj(request.getCnpj());
        savedEmpresa.setEndereco(request.getEndereco());
        savedEmpresa.setCep(request.getCep());
        savedEmpresa.setBairro(request.getBairro());
        savedEmpresa.setCidade(request.getCidade());
        savedEmpresa.setIsActive(true);

        when(empresaRepository.save(any(Empresa.class))).thenReturn(savedEmpresa);

        Empresa result = empresaService.save(request);

        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("Nova Empresa", result.getNomeEmpresa());
        assertTrue(result.getIsActive());
        verify(empresaRepository, times(1)).save(any(Empresa.class));
    }

    @Test
    void update_ShouldSaveAndReturnEmpresa() {
        UpdateEmpresaRequest request = new UpdateEmpresaRequest();
        request.setNomeEmpresa("Empresa Atualizada");
        request.setCnpj("11111111111111");

        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));
        when(empresaRepository.save(any(Empresa.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Empresa result = empresaService.update(1L, request);

        assertNotNull(result);
        assertEquals("Empresa Atualizada", result.getNomeEmpresa());
        assertEquals("11111111111111", result.getCnpj());
        verify(empresaRepository, times(1)).findById(1L);
        verify(empresaRepository, times(1)).save(any(Empresa.class));
    }

    @Test
    void disable_ShouldDisableEmpresa() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));
        when(empresaRepository.save(any(Empresa.class))).thenAnswer(invocation -> invocation.getArgument(0));

        empresaService.disable(1L);

        assertFalse(empresa.getIsActive());
        verify(empresaRepository, times(1)).findById(1L);
        verify(empresaRepository, times(1)).save(any(Empresa.class));
    }

    @Test
    void disable_WhenNotFound_ShouldThrowNotFoundException() {
        when(empresaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> empresaService.disable(99L));
        verify(empresaRepository, never()).save(any(Empresa.class));
    }
}
