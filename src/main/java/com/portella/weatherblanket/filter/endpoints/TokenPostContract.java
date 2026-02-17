package com.portella.weatherblanket.filter.endpoints;

import com.portella.weatherblanket.filter.EndpointContract;

import java.util.Set;

public class TokenPostContract {

    public static final EndpointContract CONTRACT =
            new EndpointContract(
                    "/login",
                    "POST",
                    Set.of(),
                    true
            );
}
