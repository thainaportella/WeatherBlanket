package com.portella.weatherblanket.entities;

import com.portella.weatherblanket.entities.enums.DayStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "temperatures")
public class TemperatureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private LocalTime time;
    private double temperature;
    private String color;
    private String yarn_color;
    @Enumerated(EnumType.STRING)
    private DayStatusEnum status = DayStatusEnum.PENDING;
}
