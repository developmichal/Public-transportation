package com.example.demo.Converters;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.Dto.BusDto;
import com.example.demo.Models.Bus;
import com.example.demo.Repositories.TravelRepository;

@Component
public class BusConverter {

    @Autowired
    private TravelRepository travelRepository;

    public BusDto ConvertToDto(Bus bus) {
        BusDto newBusDto = new BusDto();
        newBusDto.setId(bus.getId());
        newBusDto.setLicensePlate(bus.getLicensePlate());
        newBusDto.setSeats(bus.getSeats());
        newBusDto.setTravels(bus.getTravels().stream().map(x -> x.getId())
                .collect(Collectors.toList()));
        return newBusDto;
    }

    public Bus ConvertToBus(BusDto busDto) {
        Bus newBus = new Bus();
        newBus.setLicensePlate(busDto.getLicensePlate());
        newBus.setSeats(busDto.getSeats());
        newBus.setTravels(travelRepository.findAllById(busDto.getTravels()));
        return newBus;
    }

    public List<BusDto> converToListDto(List<Bus> buses) {
        return buses.stream().map(x -> ConvertToDto(x)).collect(Collectors.toList());
    }
}
