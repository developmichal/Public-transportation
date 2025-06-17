package com.example.demo.Converters;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.demo.Dto.StationDto;
import com.example.demo.Models.Station;
import com.example.demo.Repositories.StationLineRepository;

@Component
public class StationConverter {

    @Autowired
    private StationLineRepository stationLineRepository;

    public StationDto ConvertToDto(Station station) {
        StationDto newStationDto = new StationDto();
        newStationDto.setId(station.getId());
        newStationDto.setName(station.getName());
        newStationDto.setPhone(station.getPhone());
        newStationDto.setStationLines(station.getStationLines().stream().
        map(x -> x.getId()).collect(Collectors.toList()));
        return newStationDto;
    }

    public Station ConvertToStation(StationDto stationDto) {
        Station newStation = new Station();
        newStation.setName(stationDto.getName());
        newStation.setPhone(stationDto.getPhone());
        newStation.setStationLines(stationLineRepository.findAllById(stationDto.getStationLines()));
        return newStation;
    }

    public List<StationDto> converToListDto(List<Station> stations) {
        return stations.stream().map(x -> ConvertToDto(x)).collect(Collectors.toList());
    }
}
