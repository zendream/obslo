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

import com.odvarkajak.oslol.domain.Observation;

@Repository
public class ObservationRepositoryImpl implements ObservationRepository{

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
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
	public Observation findObservationById(Long observationId) {		
		return (Observation) em.createQuery("select o from observation o where observationId = :observationId")
                .setParameter("observationId", observationId).getSingleResult();
	}

	@Override
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
	public void update(Observation observation) {
		em.merge(observation);
		
	}

	@Override
	public void delete(Observation observation) {
		em.remove(observation);
		
	}

}
