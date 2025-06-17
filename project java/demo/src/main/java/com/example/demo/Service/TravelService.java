package com.example.demo.Service;

import java.sql.Time;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.example.demo.Converters.TravelConverter;
import com.example.demo.Dto.TravelDto;
import com.example.demo.Models.Line;
import com.example.demo.Models.Travel;
import com.example.demo.Repositories.BusRepository;
import com.example.demo.Repositories.DriverRepository;
import com.example.demo.Repositories.LineRepository;
import com.example.demo.Repositories.TravelRepository;

@Service
public class TravelService {
    @Autowired
    private TravelConverter travelConverter;
    @Autowired
    private TravelRepository travelRepository;
    @Autowired
    private BusRepository busRepository;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private LineRepository lineRepository;

    public List<TravelDto> getAllTravel() {
        return travelConverter.converToListDto(travelRepository.findAll());
    }

    public boolean creatTravel(TravelDto travelDto) {
        if (busRepository.findById(travelDto.getBus_id()).isEmpty())
            throw new IllegalArgumentException("קו אוטובוס" + travelDto.getBus_id() + " לא קיימת במערכת");
        if (driverRepository.findById(travelDto.getTzDriver()).isEmpty())
            throw new IllegalArgumentException("נהג" + travelDto.getTzDriver() + " לא קיימת במערכת");
        if (lineRepository.findById(travelDto.getLine_id()).isEmpty())
            throw new IllegalArgumentException("קו" + travelDto.getLine_id() + " לא קיימת במערכת");
        Travel travel = travelConverter.convertToTravel(travelDto);
        if (!travelRepository.exists(Example.of(travel))) {
            travelRepository.save(travel);
            return true;
        }
        return false;
    }

    public String getAllTravelsOnLine(String numberLine) {
        Line line = lineRepository.findByNumber(numberLine)
                .orElseThrow(() -> new RuntimeException("קו לא נמצא"));
        line.getTravels().sort(Comparator.comparing(Travel::getDeparture_time));
        if (line.getTravels().isEmpty())
            throw new RuntimeException("לא נמצאו נסיעות לקו זה");

        StringBuilder result = new StringBuilder();
        result.append("קו ")
                .append(numberLine)
                .append(" יצא מתחנת המוצא ");
        for (Travel travel : line.getTravels()) {
            result.append("בשעה : ")
                    .append(travel.getDeparture_time())
                    .append("\n");
        }
        return result.toString();
    }

    public String getTravelsInCertainTime(String numberLine, String timeString) {
        Time time = Time.valueOf(timeString);
        Line line = lineRepository.findByNumber(numberLine)
                .orElseThrow(() -> new RuntimeException("קו לא נמצא"));
        List<Travel> travels = line.getTravels().stream()
                .filter(x -> !x.getDeparture_time().before(time))
                .sorted(Comparator.comparing(Travel::getDeparture_time))
                .limit(5)
                .collect(Collectors.toList());
        if (travels.isEmpty())
            throw new RuntimeException("לא נמצאו נסיעות לקו זה בשעה זו");
        StringBuilder result = new StringBuilder();
        result.append("קו ")
                .append(numberLine)
                .append(" יצא מתחנת המוצא ");
        for (Travel travel : travels) {
            result.append("בשעה : ")
                    .append(travel.getDeparture_time())
                    .append("\n");
        }
        return result.toString();
    }

    public String getLastTravelOfTheDay(String numberLine) {
        Line line = lineRepository.findByNumber(numberLine)
                .orElseThrow(() -> new RuntimeException("קו לא נמצא"));
        if (line.getTravels().isEmpty())
            throw new RuntimeException("לא נמצאו נסיעות לקו זה");
        List<Travel> travels = line.getTravels();
        travels.sort(Comparator.comparing(Travel::getDeparture_time).reversed());
        Travel latestTravel = travels.get(0);

        StringBuilder result = new StringBuilder();
        result.append("קו ")
                .append(numberLine)
                .append("יצא מתחנת המוצא בשעה:  ")
                .append(latestTravel.getDeparture_time())
                .append("נסיעה אחרונה ביום");
        return result.toString();
    }
}
