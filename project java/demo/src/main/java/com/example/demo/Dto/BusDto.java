package com.example.demo.Dto;

import java.util.List;

import lombok.Data;

@Data
public class BusDto {
    private Long id;
    private String licensePlate;
    private int	seats;
    private List<Long> travels;

}
