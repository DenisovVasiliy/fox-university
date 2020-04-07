package com.foxminded.foxuniversity.dao;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DaoInterface<T> {
    public List<T> getAll();
    public T getById(int id);
    public boolean save(T object);
    public boolean update(T object);
    public boolean delete(T object);
}
