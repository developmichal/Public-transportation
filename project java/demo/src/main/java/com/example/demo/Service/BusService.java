package com.example.demo.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.example.demo.Converters.BusConverter;
import com.example.demo.Dto.BusDto;
import com.example.demo.Models.Bus;
import com.example.demo.Repositories.BusRepository;

@Service
public class BusService {
    @Autowired
    public BusRepository busRepository;

    @Autowired
    public BusConverter busConverter;

    public List<BusDto> getAllBuses(){
        return busConverter.converToListDto(busRepository.findAll());
    }
    public boolean createBus(BusDto busDto) {
        Bus bus = busConverter.ConvertToBus(busDto);
        if (!busRepository.exists(Example.of(bus))) {
            busRepository.save(bus);
            return true;
        }
        return false;
    }
}
