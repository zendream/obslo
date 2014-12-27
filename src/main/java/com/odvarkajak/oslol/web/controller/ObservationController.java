package com.odvarkajak.oslol.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.odvarkajak.oslol.domain.Observation;
import com.odvarkajak.oslol.repository.ObservationRepository;

@Controller
public class ObservationController {
	
	@Autowired
	ObservationRepository observationRepository;
	
	 @SuppressWarnings("unchecked")
		@ModelAttribute("allObservations")
	    public List<Observation> populateObservations() {
	        return (List<Observation>) this.observationRepository.loadObservations();
	    }
	 
	 @RequestMapping(value = "/observation/listAll")
	    public String projectList() {
	        return "view/observation/list";
	    }
}
