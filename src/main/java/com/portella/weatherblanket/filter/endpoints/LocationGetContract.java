package com.portella.weatherblanket.filter.endpoints;

import com.portella.weatherblanket.filter.EndpointContract;

import java.util.Set;

public class LocationGetContract {

    public static final EndpointContract CONTRACT =
            new EndpointContract(
                    "/location",
                    "GET",
                    Set.of(),
                    false
            );
}
