package com.odvarkajak.oslol.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.odvarkajak.oslol.domain.Project;
import com.odvarkajak.oslol.domain.User;
import com.odvarkajak.oslol.repository.ProjectRepository;
import com.odvarkajak.oslol.repository.UserRepository;

@Service
@Component("projectService")
public class ProjectService {

	@Autowired 
	ProjectRepository projectRepository;
	
	@Autowired 
	UserRepository userRepository;

    @SuppressWarnings("unchecked")
	public List<Project> findAll() throws DataAccessException {
        return (List<Project>) this.projectRepository.loadProjects();
    }

    public void add(final Project project) {
        this.projectRepository.saveProject(project);
    }
    
    public void edit(final Project project) {
        this.projectRepository.update(project);
    }
    
    public Project findById(final Long id) {
        return this.projectRepository.findProjectById(id);
    }
    public List<Long> getUserIdsWithAccess(Project project){
    	List<Long> ret = new LinkedList<Long>();
    	ret.add(project.getAuthor().getId());
    	for(User user : project.getUsers()){
    		ret.add(user.getId());
    	}    	
		return ret;
    }
    	
}
