package com.foxminded.foxuniversity.dao;

import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.domain.Lesson;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupDao extends GenericDao<Group> {
    public boolean assignToCourses(Group group, List<Course> courses);

    public boolean deleteFromCourse(Group group, Course course);

    public boolean deleteFromCourse(Group group, List<Course> courses);

    public List<Group> getByLesson(Lesson lesson);
}
