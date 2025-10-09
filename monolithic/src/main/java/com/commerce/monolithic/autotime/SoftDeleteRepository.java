package com.commerce.monolithic.autotime;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface SoftDeleteRepository<T extends BaseTimeEntity, ID> extends JpaRepository<T, ID> {

	Optional<T> findByIdIncludingDeleted(ID id);

	List<T> findAllIncludingDeleted();

	void softDeleteById(ID id, LocalDateTime when);

	void softDelete(T entity, LocalDateTime when);

	default void deleteById(ID id) {
		softDeleteById(id, LocalDateTime.now());
	}

	default void delete(T entity) {
		softDelete(entity, LocalDateTime.now());
	}

	default void deleteAll(Iterable<? extends T> entities) {
		var now = LocalDateTime.now();
		for (T e : entities)
			softDelete(e, now);
	}
}
