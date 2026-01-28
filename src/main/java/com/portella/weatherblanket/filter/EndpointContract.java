package com.portella.weatherblanket.filter;

import java.util.Set;

public class EndpointContract {

    private final String path;
    private final String method;
    private final Set<String> queryParams;
    private final boolean bodyRequired;

    public EndpointContract(
            String path,
            String method,
            Set<String> queryParams,
            boolean bodyRequired
    ) {
        this.path = path;
        this.method = method;
        this.queryParams = queryParams;
        this.bodyRequired = bodyRequired;
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }

    public Set<String> getQueryParams() {
        return queryParams;
    }

    public boolean isBodyRequired() {
        return bodyRequired;
    }
}

