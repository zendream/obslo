package com.odvarkajak.oslol.service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.odvarkajak.oslol.domain.Observation;
import com.odvarkajak.oslol.domain.Project;
import com.odvarkajak.oslol.domain.User;
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
    
    @Transactional(readOnly = true)
    public Set<String> getUserNamesWithAccess(Observation observation){
    	Set<String> ret = new HashSet<String>();
    	ret.add(observation.getAuthor().getEmail());
    	for(User user : observation.getUsers()){
    		ret.add(user.getEmail());
    	}
    	for(Project project : observation.getProjects()){
    		for (User user: project.getUsers()){
    			ret.add(user.getEmail());
    		}
    	}
		return ret;
    }
    @Transactional(readOnly = true)
    public Set<String> getUserNamesWithDirectAccess(Observation observation){
    	Set<String> ret = new HashSet<String>();
    	ret.add(observation.getAuthor().getEmail());
    	for(User user : observation.getUsers()){
    		ret.add(user.getEmail());
    	}
		return ret;
    }
    @Transactional(readOnly = true)
    public List<Project> getProjectsAssociatedWith(Observation observation){
    	List<Project> ret = new LinkedList<Project>();
    	for(Project project : observation.getProjects()){
    		ret.add(project);
    	}    	
		return ret;
    }
    
}