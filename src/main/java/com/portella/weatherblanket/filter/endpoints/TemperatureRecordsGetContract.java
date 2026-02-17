package com.portella.weatherblanket.filter.endpoints;

import com.portella.weatherblanket.filter.EndpointContract;

import java.util.Set;

public class TemperatureRecordsGetContract {

    public static final EndpointContract CONTRACT =
            new EndpointContract(
                    "/temperature-records",
                    "GET",
                    Set.of("limit", "month", "year", "order"),
                    false
            );

}
