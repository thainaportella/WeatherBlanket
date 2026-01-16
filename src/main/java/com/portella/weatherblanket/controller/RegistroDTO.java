package com.portella.weatherblanket.controller;

public record RegistroDTO(
        String data,
        String hora,
        String temperatura,
        String cor,
        String cor_oficial
) {}
