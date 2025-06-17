package com.example.demo.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.example.demo.Converters.StationConverter;
import com.example.demo.Dto.StationDto;
import com.example.demo.Models.Station;
import com.example.demo.Repositories.StationRepository;

@Service
public class StationService {

    @Autowired
    public StationRepository stationRepository;

    @Autowired
    public StationConverter stationConverter;

    public List<StationDto> getAllStations() {
        return stationConverter.converToListDto(stationRepository.findAll());
    }

    public boolean creatStation(StationDto stationDto) {
        Station station = stationConverter.ConvertToStation(stationDto);
        if (!stationRepository.exists(Example.of(station))) {
            stationRepository.save(station);
            return true;
        }
        return false;
    }

    public boolean deleteStation(Long stationId) {
        if (stationRepository.existsById(stationId)) {
            stationRepository.deleteById(stationId);
            return true;
        }
        return false;
    }

}
