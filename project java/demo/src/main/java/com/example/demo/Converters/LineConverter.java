package com.example.demo.Converters;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.Dto.LineDto;
import com.example.demo.Models.Line;
// import com.example.demo.Repositories.StationLineRepository;
import com.example.demo.Repositories.TravelRepository;

@Component
public class LineConverter {

    @Autowired
    private TravelRepository travelRepository;

    // @Autowired
    // private StationLineRepository stationLineRepository;

    public LineDto ConvertToDto(Line line) {
        LineDto newLineDto = new LineDto();
        newLineDto.setId(line.getId());
        newLineDto.setNumber(line.getNumber());
        newLineDto.setSource(line.getSource());
        newLineDto.setDestination(line.getDestination());
        newLineDto.setTravels(line.getTravels().stream().map(x -> x.getId()).collect(Collectors.toList()));
        newLineDto.setStationLines(line.getStationLines().stream().map(x -> x.getId()).collect(Collectors.toList()));
        return newLineDto;
    }

    public Line ConvertToLine(LineDto lineDto) {
        Line newLine = new Line();
        newLine.setNumber(lineDto.getNumber());
        newLine.setSource(lineDto.getSource());
        newLine.setDestination(lineDto.getDestination());
        newLine.setTravels(travelRepository.findAllById(lineDto.getTravels()));
        // newLine.setStationLines(stationLineRepository.findAllById(lineDto.getStationLines()));
        return newLine;
    }

    public List<LineDto> converToListDto(List<Line> lines) {
        return lines.stream().map(x -> ConvertToDto(x)).collect(Collectors.toList());
    }
}
