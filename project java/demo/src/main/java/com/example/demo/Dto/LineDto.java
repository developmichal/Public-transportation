package com.example.demo.Dto;

import java.util.List;

import lombok.Data;

@Data
public class LineDto {
    private Long id;
    private String number;
    private String source;
    private String destination;
    private List<Long> travels;
    private List<Long> stationLines;

}
