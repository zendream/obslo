package com.odvarkajak.oslol.repository;

import com.odvarkajak.oslol.domain.Observation;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

public interface ObservationRepository {

	@Transactional(readOnly = true)
	Collection loadObservations();
	
	@Transactional(readOnly = false)
    void saveObservation(Observation observation);

    @Transactional(readOnly = true)
    Observation findObservationById(Long observationId);    
    
    @Transactional(readOnly = true)
    List<Observation> findObservationsByname(String query);
       
    @Transactional(readOnly = false)
    void update(Observation observation);
    
    @Transactional(readOnly = false)
    void delete(Observation observation);
}
