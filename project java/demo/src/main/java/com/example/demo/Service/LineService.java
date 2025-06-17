package com.example.demo.Service;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.example.demo.Converters.LineConverter;
import com.example.demo.Dto.LineDto;
import com.example.demo.Models.Line;
import com.example.demo.Models.Station;
import com.example.demo.Models.Station_Line;
import com.example.demo.Models.Travel;
import com.example.demo.Repositories.LineRepository;
import com.example.demo.Repositories.StationLineRepository;
import com.example.demo.Repositories.StationRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class LineService {

    @Autowired
    public LineRepository lineRepository;
    @Autowired
    public LineConverter lineConverter;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private StationLineRepository stationLineRepository;

    public List<LineDto> getAllLines() {
        return lineConverter.converToListDto(lineRepository.findAll());
    }

    public String createLine(LineDto lineDto) {
        Line line = lineConverter.ConvertToLine(lineDto);

        List<Station_Line> stationsLine = new ArrayList<>();
        int position = 1;
        for (Long stationId : lineDto.getStationLines()) {
            Station station = stationRepository.findById(stationId)
                    .orElseThrow(() -> new RuntimeException("תחנה לא נמצאה"));
            Station_Line newStationLine = new Station_Line();
            newStationLine.setLine(line);
            newStationLine.setStation(station);
            newStationLine.setStationOrder(position++);
            stationsLine.add(newStationLine);
        }
        line.setStationLines(stationsLine);
        if (!lineRepository.exists(Example.of(line))) {
            lineRepository.save(line);
            return "הקו נוסף בהצלחה";
        }
        return "ארעה שגיאה במהלך יצירת הקו";
    }

    public String addStation(Long lineId, Long stationId, int position) {

        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new RuntimeException(" קו לא נמצא"));

        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new RuntimeException("תחנה לא נמצאה"));

        List<Station_Line> stationLines = new ArrayList<>();
        Station_Line newStationLine = new Station_Line();

        if (position < 1 || position > line.getStationLines().size() + 1) {
            throw new IllegalArgumentException( "מספר מיקום התחנה לא חוקי ");
        }
        newStationLine.setLine(line);
        newStationLine.setStation(station);
        newStationLine.setStationOrder(position);
        

        stationLines=stationLineRepository.findByLineIdAndStationOrderGreaterThanEqual(lineId, position);
        for(Station_Line x:stationLines){
            x.setStationOrder(x.getStationOrder()+1);
        }

        stationLines.add(newStationLine);
        stationLineRepository.saveAll(stationLines);
        return "התחנה הוספה בהצלחה";
    }

    public String deleteStation(Long lineId, Long stationId) {
        Station_Line result = stationLineRepository.findByLineIdAndStationId(lineId, stationId)
                .orElseThrow(() -> new EntityNotFoundException("לא נמצאה תחנה תואמת לקו שהוכנס "));
        int temp = result.getStationOrder();
        stationLineRepository.delete(result);
        List<Station_Line> stationLines = stationLineRepository.findByLineIdAndStationOrderGreaterThanEqual(lineId, temp);

        for (Station_Line x : stationLines) {
            x.setStationOrder(x.getStationOrder() - 1);
        }
        stationLineRepository.saveAll(stationLines);
        return "המחיקה התבצעה בהצלחה";
    }

    public String searchByStation(String number, String phoneStation) {
        Station station = stationRepository.findByPhone(phoneStation)
                .orElseThrow(() -> new RuntimeException("תחנה לא נמצאה"));
        Line line = lineRepository.findByNumber(number)
                .orElseThrow(() -> new RuntimeException("הקו לא נמצא"));
        return searchByStation(line, station);
    }

    public String searchByStation(Line line, Station station) {
        Optional<Station_Line> stationLines = stationLineRepository.findByLineIdAndStationId(line.getId(),
                station.getId());
        if (stationLines.isEmpty()) {
            throw new RuntimeException("לא נמצאו פריטים התואמים לחיפוש");
        }
        int stationOrder = stationLines.get().getStationOrder();
        LocalTime now = LocalTime.now();
        LocalTime updatedTime = now.minusMinutes(stationOrder);
        Time departureTime = Time.valueOf(updatedTime);

        List<Travel> travels = line.getTravels().stream()
                .filter(x -> x.getDeparture_time().after(departureTime))
                .sorted(Comparator.comparing(Travel::getDeparture_time))
                .limit(5)
                .collect(Collectors.toList());
        if (travels.isEmpty()) {
            throw new RuntimeException("לא נמצאו נסיעות התואמות לחיפוש בזמן שחיפשת");
        }
        StringBuilder result = new StringBuilder();

        for (Travel travel : travels) {
            LocalTime arrivalTime = travel.getDeparture_time().toLocalTime().plusMinutes(stationOrder);
            Long minutes = Duration.between(LocalTime.now(), arrivalTime).toMinutes();

            if (travel.getDeparture_time() != null
                    && travel.getDeparture_time().toLocalTime().isAfter(LocalTime.now())) {
                result.append("קו ")
                        .append(line.getNumber())
                        .append("  יצא מתחנת המוצא בשעה")
                        .append(travel.getDeparture_time())
                        .append(" ויגיע לתחנה בשעה ")
                        .append(arrivalTime)
                        .append("\n");
            } else {
                result.append("קו ")
                        .append(line.getNumber())
                        .append(" יגיע לתחנה ")
                        .append(station.getName())
                        .append(" בעוד ")
                        .append(minutes)
                        .append(" דקות.\n");
            }
        }
        return result.toString();
    }

    public String searchByAllLine(String phoneStation) {
        Station station = stationRepository.findByPhone(phoneStation)
                .orElseThrow(() -> new RuntimeException("תחנה לא נמצאה"));
        List<Station_Line> stationLines = station.getStationLines();
        if (stationLines.isEmpty()) {
            throw new RuntimeException("לא נמצאו קווים בתחנה זו");
        }
        StringBuilder result = new StringBuilder();
        for (Station_Line x : stationLines) {
            Line line = lineRepository.findById(x.getLine().getId())
                    .orElseThrow(() -> new RuntimeException("לא נמצא קו תואם לתחנה זו"));
                    
            String fullResult = searchByStation(line, station);
            String firstLine = fullResult.split("\n")[0]; // לוקח רק את השורה הראשונה
            result.append(firstLine).append("\n");
        }
        if (result.length() == 0) {
            throw new RuntimeException("לא נמצאו קווים בתחנה זו");
        }
        return result.toString();
    }

    public String theLocationOfTheBusesAlongTheRoute(String LineNumber) {
        Line line = lineRepository.findByNumber(LineNumber)
                .orElseThrow(() -> new RuntimeException(" קו לא נמצא"));

        int sizeStationLine = line.getStationLines().size();

        LocalTime now = LocalTime.now();
        LocalTime updatedTime = now.minusMinutes(sizeStationLine);
        Time departureTime = Time.valueOf(updatedTime);

        List<Travel> travels = line.getTravels().stream()
                .filter(x -> x.getDeparture_time().after(departureTime)
                        && x.getDeparture_time().before(Time.valueOf(now)))
                .sorted(Comparator.comparing(Travel::getDeparture_time))
                .collect(Collectors.toList());

        StringBuilder result = new StringBuilder();

        if (travels.isEmpty()) {
            result.append("לא נמצאו קווים לאורך ציר הנסיעה");
            return result.toString();
        }
        for (Travel travel : travels) {
            int minutes = (int) Duration.between(travel.getDeparture_time().toLocalTime(), now).toMinutes();

            Station_Line station = stationLineRepository.findByLineIdAndStationOrder(line.getId(), minutes)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "תחנה לא נמצאה"));

            result.append(" קו")
                    .append(line.getNumber())
                    .append(" נמצא בקירבת תחנה  ")
                    .append(station.getStation().getName())
                    .append("\n");
        }

        return result.toString();
    }

    public String getAllStationsOnLine(String numberLine) {
        Line line = lineRepository.findByNumber(numberLine)
                .orElseThrow(() -> new RuntimeException("לא נמצא קו זה"));

        List<Station_Line> stationLines = stationLineRepository.findByLineId(line.getId());
        if (stationLines.isEmpty()) {
            throw new RuntimeException("לא נמצאו תחנות לקו זה");
        }

        stationLines.sort(Comparator.comparingInt(Station_Line::getStationOrder));
        StringBuilder result = new StringBuilder();
        for (Station_Line x : stationLines) {
            result.append("תחנה מספר ")
                    .append(x.getStationOrder())
                    .append(" : ")
                    .append(x.getStation().getName())
                    .append("\n");
        }
        return result.toString();
    }
}
