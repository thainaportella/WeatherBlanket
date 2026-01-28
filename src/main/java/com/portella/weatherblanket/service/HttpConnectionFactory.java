package com.portella.weatherblanket.service;

import java.net.HttpURLConnection;
import java.net.URL;

public interface HttpConnectionFactory {
    HttpURLConnection create(URL url);
}
