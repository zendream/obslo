package com.odvarkajak.oslol.repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.odvarkajak.oslol.domain.Project;

public interface ProjectRepository {
	
	@Transactional(readOnly = true)
	Collection loadProjects();
	
	@Transactional(readOnly = false)
    void saveProject(Project project);

    @Transactional(readOnly = true)
    Project findProjectById(Long projectId);  
    
    @Transactional(readOnly = true)
    List<Project> findProjectsByPhase(Long phase);
    
    @Transactional(readOnly = true)
    List<Project> findProjectsByname(String query);
       
    @Transactional(readOnly = false)
    void update(Project project);
    
    @Transactional(readOnly = false)
    void delete(Project project);
    
    @Transactional(readOnly = true)
	boolean isNameAlreadyExists(String name);

    @Transactional(readOnly = true)
	Project findProjectByName(String name);
    
    
}
