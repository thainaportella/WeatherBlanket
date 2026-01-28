package com.portella.weatherblanket.DTO;

public record RegistroDTO(
        String data,
        String hora,
        String temperatura,
        String cor,
        String cor_oficial
) {}
