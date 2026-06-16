package betolara1.Ponto.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import betolara1.Ponto.dto.EmpresaDTO;
import betolara1.Ponto.dto.request.SaveEmpresaRequest;
import betolara1.Ponto.dto.request.UpdateEmpresaRequest;
import betolara1.Ponto.model.Empresa;
import betolara1.Ponto.service.EmpresaService;
import com.betolara1.jwt_package.security.JwtAuthFilter;

@WebMvcTest(controllers = EmpresaController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@AutoConfigureMockMvc(addFilters = false)
public class EmpresaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpresaService empresaService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter; // Mocked to satisfy SecurityConfig constructor and context loading

    @Autowired
    private ObjectMapper objectMapper;

    private EmpresaDTO empresaDTO;

    @BeforeEach
    void setUp() {
        empresaDTO = new EmpresaDTO(
            1L,
            "Empresa Teste",
            "12345678000199",
            "Rua Teste, 123",
            "12345-678",
            "Bairro Teste",
            "Cidade Teste",
            true,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    @Test
    void getAll_ShouldReturnPageOfEmpresaDTO() throws Exception {
        Page<EmpresaDTO> page = new PageImpl<>(Collections.singletonList(empresaDTO));
        when(empresaService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/empresa")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "dateCreated")
                .param("direction", "desc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nomeEmpresa").value("Empresa Teste"))
                .andExpect(jsonPath("$.content[0].cnpj").value("12345678000199"));
    }

    @Test
    void getById_ShouldReturnEmpresaDTO() throws Exception {
        when(empresaService.findById(1L)).thenReturn(empresaDTO);

        mockMvc.perform(get("/empresa/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeEmpresa").value("Empresa Teste"));
    }

    @Test
    void getActive_ShouldReturnPageOfEmpresaDTO() throws Exception {
        Page<EmpresaDTO> page = new PageImpl<>(Collections.singletonList(empresaDTO));
        when(empresaService.findByIsActive(eq(true), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/empresa")
                .param("active", "true")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nomeEmpresa").value("Empresa Teste"));
    }

    @Test
    void getDateCreated_ShouldReturnPageOfEmpresaDTO() throws Exception {
        Page<EmpresaDTO> page = new PageImpl<>(Collections.singletonList(empresaDTO));
        when(empresaService.getByDateCreated(eq("2026-06-11"), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/empresa")
                .param("dateCreated", "2026-06-11")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nomeEmpresa").value("Empresa Teste"));
    }

    @Test
    void getDateUpdated_ShouldReturnPageOfEmpresaDTO() throws Exception {
        Page<EmpresaDTO> page = new PageImpl<>(Collections.singletonList(empresaDTO));
        when(empresaService.getByDateUpdated(eq("2026-06-11"), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/empresa")
                .param("dataUpdated", "2026-06-11")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nomeEmpresa").value("Empresa Teste"));
    }

    @Test
    void getNome_ShouldReturnEmpresaDTO() throws Exception {
        when(empresaService.findByName("Teste")).thenReturn(empresaDTO);

        mockMvc.perform(get("/empresa")
                .param("nome", "Teste")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeEmpresa").value("Empresa Teste"));
    }

    @Test
    void getCNPJ_ShouldReturnEmpresaDTO() throws Exception {
        when(empresaService.findByCNPJ("12345678000199")).thenReturn(empresaDTO);

        mockMvc.perform(get("/empresa")
                .param("cnpj", "12345678000199")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeEmpresa").value("Empresa Teste"));
    }

    @Test
    void saveEmpresa_ShouldReturnEmpresaDTO() throws Exception {
        SaveEmpresaRequest request = new SaveEmpresaRequest();
        request.setNomeEmpresa("Empresa Teste");
        request.setCnpj("12345678000199");
        request.setIsActive(true);

        Empresa savedEmpresa = new Empresa();
        savedEmpresa.setId(1L);
        savedEmpresa.setNomeEmpresa("Empresa Teste");
        savedEmpresa.setCnpj("12345678000199");
        savedEmpresa.setIsActive(true);

        when(empresaService.save(any(SaveEmpresaRequest.class))).thenReturn(savedEmpresa);

        mockMvc.perform(post("/empresa")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeEmpresa").value("Empresa Teste"));
    }

    @Test
    void updateEmpresa_ShouldReturnEmpresaDTO() throws Exception {
        UpdateEmpresaRequest request = new UpdateEmpresaRequest();
        request.setNomeEmpresa("Empresa Atualizada");

        Empresa updatedEmpresa = new Empresa();
        updatedEmpresa.setId(1L);
        updatedEmpresa.setNomeEmpresa("Empresa Atualizada");

        when(empresaService.update(eq(1L), any(UpdateEmpresaRequest.class))).thenReturn(updatedEmpresa);

        mockMvc.perform(put("/empresa/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeEmpresa").value("Empresa Atualizada"));
    }

    @Test
    void disableEmpresa_ShouldReturnNoContent() throws Exception {
        doNothing().when(empresaService).disable(1L);

        mockMvc.perform(delete("/empresa/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(empresaService, times(1)).disable(1L);
    }
}
