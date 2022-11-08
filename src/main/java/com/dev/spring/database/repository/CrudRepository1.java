package com.dev.spring.database.repository;

import java.util.Optional;

public interface CrudRepository1<K, E> {

    Optional<E> findByID(K id);

    void delete(E entity);
}
