package com.foxminded.foxuniversity.service;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface GenericService<T> {
    List<T> getAll();

    T getById(int id);

    void save(T object);

    boolean update(T object);

    boolean delete(T object);
}
