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
@Table(name = "load") //schema = "wrk",
@Data
@NoArgsConstructor
public class LoadEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="load_generator")
    @SequenceGenerator(name="load_generator", sequenceName="load_sequence", initialValue=6, allocationSize=1)
    private Long id;

    public LoadEntity(final Long id, final String category) {        
        super();
        this.id = id;
    }
}