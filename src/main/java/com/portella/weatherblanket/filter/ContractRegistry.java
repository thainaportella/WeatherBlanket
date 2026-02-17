package com.portella.weatherblanket.filter;

import com.portella.weatherblanket.filter.endpoints.*;
import org.springframework.util.AntPathMatcher;

import java.util.List;
import java.util.Optional;

public class ContractRegistry {

    private static final List<EndpointContract> CONTRACTS = List.of(
            TemperatureRecordsGetContract.CONTRACT,
            LocationGetContract.CONTRACT,
            LocationPutContract.CONTRACT,
            TemperatureRecordsPutContract.CONTRACT,
            LocationResetPutContract.CONTRACT,
            TokenPostContract.CONTRACT
    );

    public static Optional<EndpointContract> find(String path, String method) {
        AntPathMatcher pathMatcher = new AntPathMatcher();

        return CONTRACTS.stream()
                .filter(c -> pathMatcher.match(c.getPath(), path) && c.getMethod().equalsIgnoreCase(method))
                .findFirst();
    }
}

