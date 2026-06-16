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

import betolara1.Ponto.dto.ColaboradoresDTO;
import betolara1.Ponto.dto.request.SaveColaboradoresRequest;
import betolara1.Ponto.dto.request.UpdateColaboradoresRequest;
import betolara1.Ponto.model.Colaboradores;
import betolara1.Ponto.model.Empresa;
import betolara1.Ponto.service.ColaboradoresService;
import com.betolara1.jwt_package.security.JwtAuthFilter;

@WebMvcTest(controllers = ColaboradoresController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@AutoConfigureMockMvc(addFilters = false)
public class ColaboradoresControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ColaboradoresService colaboradoresService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private ColaboradoresDTO colaboradoresDTO;

    @BeforeEach
    void setUp() {
        colaboradoresDTO = new ColaboradoresDTO(
            1L,
            "Colaborador Teste",
            "12345678909",
            10L,
            true,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    @Test
    void getAll_ShouldReturnPageOfColaboradoresDTO() throws Exception {
        Page<ColaboradoresDTO> page = new PageImpl<>(Collections.singletonList(colaboradoresDTO));
        when(colaboradoresService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/colaboradores")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "dateCreated")
                .param("direction", "desc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nomeColaborador").value("Colaborador Teste"))
                .andExpect(jsonPath("$.content[0].cpf").value("12345678909"));
    }

    @Test
    void getById_ShouldReturnColaboradoresDTO() throws Exception {
        when(colaboradoresService.findById(1L)).thenReturn(colaboradoresDTO);

        mockMvc.perform(get("/colaboradores/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeColaborador").value("Colaborador Teste"));
    }

    @Test
    void getByNome_ShouldReturnColaboradoresDTO() throws Exception {
        when(colaboradoresService.findByNome("Teste")).thenReturn(colaboradoresDTO);

        mockMvc.perform(get("/colaboradores")
                .param("nome", "Teste")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeColaborador").value("Colaborador Teste"));
    }

    @Test
    void getByCpf_ShouldReturnColaboradoresDTO() throws Exception {
        when(colaboradoresService.findByCpf("12345678909")).thenReturn(colaboradoresDTO);

        mockMvc.perform(get("/colaboradores")
                .param("cpf", "12345678909")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeColaborador").value("Colaborador Teste"));
    }

    @Test
    void getByEmpresaId_ShouldReturnColaboradoresDTO() throws Exception {
        when(colaboradoresService.findByEmpresaId(10L)).thenReturn(colaboradoresDTO);

        mockMvc.perform(get("/colaboradores")
                .param("empresaId", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeColaborador").value("Colaborador Teste"));
    }

    @Test
    void getActive_ShouldReturnPageOfColaboradoresDTO() throws Exception {
        Page<ColaboradoresDTO> page = new PageImpl<>(Collections.singletonList(colaboradoresDTO));
        when(colaboradoresService.findByIsActive(eq(true), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/colaboradores")
                .param("active", "true")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nomeColaborador").value("Colaborador Teste"));
    }

    @Test
    void getDateCreated_ShouldReturnPageOfColaboradoresDTO() throws Exception {
        Page<ColaboradoresDTO> page = new PageImpl<>(Collections.singletonList(colaboradoresDTO));
        when(colaboradoresService.getByDateCreated(eq("2026-06-12"), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/colaboradores")
                .param("dateCreated", "2026-06-12")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nomeColaborador").value("Colaborador Teste"));
    }

    @Test
    void getDateUpdated_ShouldReturnPageOfColaboradoresDTO() throws Exception {
        Page<ColaboradoresDTO> page = new PageImpl<>(Collections.singletonList(colaboradoresDTO));
        when(colaboradoresService.getByDateUpdated(eq("2026-06-12"), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/colaboradores")
                .param("dataUpdated", "2026-06-12")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nomeColaborador").value("Colaborador Teste"));
    }

    @Test
    void saveColaboradores_ShouldReturnColaboradoresDTO() throws Exception {
        SaveColaboradoresRequest request = new SaveColaboradoresRequest();
        request.setNomeColaborador("Colaborador Teste");
        request.setCpf("12345678909");
        request.setSenha("senha123");
        request.setEmpresaId(10L);
        request.setIsActive(true);

        Empresa emp = new Empresa();
        emp.setId(10L);

        Colaboradores savedColab = new Colaboradores();
        savedColab.setId(1L);
        savedColab.setNomeColaborador("Colaborador Teste");
        savedColab.setCpf("12345678909");
        savedColab.setSenha("senha123");
        savedColab.setEmpresaId(emp);
        savedColab.setIsActive(true);

        when(colaboradoresService.save(any(SaveColaboradoresRequest.class))).thenReturn(savedColab);

        mockMvc.perform(post("/colaboradores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeColaborador").value("Colaborador Teste"));
    }

    @Test
    void updateColaboradores_ShouldReturnColaboradoresDTO() throws Exception {
        UpdateColaboradoresRequest request = new UpdateColaboradoresRequest();
        request.setNomeColaborador("Colaborador Atualizado");

        Empresa emp = new Empresa();
        emp.setId(10L);

        Colaboradores updatedColab = new Colaboradores();
        updatedColab.setId(1L);
        updatedColab.setNomeColaborador("Colaborador Atualizado");
        updatedColab.setCpf("12345678909");
        updatedColab.setSenha("senha123");
        updatedColab.setEmpresaId(emp);
        updatedColab.setIsActive(true);

        when(colaboradoresService.update(eq(1L), any(UpdateColaboradoresRequest.class))).thenReturn(updatedColab);

        mockMvc.perform(put("/colaboradores/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeColaborador").value("Colaborador Atualizado"));
    }

    @Test
    void disableColaborador_ShouldReturnNoContent() throws Exception {
        doNothing().when(colaboradoresService).delete(1L);

        mockMvc.perform(delete("/colaboradores/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(colaboradoresService, times(1)).delete(1L);
    }
}
