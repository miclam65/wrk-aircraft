package com.da.wrk.aircraft.web.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.da.wrk.aircraft.service.AircraftService;
import com.da.wrk.aircraft.web.model.Aircraft;

@RestController
@RequestMapping("/aircrafts")
public class AircraftController {

	static final Logger logger = LoggerFactory.getLogger(AircraftController.class);

	private final AircraftService aircraftService;

	@Autowired
	AircraftController(AircraftService aircraftService) {
		this.aircraftService = aircraftService;
	}

	@GetMapping
	public ResponseEntity<List<Aircraft>> getAllAircraft() {
		logger.info("getAllAircraft");
		return ResponseEntity.ok(aircraftService.getAllAircrafts());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Aircraft> getAircraftById(@PathVariable("id") Long id) {
		logger.info("getAircraft id=" + id);
		return aircraftService.getAircraftById(id).map(aircraft -> {
			return ResponseEntity.ok(aircraft);
		}).orElseGet(() -> {
			return new ResponseEntity<Aircraft>(HttpStatus.NOT_FOUND);
		});
	}

	@PostMapping
	public ResponseEntity<Aircraft> createAircraft(@RequestBody Aircraft aircraft) {
		logger.info("createAircraft id=gen");
		return createUpdateAircraft(aircraft);
	}

	@PutMapping
	public ResponseEntity<Aircraft> updateAircraft(@RequestBody Aircraft aircraft) {
		logger.info("updateAircraft id=" + aircraft.getId());
		return createUpdateAircraft(aircraft);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<String> deleteAircraft(@PathVariable("id") Long id) {
		if (aircraftService.deleteAircraft(id)) {
			logger.info("deleteAircraft id=" + id);
			return ResponseEntity.ok("Aircraft with id : " + id + " is deleted");
		} else {
			return new ResponseEntity<String>("Error deleting entity ", HttpStatus.EXPECTATION_FAILED);
		}
	}

	private ResponseEntity<Aircraft> createUpdateAircraft(Aircraft aircraft) {
		return aircraftService.createUpdateAircraft(aircraft).map(a -> {
			return ResponseEntity.ok(a);
		}).orElseGet(() -> {
			return new ResponseEntity<Aircraft>(HttpStatus.EXPECTATION_FAILED);
		});
	}	
}
