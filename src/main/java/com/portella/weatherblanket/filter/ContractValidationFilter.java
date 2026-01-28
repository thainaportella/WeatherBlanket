package com.portella.weatherblanket.filter;

import com.portella.weatherblanket.exceptions.ContractViolationException;
import com.portella.weatherblanket.support.ContractValidator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ContractValidationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws IOException, ServletException {

        try {
            ContractRegistry.find(request.getRequestURI())
                    .ifPresent(contract ->
                            ContractValidator.validate(request, contract)
                    );

            filterChain.doFilter(request, response);

        } catch (ContractViolationException ex) {

            response.setStatus(ex.getStatus());
            response.setContentType("application/json");

            response.getWriter().write(
                    """
                    {
                      "status": %d,
                      "error": "%s",
                      "message": "%s"
                    }
                    """.formatted(
                            ex.getStatus(),
                            ex.getError(),
                            ex.getMessage()
                    )
            );
        }
    }
}

