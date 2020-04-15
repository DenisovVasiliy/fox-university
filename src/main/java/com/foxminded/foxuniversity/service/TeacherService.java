package com.foxminded.foxuniversity.service;

import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Lesson;
import com.foxminded.foxuniversity.domain.Teacher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TeacherService extends GenericService<Teacher> {
    public List<Teacher> getByCourse(Course course);

    public List<Lesson> getTimetable(Teacher teacher);
}
