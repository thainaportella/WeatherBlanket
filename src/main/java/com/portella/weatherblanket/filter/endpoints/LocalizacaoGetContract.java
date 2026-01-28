package com.portella.weatherblanket.filter.endpoints;

import com.portella.weatherblanket.filter.EndpointContract;

import java.util.Set;

public class LocalizacaoGetContract {

    public static final EndpointContract CONTRACT =
            new EndpointContract(
                    "/localizacao",
                    "GET",
                    Set.of(),
                    false
            );
}
