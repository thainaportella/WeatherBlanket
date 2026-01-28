package com.portella.weatherblanket.service;

import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class DefaultHttpConnectionFactory implements HttpConnectionFactory {

    @Override
    public HttpURLConnection create(URL url) {
        try {
            return (HttpURLConnection) url.openConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

