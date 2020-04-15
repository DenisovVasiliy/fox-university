package com.foxminded.foxuniversity.service;

import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.domain.Lesson;
import com.foxminded.foxuniversity.domain.Student;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface StudentService extends GenericService<Student> {
    public List<Student> getByGroup(Group group);

    public boolean assignToGroup(Student student, Group group);

    public boolean updateAssignment(Student student);

    public boolean deleteAssignment(Student student);

    public List<Lesson> getTimetable(Student student);
}
