package com.example.demo.Converters;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.Dto.DriverDto;
import com.example.demo.Models.Driver;
import com.example.demo.Repositories.TravelRepository;

@Component
public class DriverConverter {

    @Autowired
    private TravelRepository travelRepository;

    public DriverDto ConvertToDto(Driver driver) {
        DriverDto newDriverDto = new DriverDto();
        newDriverDto.setTz(driver.getTz());
        ;
        newDriverDto.setName(driver.getName());
        ;
        newDriverDto.setPhone(driver.getPhone());
        ;
        newDriverDto.setTravels(driver.getTravels().stream().map(x -> x.getId())
                .collect(Collectors.toList()));
        return newDriverDto;
    }

    public Driver ConvertToDriver(DriverDto driverDto) {
        Driver newDriver = new Driver();
        newDriver.setTz(driverDto.getTz());
        newDriver.setName(driverDto.getName());
        newDriver.setPhone(driverDto.getPhone());
        newDriver.setTravels(travelRepository.findAllById(driverDto.getTravels()));
        return newDriver;
    }

    public List<DriverDto> converToListDto(List<Driver> drivers) {
        return drivers.stream().map(x -> ConvertToDto(x)).collect(Collectors.toList());
    }
}
