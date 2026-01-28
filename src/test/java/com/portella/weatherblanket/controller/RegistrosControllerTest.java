package com.portella.weatherblanket.controller;

import com.portella.weatherblanket.entities.RegistroDTO;
import com.portella.weatherblanket.repositories.TemperatureRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegistrosController.class)
class RegistrosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TemperatureRepository temperatureRepository;

    @Test
    void deveListarRegistrosSemFiltros() throws Exception {

        List<RegistroDTO> registros = List.of(
                new RegistroDTO(
                        "2026-01-28",
                        "13:10",
                        "25.6",
                        "AMARELO",
                        "solar"
                )
        );

        Mockito.when(
                temperatureRepository.listarRegistros(null, null, "desc", null)
        ).thenReturn(registros);

        mockMvc.perform(get("/registros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].data").value("2026-01-28"))
                .andExpect(jsonPath("$[0].hora").value("13:10"))
                .andExpect(jsonPath("$[0].temperatura").value("25.6"))
                .andExpect(jsonPath("$[0].cor").value("AMARELO"))
                .andExpect(jsonPath("$[0].cor_oficial").value("solar"));
    }

    @Test
    void deveListarRegistrosComAno() throws Exception {

        Mockito.when(
                temperatureRepository.listarRegistros(null, 2026, "desc", null)
        ).thenReturn(List.of());

        mockMvc.perform(get("/registros")
                        .param("ano", "2026"))
                .andExpect(status().isOk());
    }

    @Test
    void deveListarRegistrosComMesEAno() throws Exception {

        Mockito.when(
                temperatureRepository.listarRegistros(1, 2026, "asc", 5)
        ).thenReturn(List.of());

        mockMvc.perform(get("/registros")
                        .param("mes", "1")
                        .param("ano", "2026")
                        .param("order", "asc")
                        .param("limit", "5"))
                .andExpect(status().isOk());
    }
}
