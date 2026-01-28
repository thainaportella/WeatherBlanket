package com.portella.weatherblanket.filter.endpoints;

import com.portella.weatherblanket.filter.EndpointContract;

import java.util.Set;

public class LocalizacaoResetPutContract {

    public static final EndpointContract CONTRACT =
            new EndpointContract(
                    "/localizacao/reset",
                    "PUT",
                    Set.of(),
                    false
            );
}

