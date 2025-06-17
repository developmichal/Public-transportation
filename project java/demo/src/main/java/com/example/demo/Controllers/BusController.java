package com.example.demo.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.Dto.BusDto;
import com.example.demo.Service.BusService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/bus")
public class BusController {
    @Autowired
    private BusService busService;

    @GetMapping("/getAll")
    public ResponseEntity<List<BusDto>> getAll() {
        return ResponseEntity.ok().body(busService.getAllBuses());
    }

    public String getMethodName(@RequestParam String param) {
        return new String();
    }

    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody BusDto busDto) {
        if (busService.createBus(busDto))
            return ResponseEntity.status(HttpStatus.CREATED).build();
        return ResponseEntity.badRequest().build();

    }

}
