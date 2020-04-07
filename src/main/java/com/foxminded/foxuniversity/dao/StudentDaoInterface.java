package com.foxminded.foxuniversity.dao;

import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.domain.Student;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentDaoInterface extends DaoInterface<Student> {
    public List<Student> getByGroup(Group group);

    public boolean assignToGroup(Student student, Group group);

    public boolean updateAssignment(Student student);

    public boolean deleteAssignment(Student student);
}
