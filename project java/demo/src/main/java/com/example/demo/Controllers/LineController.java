package com.example.demo.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Dto.LineDto;
import com.example.demo.Service.LineService;

@RestController
@RequestMapping("/line")
public class LineController {
    @Autowired
    private LineService lineService;

    @GetMapping("/getAll")
    public ResponseEntity<List<LineDto>> getAll() {
        return ResponseEntity.ok().body(lineService.getAllLines());
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody LineDto lineDto) {
        try {
            String result = lineService.createLine(lineDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (RuntimeException ex) {
            // חריגה כללית
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

    }

    @PostMapping("/addStation")
    public ResponseEntity<String> addStation(@RequestParam Long lineID, @RequestParam Long stationID,
            @RequestParam int position) {
        try {
            String result = lineService.addStation(lineID, stationID, position);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (RuntimeException ex) {
            // חריגה כללית
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

    }

    @DeleteMapping("/deleteStation")
    public ResponseEntity<String> deleteStation(@RequestParam Long lineID, @RequestParam Long stationID) {
        try {
            String result = lineService.deleteStation(lineID, stationID);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (RuntimeException ex) {
            // חריגה כללית
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

    }

    @GetMapping("/searchByStation")
    public ResponseEntity<String> searchByStation(@RequestParam String number, @RequestParam String phoneStation) {
        try {
            String result = lineService.searchByStation(number, phoneStation);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (RuntimeException ex) {
            // חריגה כללית
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/searchByAllLine")
    public ResponseEntity<String> searchByAllLine(@RequestParam String phoneStation) {
        try {
            String result = lineService.searchByAllLine(phoneStation);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (RuntimeException ex) {
            // חריגה כללית
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/theLocationOfTheBusesAlongTheRoute")
    public ResponseEntity<String> theLocationOfTheBusesAlongTheRoute(@RequestParam String lineNumber) {

        try {
            String result = lineService.theLocationOfTheBusesAlongTheRoute(lineNumber);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (RuntimeException ex) {
            // אם זה באמת רק הקו שלא נמצא
            if (ex.getMessage().contains("לא נמצאו אוטובוסים לאורך ציר הנסיעה")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
            }
            // חריגה כללית
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("אירעה שגיאה: " + ex.getMessage());
        }
    }

    @GetMapping("/getAllStationsOnLine")
    public ResponseEntity<String> toHearAllTheStationsOnTheLine(@RequestParam String lineNumber) {
        try {
            String result = lineService.getAllStationsOnLine(lineNumber);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (RuntimeException ex) {
            // חריגה כללית
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

}