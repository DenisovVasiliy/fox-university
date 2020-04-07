package com.foxminded.foxuniversity.dao;

import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Teacher;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherDaoInterface extends DaoInterface<Teacher> {
    public List<Teacher> getByCourse(Course course);
}
