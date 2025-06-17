package com.example.demo.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.Station;

public interface StationRepository extends JpaRepository<Station,Long> {
    Optional<Station> findByPhone(String phone);

    
}  
    

