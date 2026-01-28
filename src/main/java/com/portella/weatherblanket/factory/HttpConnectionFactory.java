package com.portella.weatherblanket.factory;

import java.net.HttpURLConnection;
import java.net.URL;

public interface HttpConnectionFactory {
    HttpURLConnection create(URL url);
}
