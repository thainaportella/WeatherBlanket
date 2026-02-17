package com.portella.weatherblanket.filter.endpoints;

import com.portella.weatherblanket.filter.EndpointContract;

import java.util.Set;

public class LocationResetPutContract {

    public static final EndpointContract CONTRACT =
            new EndpointContract(
                    "/location/reset",
                    "PUT",
                    Set.of(),
                    false
            );
}

