package com.portella.weatherblanket.filter.endpoints;

import com.portella.weatherblanket.filter.EndpointContract;

import java.util.Set;

public class LocationPutContract {

    public static final EndpointContract CONTRACT =
            new EndpointContract(
                    "/location",
                    "PUT",
                    Set.of(),
                    true
            );
}


