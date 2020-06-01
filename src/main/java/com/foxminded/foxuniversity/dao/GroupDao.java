package com.foxminded.foxuniversity.dao;

import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.domain.Lesson;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupDao extends GenericDao<Group> {
    boolean assignToCourses(Group group, List<Course> courses);

    void deleteFromCourse(Group group, Course course);

    List<Group> getByLesson(Lesson lesson);

    List<Group> getByCourse(Course course);
}
