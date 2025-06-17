package com.example.demo.Models;

import java.util.List;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String number;
    private String source;
    private String destination;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "line", fetch = FetchType.LAZY)
    private List<Travel> travels;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "line", fetch = FetchType.LAZY)
    // private List<Station_Line> stationLines;
    // @OneToMany(cascade = CascadeType.ALL,mappedBy = "line", orphanRemoval = true)
    @OrderBy("stationOrder ASC")
    private List<Station_Line> stationLines;

}
