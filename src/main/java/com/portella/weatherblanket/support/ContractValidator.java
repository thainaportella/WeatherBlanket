package com.portella.weatherblanket.support;

import com.portella.weatherblanket.exceptions.ContractViolationException;
import com.portella.weatherblanket.filter.EndpointContract;
import jakarta.servlet.http.HttpServletRequest;

public class ContractValidator {

    public static void validate(
            HttpServletRequest request,
            EndpointContract contract
    ) {

        if (!request.getMethod().equals(contract.getMethod())) {
            throw new ContractViolationException(
                    405,
                    "METHOD_NOT_ALLOWED",
                    "Método inválido para este endpoint."
            );
        }

        request.getParameterMap().keySet().forEach(param -> {
            if (!contract.getQueryParams().contains(param)) {
                throw new ContractViolationException(
                        400,
                        "INVALID_QUERY_PARAM",
                        "Parâmetro inválido: " + param
                );
            }
        });

        if (contract.isBodyRequired() && request.getContentLength() == 0) {
            throw new ContractViolationException(
                    400,
                    "BODY_REQUIRED",
                    "Body obrigatório para este endpoint."
            );
        }
    }
}

