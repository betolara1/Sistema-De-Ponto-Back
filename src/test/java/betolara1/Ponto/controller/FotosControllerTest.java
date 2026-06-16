package betolara1.Ponto.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
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

import betolara1.Ponto.dto.FotosDTO;
import betolara1.Ponto.dto.request.SaveFotosRequest;
import betolara1.Ponto.dto.request.UpdateFotosRequest;
import betolara1.Ponto.model.Colaboradores;
import betolara1.Ponto.model.Fotos;
import betolara1.Ponto.service.FotosService;
import com.betolara1.jwt_package.security.JwtAuthFilter;

@WebMvcTest(controllers = FotosController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@AutoConfigureMockMvc(addFilters = false)
public class FotosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FotosService fotosService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private FotosDTO fotosDTO;

    @BeforeEach
    void setUp() {
        fotosDTO = new FotosDTO(
            1L,
            new byte[]{1, 2, 3},
            1L
        );
    }

    @Test
    void getAll_ShouldReturnPageOfFotosDTO() throws Exception {
        Page<FotosDTO> page = new PageImpl<>(Collections.singletonList(fotosDTO));
        when(fotosService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/fotos")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "dateCreated")
                .param("direction", "desc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].colaboradorId").value(1L));
    }

    @Test
    void getById_ShouldReturnFotosDTO() throws Exception {
        when(fotosService.findById(1L)).thenReturn(fotosDTO);

        mockMvc.perform(get("/fotos/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.colaboradorId").value(1L));
    }

    @Test
    void getColaboradorId_ShouldReturnFotosDTO() throws Exception {
        when(fotosService.findByColaboradorId(1L)).thenReturn(fotosDTO);

        mockMvc.perform(get("/fotos")
                .param("colaboradorId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.colaboradorId").value(1L));
    }

    @Test
    void getSaveFotos_ShouldReturnFotosDTO() throws Exception {
        SaveFotosRequest request = new SaveFotosRequest();
        request.setColaboradorId(1L);
        request.setFoto(new byte[]{1, 2, 3});

        Colaboradores colab = new Colaboradores();
        colab.setId(1L);

        Fotos savedFoto = new Fotos();
        savedFoto.setId(1L);
        savedFoto.setFoto(new byte[]{1, 2, 3});
        savedFoto.setColaboradorId(colab);

        when(fotosService.save(any(SaveFotosRequest.class))).thenReturn(savedFoto);

        mockMvc.perform(post("/fotos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.colaboradorId").value(1L));
    }

    @Test
    void getUpdateFotos_ShouldReturnFotosDTO() throws Exception {
        UpdateFotosRequest request = new UpdateFotosRequest();
        request.setColaboradorId(1L);
        request.setFoto(new byte[]{4, 5, 6});

        Colaboradores colab = new Colaboradores();
        colab.setId(1L);

        Fotos updatedFoto = new Fotos();
        updatedFoto.setId(1L);
        updatedFoto.setFoto(new byte[]{4, 5, 6});
        updatedFoto.setColaboradorId(colab);

        when(fotosService.update(any(UpdateFotosRequest.class), eq(1L))).thenReturn(updatedFoto);

        mockMvc.perform(put("/fotos/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.colaboradorId").value(1L));
    }
}
