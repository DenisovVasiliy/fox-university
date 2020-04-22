package com.foxminded.foxuniversity.service.implementation;

import com.foxminded.foxuniversity.dao.StudentDao;
import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.domain.Lesson;
import com.foxminded.foxuniversity.domain.Student;
import com.foxminded.foxuniversity.service.GroupService;
import com.foxminded.foxuniversity.service.LessonService;
import com.foxminded.foxuniversity.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentDao studentDao;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private GroupService groupService;

    @Override
    public List<Student> getAll() {
        List<Student> students = studentDao.getAll();
        setGroup(students);
        return students;
    }

    @Override
    public Student getById(int id) {
        Student student = studentDao.getById(id);
        setGroup(student);
        return student;
    }

    @Override
    public boolean save(Student student) {
        return studentDao.save(student);
    }

    @Override
    public boolean update(Student student) {
        return studentDao.update(student);
    }

    @Override
    public boolean delete(Student student) {
        return studentDao.delete(student);
    }

    @Override
    public List<Student> getByGroup(Group group) {
        List<Student> students = studentDao.getByGroup(group);
        setGroup(students, group);
        return students;
    }

    @Override
    public boolean assignToGroup(Student student, Group group) {
        if (group == null) return false;
        if (student.getGroup() == null) {
            if (studentDao.assignToGroup(student, group)) {
                student.setGroup(group);
                return true;
            }
            return false;
        } else if (student.getGroup() != group) {
            Group oldGroup = student.getGroup();
            student.setGroup(group);
            if (!updateAssignment(student)) {
                student.setGroup(oldGroup);
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean updateAssignment(Student student) {
        return studentDao.updateAssignment(student);
    }

    @Override
    public boolean deleteAssignment(Student student) {
        if (studentDao.deleteAssignment(student)) {
            student.setGroup(null);
            return true;
        }
        return false;
    }

    @Override
    public List<Lesson> getTimetable(Student student) {
        return lessonService.getByStudent(student);
    }

    private void setGroup(Student student) {
        student.setGroup(groupService.getById(student.getGroup().getId()));
    }

    private void setGroup(List<Student> students) {
        for (Student student : students) {
            setGroup(student);
        }
    }

    private void setGroup(Student student, Group group) {
        student.setGroup(group);
    }

    private void setGroup(List<Student> students, Group group) {
        for (Student student : students) {
            setGroup(student, group);
        }
    }
}
