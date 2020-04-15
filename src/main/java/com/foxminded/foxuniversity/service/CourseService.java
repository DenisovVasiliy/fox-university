package com.foxminded.foxuniversity.service;

import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Group;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CourseService extends GenericService<Course> {
    public List<Course> getByGroup(Group group);
    public void fillCoursesLessons(Course course);
}
