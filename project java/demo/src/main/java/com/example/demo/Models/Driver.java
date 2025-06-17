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
public class Driver {
    @Id
    private String tz;
    private String name;
    private String phone;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "driver", fetch = FetchType.LAZY)
    private List<Travel> travels;
}
