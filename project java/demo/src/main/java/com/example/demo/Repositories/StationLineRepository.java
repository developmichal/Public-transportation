package com.example.demo.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.Station_Line;

@Repository
public interface StationLineRepository extends JpaRepository<Station_Line, Long> {
    Optional<Station_Line> findByLineIdAndStationId(Long lineId, Long stationId);

    List<Station_Line> findByLineIdAndStationOrderGreaterThanEqual(Long lineId, int stationOrder);

    Optional<Station_Line> findByLineIdAndStationOrder(Long lineId, int stationOrder);

    List<Station_Line> findByLineId(Long lineId);

}
