package com.example.demo.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.Dto.DriverDto;
import com.example.demo.Service.DriverService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/driver")
public class DriverController {
    @Autowired
    private DriverService driverService;

    @GetMapping("/getAll")
    public ResponseEntity<List<DriverDto>> getAll() {
        return ResponseEntity.ok().body(driverService.getAllDrivers());
    }

    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody DriverDto driverDto) {
        if (driverService.createDriver(driverDto))
            return ResponseEntity.status(HttpStatus.CREATED).build();
        return ResponseEntity.badRequest().build();

    }

}
