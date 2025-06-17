package com.example.demo.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.example.demo.Converters.DriverConverter;
import com.example.demo.Dto.DriverDto;
import com.example.demo.Models.Driver;
import com.example.demo.Repositories.DriverRepository;

@Service
public class DriverService {
    @Autowired
    public DriverRepository driverRepository;

    @Autowired
    public DriverConverter driverConverter;

    public List<DriverDto> getAllDrivers() {
        return driverConverter.converToListDto(driverRepository.findAll());
    }

    public boolean createDriver(DriverDto driverDto) {
        Driver driver = driverConverter.ConvertToDriver(driverDto);
        if (!driverRepository.exists(Example.of(driver))) {
            driverRepository.save(driver);
            return true;
        }
        return false;
    }
}
