package com.portella.weatherblanket.entities;

public record RegistroDTO(
        String data,
        String hora,
        String temperatura,
        String cor,
        String cor_oficial
) {}
