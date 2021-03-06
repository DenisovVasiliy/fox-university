package com.foxminded.foxuniversity.service;

import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.domain.Lesson;

import java.util.List;

public interface GroupService extends GenericService<Group> {
    boolean assignToCourses(Group group, List<Course> courses);

    boolean deleteFromCourse(Group group, Course course);

    List<Group> getByLesson(Lesson lesson);

    List<Group> getByCourse(Course course);
}
