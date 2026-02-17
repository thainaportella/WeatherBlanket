package com.portella.weatherblanket.filter.endpoints;

import com.portella.weatherblanket.filter.EndpointContract;

import java.util.Set;

public class TemperatureRecordsPutContract {

    public static final EndpointContract CONTRACT =
            new EndpointContract(
                    "/temperature-records/{date}/done",
                    "PUT",
                    Set.of(),
                    false
            );
}
