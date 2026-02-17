package com.portella.weatherblanket.entities.DTOs;

public record RegistroDTO(
        String date,
        String time,
        String temperature,
        String color,
        String yarn_color,
        String status
) {}
