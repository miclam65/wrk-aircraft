
package com.da.wrk.aircraft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.da.wrk.aircraft.domain.AircraftEntity;

@Repository
public interface AircraftRepository extends JpaRepository<AircraftEntity, Long>{
}