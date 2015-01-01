package com.odvarkajak.oslol.repository;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.odvarkajak.oslol.domain.Project;
import com.odvarkajak.oslol.domain.User;

@Repository
public class ProjectRepositoryImpl implements ProjectRepository{

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager em;

    @SuppressWarnings("rawtypes")
	@Override
    public Collection loadProjects() {
        Query query = em.createQuery("from project");
        return query.getResultList();
    }

	@Override
	public void saveProject(Project project) {		
		em.merge(project);
		em.flush();
		
	}

	@Override
	public Project findProjectById(Long id) {		
		return (Project) em.createQuery("select o from project o where projectId = :projectId")
                .setParameter("projectId", id).getSingleResult();
	}

	@Override
	public List<Project> findProjectsByname(String name) {
		Query query = em.createQuery("select o from project o where name like :name")
                .setParameter("name",  "%" + name + "%");
		final List<Project> list = new LinkedList<>();
		for(final Object o : query.getResultList()) {
		    list.add((Project)o);
		}
		return list;
	}

	@Override
	public void update(Project project) {
		em.merge(project);
		em.flush();
		
	}

	@Override
	public void delete(Project project) {
		em.remove(project);
		em.flush();
		
	}
	@Override
    @Transactional(readOnly = true)
    public Project findProjectByName(String name) {
        return (Project) em.createQuery("select p from project p where name = :name")
                .setParameter("name", name).getSingleResult();

    }
	@Override
	public
    List<Project> findProjectsByPhase(Long phase){		
		Query query = em.createQuery("select o from project o where phase = :phase")
                .setParameter("phase",  phase.toString());
		final List<Project> list = new LinkedList<>();
		for(final Object o : query.getResultList()) {
		    list.add((Project)o);
		}
		return list;	
	}

	@Override
	public boolean isNameAlreadyExists(String name) {
		Long count = (Long) em.createQuery("select count(u.name) from project u where name = :name")
                .setParameter("name", name)
                .getSingleResult();
        if (count.compareTo(0l) > 0) {
            return true;
        }
        return false;
	}
}