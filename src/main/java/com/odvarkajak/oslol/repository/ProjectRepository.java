package com.odvarkajak.oslol.repository;

import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.odvarkajak.oslol.domain.Observation;
import com.odvarkajak.oslol.domain.Project;

public interface ProjectRepository {

	/**
     * Finds all projects stored in the database.
     * @return
     */
    @Transactional(readOnly = true)
    public List<Project> findAllProjects();
    
    
    /**
     * Finds the count of project stored in the database.
     * @param searchTerm
     * @return
     */
    @Transactional(readOnly = true)
    public long findProjectCount(String searchTerm);
    /**
     * Finds projects for the requested page whose last name starts with the given search term.
     * @param searchTerm    The used search term.
     * @param page  The number of the requested page.
     * @return  A list of projects belonging to the requested page.
     */
    @Transactional(readOnly = true)
    public List<Project> findProjectsForPage(String searchTerm, int page);

    void saveProject(Project project);
    void update(Project project);
    
    @Transactional(readOnly = true)
    Set<Observation> loadUserOwnObservationByName(String username);
    
    
}
