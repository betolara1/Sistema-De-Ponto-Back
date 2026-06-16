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

import betolara1.Ponto.dto.PontoDTO;
import betolara1.Ponto.dto.request.SavePontoRequest;
import betolara1.Ponto.dto.request.UpdatePontoRequest;
import betolara1.Ponto.model.Colaboradores;
import betolara1.Ponto.model.Ponto;
import betolara1.Ponto.service.PontoService;
import com.betolara1.jwt_package.security.JwtAuthFilter;

@WebMvcTest(controllers = PontoController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@AutoConfigureMockMvc(addFilters = false)
public class PontoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PontoService pontoService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter; // Mocked to satisfy SecurityConfig constructor and context loading

    @Autowired
    private ObjectMapper objectMapper;

    private PontoDTO pontoDTO;
    private Ponto pontoEntity;
    private Colaboradores colab;
    private Colaboradores colabUpdate;

    @BeforeEach
    void setUp() {
        // PontoDTO(Long id, Long colaboradorId, Long colaboradorIdUpdate)
        pontoDTO = new PontoDTO(10L, 1L, 2L);

        colab = new Colaboradores();
        colab.setId(1L);
        colab.setNomeColaborador("Colaborador Teste");

        colabUpdate = new Colaboradores();
        colabUpdate.setId(2L);
        colabUpdate.setNomeColaborador("Responsavel Teste");

        pontoEntity = new Ponto();
        pontoEntity.setId(10L);
        pontoEntity.setColaboradorId(colab);
        pontoEntity.setColaboradorIdUpdate(colabUpdate);
    }

    @Test
    void getAll_ShouldReturnPageOfPontoDTO() throws Exception {
        Page<PontoDTO> page = new PageImpl<>(Collections.singletonList(pontoDTO));
        when(pontoService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/ponto")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "dateCreated")
                .param("direction", "desc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(10))
                .andExpect(jsonPath("$.content[0].colaboradorId").value(1))
                .andExpect(jsonPath("$.content[0].colaboradorIdUpdate").value(2));
    }

    @Test
    void getById_ShouldReturnPontoDTO() throws Exception {
        when(pontoService.findById(10L)).thenReturn(pontoDTO);

        mockMvc.perform(get("/ponto/{id}", 10L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.colaboradorId").value(1));
    }

    @Test
    void getColaboradorId_ShouldReturnPontoDTO() throws Exception {
        when(pontoService.findByColaboradorId(1L)).thenReturn(pontoDTO);

        mockMvc.perform(get("/ponto")
                .param("colabId", "")
                .param("id", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.colaboradorId").value(1));
    }

    @Test
    void getColaboradorIdUpdater_ShouldReturnPontoDTO() throws Exception {
        when(pontoService.findByColaboradorIdUpdater(2L)).thenReturn(pontoDTO);

        mockMvc.perform(get("/ponto")
                .param("colabUpdater", "")
                .param("id", "2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.colaboradorIdUpdate").value(2));
    }

    @Test
    void getDateCreated_ShouldReturnPageOfPontoDTO() throws Exception {
        Page<PontoDTO> page = new PageImpl<>(Collections.singletonList(pontoDTO));
        when(pontoService.getByPontoCreated(eq("2026-06-16"), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/ponto")
                .param("dateCreated", "")
                .param("dateString", "2026-06-16")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(10));
    }

    @Test
    void getDateUpdated_ShouldReturnPageOfPontoDTO() throws Exception {
        Page<PontoDTO> page = new PageImpl<>(Collections.singletonList(pontoDTO));
        when(pontoService.getByPontoUpdated(eq("2026-06-16"), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/ponto")
                .param("dataUpdated", "")
                .param("dateString", "2026-06-16")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(10));
    }

    @Test
    void savePonto_ShouldReturnPontoDTO() throws Exception {
        SavePontoRequest request = new SavePontoRequest();
        request.setColaboradorId(1L);

        when(pontoService.save(any(SavePontoRequest.class))).thenReturn(pontoEntity);

        mockMvc.perform(post("/ponto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                // new PontoDTO(pontoEntity) will result in:
                // id = colab.getId() -> 1
                // colaboradorId = colabUpdate.getId() -> 2
                // colaboradorIdUpdate = pontoEntity.getId() -> 10
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.colaboradorId").value(2))
                .andExpect(jsonPath("$.colaboradorIdUpdate").value(10));
    }

    @Test
    void updatePonto_ShouldReturnPontoDTO() throws Exception {
        UpdatePontoRequest request = new UpdatePontoRequest();
        request.setColaboradorIdUpdate(2L);
        request.setPontoUpdated(LocalDateTime.now());

        when(pontoService.update(any(UpdatePontoRequest.class), eq(1L))).thenReturn(pontoEntity);

        mockMvc.perform(put("/ponto")
                .param("id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.colaboradorId").value(2))
                .andExpect(jsonPath("$.colaboradorIdUpdate").value(10));
    }
}
