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
    public List<Lesson> getByCourse(Course course);

    public List<Lesson> getByStudent(Student student);

    public List<Lesson> getByTeacher(Teacher teacher);

    public boolean assignGroups(Lesson lesson, List<Group> groups);

    public boolean deleteGroup(Lesson lesson, Group group);

    public void fillGroups(Lesson lesson);
}
