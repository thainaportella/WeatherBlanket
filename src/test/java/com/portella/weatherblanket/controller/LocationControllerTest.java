package com.portella.weatherblanket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portella.weatherblanket.filter.ContractValidationFilter;
import com.portella.weatherblanket.model.Localizacao;
import com.portella.weatherblanket.model.LocalizacaoRequest;
import com.portella.weatherblanket.service.LocationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = LocationController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = ContractValidationFilter.class
        )
)
class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationService locationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveBuscarLocalizacaoAtual() throws Exception {

        Localizacao mock = new Localizacao(
                -22.42,
                -42.98,
                "Magé",
                "RJ",
                "Brasil"
        );

        Mockito.when(locationService.buscarLocalizacao(
                Mockito.anyDouble(),
                Mockito.anyDouble()
        )).thenReturn(mock);

        mockMvc.perform(get("/localizacao"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cidade").value("Magé"))
                .andExpect(jsonPath("$.estado").value("RJ"))
                .andExpect(jsonPath("$.pais").value("Brasil"));
    }

    @Test
    void deveAtualizarLocalizacao() throws Exception {

        LocalizacaoRequest request = new LocalizacaoRequest();
        request.setLatitude(-22.4138);
        request.setLongitude(-43.1720);

        Localizacao mock = new Localizacao(
                -22.4138,
                -43.1720,
                "Petrópolis",
                "RJ",
                "Brasil"
        );

        Mockito.when(locationService.buscarLocalizacao(
                Mockito.anyDouble(),
                Mockito.anyDouble()
        )).thenReturn(mock);

        mockMvc.perform(
                        put("/localizacao")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cidade").value("Petrópolis"))
                .andExpect(jsonPath("$.estado").value("RJ"))
                .andExpect(jsonPath("$.pais").value("Brasil"));
    }

    @Test
    void deveResetarLocalizacao() throws Exception {

        Localizacao mock = new Localizacao(
                -22.4209,
                -42.9801,
                "Magé",
                "RJ",
                "Brasil"
        );

        Mockito.when(locationService.buscarLocalizacao(
                Mockito.anyDouble(),
                Mockito.anyDouble()
        )).thenReturn(mock);

        mockMvc.perform(put("/localizacao/reset"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cidade").value("Magé"))
                .andExpect(jsonPath("$.estado").value("RJ"))
                .andExpect(jsonPath("$.pais").value("Brasil"));
    }
}
