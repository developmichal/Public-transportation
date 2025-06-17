package com.example.demo.Converters;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.Dto.TravelDto;
import com.example.demo.Models.Travel;
import com.example.demo.Repositories.BusRepository;
import com.example.demo.Repositories.DriverRepository;
import com.example.demo.Repositories.LineRepository;

@Component
public class TravelConverter {
    @Autowired
    private BusRepository busRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private LineRepository lineRepository;

    public TravelDto ConvertToDto(Travel travel) {
        TravelDto newTravelDto = new TravelDto();
        newTravelDto.setDepartureTime(travel.getDeparture_time());
        newTravelDto.setBus_id(travel.getBus().getId());
        newTravelDto.setLine_id(travel.getLine().getId());
        newTravelDto.setTzDriver(travel.getDriver().getTz());
        return newTravelDto;
    }

    public Travel convertToTravel(TravelDto travelDto) {
        Travel newTravel = new Travel();
        newTravel.setDeparture_time(travelDto.getDepartureTime());
        newTravel.setBus(busRepository.findById(travelDto.getBus_id()).get());
        newTravel.setDriver(driverRepository.findById(travelDto.getTzDriver()).get());
        newTravel.setLine(lineRepository.findById(travelDto.getLine_id()).get());
        return newTravel;
    }

    public List<TravelDto> converToListDto(List<Travel> travels) {
        return travels.stream().map(x -> ConvertToDto(x)).collect(Collectors.toList());
    }
}
