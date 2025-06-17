package com.example.demo.Dto;

import java.util.List;

import lombok.Data;

@Data
public class DriverDto {
    private String tz;
    private String name;
    private String phone;
    private List<Long> travels;

}
