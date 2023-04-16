package com.da.wrk.aircraft.web.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.springframework.stereotype.Component;
import lombok.Data;

@Data
@Component
public class Aircraft {
    
    private Long id;
    private String category;

    @NotBlank
    @Size(min=3, message="Model must be at least 3 characters long")
    private String model;

    public Aircraft() {}

    public Aircraft(Long id, String category, String model) {
        this.id = id;
        this.category = category;
        this.model = model;
    }
}