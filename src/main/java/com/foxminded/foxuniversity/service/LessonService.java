package com.foxminded.foxuniversity.service;

import com.foxminded.foxuniversity.domain.Student;
import com.foxminded.foxuniversity.domain.Lesson;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Teacher;
import com.foxminded.foxuniversity.domain.Group;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface LessonService extends GenericService<Lesson> {
    List<Lesson> getByCourse(Course course);

    List<Lesson> getByStudent(Student student);

    List<Lesson> getByTeacher(Teacher teacher);

    boolean assignGroups(Lesson lesson, List<Group> groups);

    boolean deleteGroup(Lesson lesson, Group group);

    void fillGroups(Lesson lesson);
}
