package com.odvarkajak.oslol.repository;

import com.odvarkajak.oslol.domain.Observation;

import java.util.Collection;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

public interface ObservationRepository {

	Collection loadObservations();
	
    void saveObservation(Observation observation);

    @Transactional(readOnly = true)
    Observation findObservationById(Long observationId);    
    
    @Transactional(readOnly = true)
    List<Observation> findObservationsByname(String query);
       
    Observation update(Observation observation);
    
    void delete(Observation observation);
    
    @Transactional(readOnly = true)
	boolean isNameAlreadyExistsForThisUser(String name, Long userId);
}
