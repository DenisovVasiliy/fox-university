package com.foxminded.foxuniversity.service;

import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.domain.Lesson;

import java.util.List;

public interface GroupService extends GenericService<Group> {
    public boolean assignToCourses(Group group, List<Course> courses);

    public boolean deleteFromCourse(Group group, Course course);

    public boolean deleteFromCourse(Group group, List<Course> courses);

    public List<Group> getByLesson(Lesson lesson);

    public void fillCourses(Group group);

    public void fillStudents(Group group);
}
