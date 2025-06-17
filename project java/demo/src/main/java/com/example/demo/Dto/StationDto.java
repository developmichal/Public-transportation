package com.example.demo.Dto;

import java.util.List;

import lombok.Data;

@Data
public class StationDto {
    private Long id;
    private String name;
    private String phone;
    private List<Long> stationLines;

}
