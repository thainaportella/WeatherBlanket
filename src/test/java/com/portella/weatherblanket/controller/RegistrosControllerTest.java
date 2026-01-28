package com.portella.weatherblanket.controller;

import com.portella.weatherblanket.filter.ContractValidationFilter;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = RegistrosController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = ContractValidationFilter.class
        )
)
class RegistrosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveListarRegistrosSemFiltro() throws Exception {

        List<String> csvMock = List.of(
                "data,hora,temperatura,cor,cor_oficial",
                "2026-01-01,15:00,25.0,amarelo,solar",
                "2026-01-02,15:00,26.0,mostarda,mostarda"
        );

        try (MockedStatic<Files> filesMock = Mockito.mockStatic(Files.class)) {

            filesMock.when(() ->
                    Files.readAllLines(Mockito.any(Path.class))
            ).thenReturn(csvMock);

            mockMvc.perform(get("/registros"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].data").value("2026-01-02"))
                    .andExpect(jsonPath("$[1].data").value("2026-01-01"));
        }
    }

    @Test
    void deveFiltrarPorMesEAno() throws Exception {

        List<String> csvMock = List.of(
                "data,hora,temperatura,cor,cor_oficial",
                "2026-01-01,15:00,25.0,amarelo,solar",
                "2025-12-01,15:00,24.0,verde,musgo"
        );

        try (MockedStatic<Files> filesMock = Mockito.mockStatic(Files.class)) {

            filesMock.when(() ->
                    Files.readAllLines(Mockito.any(Path.class))
            ).thenReturn(csvMock);

            mockMvc.perform(
                            get("/registros")
                                    .param("mes", "1")
                                    .param("ano", "2026")
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].data").value("2026-01-01"));
        }
    }

    @Test
    void deveRetornarErroQuandoMesSemAno() throws Exception {

        List<String> csvMock = List.of(
                "data,hora,temperatura,cor,cor_oficial",
                "2026-01-01,15:00,25.0,amarelo,solar"
        );

        try (MockedStatic<Files> filesMock = Mockito.mockStatic(Files.class)) {

            filesMock.when(() ->
                    Files.readAllLines(Mockito.any(Path.class))
            ).thenReturn(csvMock);

            mockMvc.perform(
                            get("/registros")
                                    .param("mes", "1")
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.message")
                            .value("O parâmetro 'mes' deve ser informado junto de 'ano'."));
        }
    }

    @Test
    void deveAplicarLimit() throws Exception {

        List<String> csvMock = List.of(
                "data,hora,temperatura,cor,cor_oficial",
                "2026-01-01,15:00,25.0,amarelo,solar",
                "2026-01-02,15:00,26.0,mostarda,mostarda",
                "2026-01-03,15:00,27.0,laranja,brasa"
        );

        try (MockedStatic<Files> filesMock = Mockito.mockStatic(Files.class)) {

            filesMock.when(() ->
                    Files.readAllLines(Mockito.any(Path.class))
            ).thenReturn(csvMock);

            mockMvc.perform(
                            get("/registros")
                                    .param("limit", "2")
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2));
        }
    }

    @Test
    void deveOrdenarAsc() throws Exception {

        List<String> csvMock = List.of(
                "data,hora,temperatura,cor,cor_oficial",
                "2026-01-03,15:00,27.0,laranja,brasa",
                "2026-01-01,15:00,25.0,amarelo,solar"
        );

        try (MockedStatic<Files> filesMock = Mockito.mockStatic(Files.class)) {

            filesMock.when(() ->
                    Files.readAllLines(Mockito.any(Path.class))
            ).thenReturn(csvMock);

            mockMvc.perform(
                            get("/registros")
                                    .param("order", "asc")
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].data").value("2026-01-01"))
                    .andExpect(jsonPath("$[1].data").value("2026-01-03"));
        }
    }
}
