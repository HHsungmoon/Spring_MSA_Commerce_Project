package com.commerce.monolithic.autotime;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Id;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

public class SoftDeleteRepositoryImpl<T extends BaseTimeEntity, ID>
	extends SimpleJpaRepository<T, ID>
	implements SoftDeleteRepository<T, ID> {

	private final EntityManager em;
	private final JpaEntityInformation<T, ID> info;
	private final String idField;
	private final String entityName;

	public SoftDeleteRepositoryImpl(JpaEntityInformation<T, ID> ei, EntityManager em) {
		super(ei, em);
		this.em = em;
		this.info = ei;
		this.entityName = ei.getEntityName();
		this.idField = resolveIdFieldName(ei.getJavaType());
	}

	private String resolveIdFieldName(Class<?> type) {
		for (Class<?> c = type; c != null; c = c.getSuperclass()) {
			for (Field f : c.getDeclaredFields())
				if (f.isAnnotationPresent(Id.class))
					return f.getName();
		}
		throw new IllegalStateException("ID field not found: " + type);
	}

	@Override
	public Optional<T> findByIdIncludingDeleted(ID id) {
		String jpql = "select e from " + entityName + " e where e." + idField + " = :id";
		TypedQuery<T> q = em.createQuery(jpql, info.getJavaType());
		q.setParameter("id", id);
		return q.getResultStream().findFirst();
	}

	@Override
	public List<T> findAllIncludingDeleted() {
		return em.createQuery("select e from " + entityName + " e", info.getJavaType()).getResultList();
	}

	@Override
	@Transactional
	public void softDeleteById(ID id, LocalDateTime when) {
		findByIdIncludingDeleted(id).ifPresent(e -> softDelete(e, when));
	}

	@Override
	@Transactional
	public void softDelete(T entity, LocalDateTime when) {
		entity.setDeletedAt(when);
		save(entity); // 변경감지로 UPDATE
	}
}
