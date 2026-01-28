package com.portella.weatherblanket.filter;

import com.portella.weatherblanket.filter.endpoints.LocalizacaoGetContract;
import com.portella.weatherblanket.filter.endpoints.LocalizacaoPutContract;
import com.portella.weatherblanket.filter.endpoints.LocalizacaoResetPutContract;
import com.portella.weatherblanket.filter.endpoints.RegistrosGetContract;

import java.util.List;
import java.util.Optional;

public class ContractRegistry {

    private static final List<EndpointContract> CONTRACTS = List.of(
            RegistrosGetContract.CONTRACT,
            LocalizacaoGetContract.CONTRACT,
            LocalizacaoPutContract.CONTRACT,
            LocalizacaoResetPutContract.CONTRACT
    );

    public static Optional<EndpointContract> find(String path) {
        return CONTRACTS.stream()
                .filter(c -> c.getPath().equals(path))
                .findFirst();
    }
}

