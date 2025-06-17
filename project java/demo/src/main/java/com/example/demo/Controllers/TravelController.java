package com.example.demo.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.Dto.TravelDto;
import com.example.demo.Service.TravelService;

@RestController
@RequestMapping("/travel")
public class TravelController {
    @Autowired
    private TravelService travelService;

    @GetMapping("/getAll")
    public ResponseEntity<List<TravelDto>> getAll() {
        return ResponseEntity.ok().body(travelService.getAllTravel());
    }
    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody TravelDto travelDto) {
        if (travelService.creatTravel(travelDto))
            return ResponseEntity.status(HttpStatus.CREATED).build();
        return ResponseEntity.badRequest().build();

    }
    
    @GetMapping("/getAllTravelsOnLine")
    public ResponseEntity<String> getAllTravelsOnLine(@RequestParam String lineNumber) {
        try {
            String result = travelService.getAllTravelsOnLine(lineNumber);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (RuntimeException ex) {
            // חריגה כללית
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/getTravelsInCertainTime")
    public ResponseEntity<String> getTravelsInCertainTime(@RequestParam String lineNumber,@RequestParam String time) {
        try {
            String result = travelService.getTravelsInCertainTime(lineNumber,time);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (RuntimeException ex) {
            // חריגה כללית
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
    @GetMapping("/getLastTravelOfTheDay")
    public ResponseEntity<String> getLastTravelOfTheDay(@RequestParam String lineNumber) {
        try {
            String result = travelService.getLastTravelOfTheDay(lineNumber);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (RuntimeException ex) {
            // חריגה כללית
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
}