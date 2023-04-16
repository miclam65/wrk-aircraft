package com.da.wrk.aircraft.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.da.wrk.aircraft.web.model.Category;

@Entity
@Table(name = "aircraft") //schema = "wrk",
@Data
@NoArgsConstructor
public class AircraftEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="aircraft_generator")
    @SequenceGenerator(name="aircraft_generator", sequenceName="aircraft_sequence", initialValue=6, allocationSize=1)
    private Long id;

    private String category;
    private String model;
    
    public AircraftEntity(final String category, final String model) {
        super();
        this.category = category;
        this.model = model;
    }

    public AircraftEntity(final Long id, final String category, final String model) {        
        super();
        this.id = id;
        this.category = category;
        this.model = model;
    }

    public String getCategory () {        
        return Category.valueOf(category).getValue();
    }
}