package com.foxminded.foxuniversity.dao;

import com.foxminded.foxuniversity.domain.Student;
import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Teacher;
import com.foxminded.foxuniversity.domain.Lesson;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonDaoInterface extends DaoInterface<Lesson> {
    public List<Lesson> getByCourse(Course course);

    public List<Lesson> getByStudent(Student student);

    public List<Lesson> getByTeacher(Teacher teacher);

    public boolean assignGroups(Lesson lesson, List<Group> groups);

    public boolean deleteGroup(Lesson lesson, Group group);
}
