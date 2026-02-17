package com.portella.weatherblanket.controller;

import com.portella.weatherblanket.entities.enums.DayStatusEnum;
import com.portella.weatherblanket.entities.DTOs.RegistroDTO;
import com.portella.weatherblanket.repositories.TemperatureRepository;
import com.portella.weatherblanket.service.TemperatureSchedulerService;
import com.portella.weatherblanket.service.TokenService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TemperatureRecordsController.class)
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TemperatureRecordsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TokenService tokenService;
    @MockBean
    private TemperatureRepository temperatureRepository;

    @MockBean
    private TemperatureSchedulerService temperatureSchedulerService;

    @Test
    void deveListarRegistrosSemFiltros() throws Exception {

        List<RegistroDTO> registros = List.of(
                new RegistroDTO(
                        "2026-01-28",
                        "13:10",
                        "25.6",
                        "YELLOW",
                        "solar",
                        DayStatusEnum.PENDING.getValue()
                )
        );

        Mockito.when(
                temperatureRepository.listRecords(null, null, "desc", null)
        ).thenReturn(registros);

        mockMvc.perform(get("/temperature-records"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].date").value("2026-01-28"))
                .andExpect(jsonPath("$[0].time").value("13:10"))
                .andExpect(jsonPath("$[0].temperature").value("25.6"))
                .andExpect(jsonPath("$[0].color").value("YELLOW"))
                .andExpect(jsonPath("$[0].yarn_color").value("solar"))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    @Test
    void deveListarRegistrosComAno() throws Exception {

        Mockito.when(
                temperatureRepository.listRecords(null, 2026, "desc", null)
        ).thenReturn(List.of());

        mockMvc.perform(get("/temperature-records")
                        .param("year", "2026"))
                .andExpect(status().isOk());
    }

    @Test
    void deveListarRegistrosComMesEAno() throws Exception {

        Mockito.when(
                temperatureRepository.listRecords(1, 2026, "asc", 5)
        ).thenReturn(List.of());

        mockMvc.perform(get("/temperature-records")
                        .param("month", "1")
                        .param("year", "2026")
                        .param("order", "asc")
                        .param("limit", "5"))
                .andExpect(status().isOk());
    }

}
