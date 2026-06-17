package betolara1.Ponto.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
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

import betolara1.Ponto.dto.CoordenadasDTO;
import betolara1.Ponto.dto.request.SaveCoordenadasRequest;
import betolara1.Ponto.dto.request.UpdateCoordenadasRequest;
import betolara1.Ponto.model.Coordenadas;
import betolara1.Ponto.model.Empresa;
import betolara1.Ponto.service.CoordenadasService;
import com.betolara1.jwt_package.security.JwtAuthFilter;

@WebMvcTest(controllers = CoordenadasController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@AutoConfigureMockMvc(addFilters = false)
public class CoordenadasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CoordenadasService coordenadasService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter; // Mocked to satisfy SecurityConfig constructor and context loading

    @Autowired
    private ObjectMapper objectMapper;

    private CoordenadasDTO coordenadasDTO;

    @BeforeEach
    void setUp() {
        coordenadasDTO = new CoordenadasDTO(
            10L,
            new BigDecimal("-23.55052"),
            new BigDecimal("-46.633308"),
            1L,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    @Test
    void getAll_ShouldReturnPageOfCoordenadasDTO() throws Exception {
        Page<CoordenadasDTO> page = new PageImpl<>(Collections.singletonList(coordenadasDTO));
        when(coordenadasService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/coordenadas")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "dateCreated")
                .param("direction", "desc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].latitude").value(-23.55052))
                .andExpect(jsonPath("$.content[0].empresaId").value(1L));
    }

    @Test
    void getByEmpresaId_ShouldReturnCoordenadasDTO() throws Exception {
        when(coordenadasService.findByEmpresaId(1L)).thenReturn(coordenadasDTO);

        mockMvc.perform(get("/coordenadas/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.latitude").value(-23.55052))
                .andExpect(jsonPath("$.empresaId").value(1L));
    }

    @Test
    void saveCoordenadas_ShouldReturnCoordenadasDTO() throws Exception {
        SaveCoordenadasRequest request = new SaveCoordenadasRequest();
        request.setLatitude("-23.55052");
        request.setLongitude("-46.633308");
        request.setEmpresaId(1L);

        Empresa empresa = new Empresa();
        empresa.setId(1L);

        Coordenadas savedCoordenadas = new Coordenadas();
        savedCoordenadas.setId(10L);
        savedCoordenadas.setLatitude(new BigDecimal("-23.55052"));
        savedCoordenadas.setLongitude(new BigDecimal("-46.633308"));
        savedCoordenadas.setEmpresaId(empresa);

        when(coordenadasService.save(any(SaveCoordenadasRequest.class))).thenReturn(savedCoordenadas);

        mockMvc.perform(post("/coordenadas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.latitude").value(-23.55052))
                .andExpect(jsonPath("$.empresaId").value(1L));
    }

    @Test
    void updateCoordenadas_ShouldReturnCoordenadasDTO() throws Exception {
        UpdateCoordenadasRequest request = new UpdateCoordenadasRequest();
        request.setLatitude(new BigDecimal("-25.4284"));

        Empresa empresa = new Empresa();
        empresa.setId(1L);

        Coordenadas updatedCoordenadas = new Coordenadas();
        updatedCoordenadas.setId(10L);
        updatedCoordenadas.setLatitude(new BigDecimal("-25.4284"));
        updatedCoordenadas.setLongitude(new BigDecimal("-46.633308"));
        updatedCoordenadas.setEmpresaId(empresa);

        when(coordenadasService.update(any(UpdateCoordenadasRequest.class), eq(10L))).thenReturn(updatedCoordenadas);

        mockMvc.perform(put("/coordenadas/{id}", 10L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.latitude").value(-25.4284))
                .andExpect(jsonPath("$.empresaId").value(1L));
    }
}
