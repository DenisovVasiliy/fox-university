package com.foxminded.foxuniversity.dao;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenericDao<T> {
    List<T> getAll();

    T getById(int id);

    boolean save(T object);

    boolean update(T object);

    boolean delete(T object);
}
