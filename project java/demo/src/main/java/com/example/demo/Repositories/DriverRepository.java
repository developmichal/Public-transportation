package com.example.demo.Repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.Driver;

@Repository
public interface DriverRepository extends JpaRepository<Driver, String>{

}
