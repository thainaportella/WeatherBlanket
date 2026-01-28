package com.portella.weatherblanket.filter.endpoints;

import com.portella.weatherblanket.filter.EndpointContract;

import java.util.Set;

public class RegistrosGetContract {

    public static final EndpointContract CONTRACT =
            new EndpointContract(
                    "/registros",
                    "GET",
                    Set.of("limit"),
                    false
            );
}
