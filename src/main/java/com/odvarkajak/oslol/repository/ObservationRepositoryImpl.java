package com.odvarkajak.oslol.repository;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.odvarkajak.oslol.domain.Observation;

@Repository
public class ObservationRepositoryImpl implements ObservationRepository{

	@PersistenceContext
    private EntityManager em;

    @SuppressWarnings("rawtypes")
	@Override
    public Collection loadObservations() {
        Query query = em.createQuery("from observation");
        return query.getResultList();
    }

	@Override
	public void saveObservation(Observation observation) {
		em.merge(observation);
		
		
	}

	@Override
	@Transactional(readOnly = true)
	public Observation findObservationById(Long observationId) {		
		return (Observation) em.createQuery("from observation o where observationId = :observationId")
                .setParameter("observationId", observationId).getSingleResult();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Observation> findObservationsByname(String name) {
		Query query = em.createQuery("select o from observation o where name like :name")
                .setParameter("name",  "%" + name + "%");
		final List<Observation> list = new LinkedList<>();
		for(final Object o : query.getResultList()) {
		    list.add((Observation)o);
		}
		return list;
	}

	@Override
	public Observation update(Observation observation) {
		return em.merge(observation);
		
		
	}

	@Override
	public void delete(Observation observation) {
		em.remove(observation);
		
		
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean isNameAlreadyExistsForThisUser(String name, Long userId) {
		Query query = em.createQuery("select count(a.name) from observation a join a.author t where t.id =:id and a.name = :name")
                .setParameter("name", name)
                .setParameter("id", userId);
		try{
			Long count = (Long) query.getSingleResult();
			if (count != null)
				if (count != 0)
					return true;
		}
		catch (NoResultException nre){
			return false;
		}
		return false;

	}

}
