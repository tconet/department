package com.department.entity;

import com.department.types.StockType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column( unique = true, nullable = false)
    private String symbol;
    private String companyName;
    @Column( unique = true, nullable = false)
    private String document;
    private String description;
    private String image;
    @Column(nullable = false)
    private StockType type;
}
