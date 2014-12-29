package com.odvarkajak.oslol.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.odvarkajak.oslol.repository.ObservationRepository;
import com.odvarkajak.oslol.repository.ProjectRepository;
import com.odvarkajak.oslol.repository.UserRepository;

@Service
public class FileAccessService {
    
    @Value("${fileDir.mainFolder}")
    private String mainFolder;
    @Value("${fileDir.imagesFolder}")
    private String imagesFolder;
    @Value("${fileDir.dataFolder}")
    private String dataFolder;
    
	@Autowired 
	ProjectRepository projectRepository;
	
	@Autowired 
	UserRepository userRepository;
	
	@Autowired 
	ObservationRepository observationRepository;

}
