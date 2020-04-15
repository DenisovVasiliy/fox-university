package com.foxminded.foxuniversity.service;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface GenericService<T> {
    public List<T> getAll();

    public T getById(int id);

    public boolean save(T object);

    public boolean update(T object);

    public boolean delete(T object);
}
