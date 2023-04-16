package com.da.wrk.aircraft.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.da.wrk.aircraft.domain.AircraftEntity;
import com.da.wrk.aircraft.repository.AircraftRepository;
import com.da.wrk.aircraft.service.AircraftService;
import com.da.wrk.aircraft.web.model.Aircraft;

@Component
public class AircraftServiceImpl implements AircraftService {

	static final Logger logger = LoggerFactory.getLogger(AircraftServiceImpl.class);

	private final AircraftRepository aircraftRepo;

	@Autowired
	AircraftServiceImpl(AircraftRepository aircraftRepo) {
		this.aircraftRepo = aircraftRepo;
	}

	private final Function<Aircraft, AircraftEntity> aircraftToEntity = new Function<Aircraft, AircraftEntity>() {
		@Override
		public AircraftEntity apply(Aircraft aircraft) {
			if (aircraft.getId() == 0) {
				return new AircraftEntity(aircraft.getCategory(), aircraft.getModel());
			} else {
				return new AircraftEntity(aircraft.getId(), aircraft.getCategory(), aircraft.getModel());
			}
		}
	};

	private final Function<AircraftEntity, Aircraft> entityToAircraft = new Function<AircraftEntity, Aircraft>() {
		@Override
		public Aircraft apply(AircraftEntity aircraftEntity) {
			return new Aircraft(aircraftEntity.getId(), aircraftEntity.getCategory(), aircraftEntity.getModel());
		}
	};

	@Override
	public Optional<Aircraft> getAircraftById(Long id) {
		return aircraftRepo.findById(id).map(s -> entityToAircraft.apply(s));
	}

	@Override
	public List<Aircraft> getAllAircrafts() {
		return aircraftRepo.findAll().parallelStream().map(s -> entityToAircraft.apply(s)).collect(Collectors.toList());
	}

	@Override
	public boolean deleteAircraft(Long id) {
		aircraftRepo.deleteById(id);
		return true;
	}

	@Override
	public Optional<Aircraft> createUpdateAircraft(Aircraft aircraft) {
		if (aircraft.getId() == null) {
			aircraft.setId(Long.valueOf(0));
		}
		AircraftEntity aircraftEntity = aircraftRepo.save(aircraftToEntity.apply(aircraft));
		return Optional.of(entityToAircraft.apply(aircraftEntity));
	}
}
