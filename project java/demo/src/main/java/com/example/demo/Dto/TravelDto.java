package com.example.demo.Dto;

import java.sql.Time;

import lombok.Data;

@Data
public class TravelDto {
    private Long bus_id;
    private String tzDriver;
    private Long line_id;
    private Time departureTime;
}
