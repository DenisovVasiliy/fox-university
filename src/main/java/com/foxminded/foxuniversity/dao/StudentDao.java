package com.foxminded.foxuniversity.dao;

import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.domain.Student;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentDao extends GenericDao<Student> {
    List<Student> getByGroup(Group group);

    boolean assignToGroup(Student student, Group group);

    boolean updateAssignment(Student student);

    boolean deleteAssignment(Student student);
}
