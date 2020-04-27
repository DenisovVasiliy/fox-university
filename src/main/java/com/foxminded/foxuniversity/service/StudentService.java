package com.foxminded.foxuniversity.service;

import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.domain.Lesson;
import com.foxminded.foxuniversity.domain.Student;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface StudentService extends GenericService<Student> {
    List<Student> getByGroup(Group group);

    boolean assignToGroup(Student student, Group group);

    boolean updateAssignment(Student student);

    boolean deleteAssignment(Student student);

    List<Lesson> getTimetable(Student student);
}
