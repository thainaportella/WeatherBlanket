package com.portella.weatherblanket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portella.weatherblanket.filter.ContractValidationFilter;
import com.portella.weatherblanket.model.LocationRequest;
import com.portella.weatherblanket.model.LocationResponse;
import com.portella.weatherblanket.service.LocationService;
import com.portella.weatherblanket.service.TokenService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
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
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LocationService locationService;
    @MockBean
    private TokenService tokenService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveBuscarLocalizacaoAtual() throws Exception {

        LocationResponse mock = new LocationResponse(
                -22.42,
                -42.98,
                "Magé",
                "RJ",
                "Brasil"
        );

        Mockito.when(locationService.getLocation(
                Mockito.anyDouble(),
                Mockito.anyDouble()
        )).thenReturn(mock);

        mockMvc.perform(get("/location"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("Magé"))
                .andExpect(jsonPath("$.state").value("RJ"))
                .andExpect(jsonPath("$.country").value("Brasil"));
    }

    @Test
    void deveAtualizarLocalizacao() throws Exception {

        LocationRequest request = new LocationRequest();
        request.setLatitude(-22.4138);
        request.setLongitude(-43.1720);

        LocationResponse mock = new LocationResponse(
                -22.4138,
                -43.1720,
                "Petrópolis",
                "RJ",
                "Brasil"
        );

        Mockito.when(locationService.getLocation(
                Mockito.anyDouble(),
                Mockito.anyDouble()
        )).thenReturn(mock);

        mockMvc.perform(
                        put("/location")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("Petrópolis"))
                .andExpect(jsonPath("$.state").value("RJ"))
                .andExpect(jsonPath("$.country").value("Brasil"));
    }

    @Test
    void deveResetarLocalizacao() throws Exception {

        LocationResponse mock = new LocationResponse(
                -22.4209,
                -42.9801,
                "Magé",
                "RJ",
                "Brasil"
        );

        Mockito.when(locationService.getLocation(
                Mockito.anyDouble(),
                Mockito.anyDouble()
        )).thenReturn(mock);

        mockMvc.perform(put("/location/reset"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("Magé"))
                .andExpect(jsonPath("$.state").value("RJ"))
                .andExpect(jsonPath("$.country").value("Brasil"));
    }
}
