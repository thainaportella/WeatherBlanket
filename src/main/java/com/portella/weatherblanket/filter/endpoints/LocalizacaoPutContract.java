package com.portella.weatherblanket.filter.endpoints;

import com.portella.weatherblanket.filter.EndpointContract;

import java.util.Set;

public class LocalizacaoPutContract {

    public static final EndpointContract CONTRACT =
            new EndpointContract(
                    "/localizacao",
                    "PUT",
                    Set.of(),
                    true
            );
}


