package com.hydrogen.jpa;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.hydrogen.model.step.Ingestion;

public class DBUtil {
	
	public static Timestamp now() {

		return new Timestamp(System.currentTimeMillis());
	}

	private static EntityManagerFactory factory = null;

	public static EntityManager getManager() {
		factory = Persistence.createEntityManagerFactory("H2O");
		EntityManager em = factory.createEntityManager();
		em.setProperty("serverTimezone", "UTC");
		em.setProperty("useLegacyDatetimeCode", "false");
		return em;
	}

	public static void persist(Object o) {
		getManager().persist(o);
	}

	public static Object merge(Object o) {
		return getManager().merge(o);
	}

	public static Object find(Class cls, Object key) {
		return getManager().find(cls, key);
	}

	public static void main(String[] args) {

		EntityManager em = getManager();

	}

	public static <T> List<T> findAll(Class<T> class1, Map<String, Object> where) {
		EntityManager em = getManager();
		CriteriaBuilder qb = em.getCriteriaBuilder();
		CriteriaQuery<T> query = qb.createQuery(class1);
		Root<T> rt = query.from(class1);
		for (String column : where.keySet()) {
			query.where(qb.equal(rt.get(column), where.get(column)));
		}
		List<T> result = em.createQuery(query).getResultList();
		return result;
	}

}
