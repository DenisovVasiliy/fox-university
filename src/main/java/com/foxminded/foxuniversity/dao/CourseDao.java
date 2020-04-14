package com.foxminded.foxuniversity.dao;

import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Group;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseDao extends GenericDao<Course> {
    public List<Course> getByGroup(Group group);
}
