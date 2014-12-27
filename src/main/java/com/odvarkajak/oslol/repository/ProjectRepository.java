package com.odvarkajak.oslol.repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.odvarkajak.oslol.domain.Project;

public interface ProjectRepository {

	Collection loadProjects();
	
    void saveProject(Project project);

    @Transactional(readOnly = true)
    Project findProjectById(Long projectId);    
    
    @Transactional(readOnly = true)
    List<Project> findProjectsByname(String query);
       
    void update(Project project);
    
    void delete(Project project);
    
    
}
