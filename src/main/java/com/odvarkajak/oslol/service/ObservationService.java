package com.odvarkajak.oslol.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.odvarkajak.oslol.domain.Observation;
import com.odvarkajak.oslol.repository.ObservationRepository;

@Service
@Component("observationService")
public class ObservationService {

	@Autowired 
	ObservationRepository observationRepository;

    @SuppressWarnings("unchecked")
	public List<Observation> findAll() throws DataAccessException {
        return (List<Observation>) this.observationRepository.loadObservations();
    }

    public void add(final Observation observation) {
        this.observationRepository.saveObservation(observation);
    }
    
    public void edit(final Observation observation) {
        this.observationRepository.update(observation);
    }
    
    public Observation findById(final Long id) {
        return this.observationRepository.findObservationById(id);
    }
}