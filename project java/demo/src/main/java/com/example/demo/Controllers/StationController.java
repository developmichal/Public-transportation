package com.example.demo.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.Dto.StationDto;
import com.example.demo.Service.StationService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/station")
public class StationController {

    @Autowired
    private StationService stationService;


    @GetMapping("/getAll")
    public ResponseEntity<List<StationDto>> getAll() {
        return ResponseEntity.ok().body(stationService.getAllStations());
    }

    public String getMethodName(@RequestParam String param) {
        return new String();
    }

    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody StationDto stationDto) {
        if (stationService.creatStation(stationDto))
            return ResponseEntity.status(HttpStatus.CREATED).build();
        return ResponseEntity.badRequest().build();
    }
    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam Long stationId) {
        if (stationService.deleteStation(stationId))
            return ResponseEntity.noContent().build();
        return ResponseEntity.badRequest().build();
    }
    

}
