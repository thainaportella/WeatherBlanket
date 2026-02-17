package com.portella.weatherblanket.filter;

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
                    "Invalid HTTP method for this endpoint."
            );
        }

        request.getParameterMap().keySet().forEach(param -> {
            if (!contract.getQueryParams().contains(param)) {
                throw new ContractViolationException(
                        400,
                        "INVALID_QUERY_PARAM",
                        "Invalid request parameter: " + param
                );
            }
        });

        if (contract.isBodyRequired() && request.getContentLength() == 0) {
            throw new ContractViolationException(
                    400,
                    "BODY_REQUIRED",
                    "Request body is required."
            );
        }

        if (contract.getPath().equals("/temperature-records")) {
            String month = request.getParameter("month");
            String year = request.getParameter("year");

            if (month != null && year == null) {
                throw new ContractViolationException(
                        400,
                        "Bad Request",
                        "The parameter 'month' requires the presence of the parameter 'year'."
                );
            }
        }
    }
}

