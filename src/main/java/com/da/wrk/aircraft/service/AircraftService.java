package com.da.wrk.aircraft.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

import com.da.wrk.aircraft.web.model.Aircraft;

@Service
public interface AircraftService {
    public Optional<Aircraft> getAircraftById(Long id);
    public List<Aircraft> getAllAircrafts();
    public boolean deleteAircraft(Long id);
    public Optional<Aircraft> createUpdateAircraft(Aircraft aircraft);
}