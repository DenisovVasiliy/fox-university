package com.foxminded.foxuniversity.dao;

import com.foxminded.foxuniversity.domain.Student;
import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Teacher;
import com.foxminded.foxuniversity.domain.Lesson;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonDao extends GenericDao<Lesson> {
    List<Lesson> getByCourse(Course course);

    List<Lesson> getByGroup(Group group);

    List<Lesson> getByStudent(Student student);

    List<Lesson> getByTeacher(Teacher teacher);

    boolean assignGroups(Lesson lesson, List<Group> groups);

    boolean deleteGroup(Lesson lesson, Group group);
}
