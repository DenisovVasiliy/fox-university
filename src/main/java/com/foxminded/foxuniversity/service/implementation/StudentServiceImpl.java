package com.foxminded.foxuniversity.service.implementation;

import com.foxminded.foxuniversity.dao.StudentDao;
import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.domain.Lesson;
import com.foxminded.foxuniversity.domain.Student;
import com.foxminded.foxuniversity.service.GroupService;
import com.foxminded.foxuniversity.service.LessonService;
import com.foxminded.foxuniversity.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentDao studentDao;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private GroupService groupService;

    @Override
    public List<Student> getAll() {
        log.debug("StudentService calls studentDao.getAll().");
        List<Student> students = studentDao.getAll();
        setGroup(students);
        return students;
    }

    @Override
    public Student getById(int id) {
        log.debug("StudentService calls studentDao.getById({}).", id);
        Student student = studentDao.getById(id);
        setGroup(student);
        return student;
    }

    @Override
    public void save(Student student) {
        log.debug("StudentService calls studentDao.save({}).", student);
        studentDao.save(student);
    }

    @Override
    public boolean update(Student student) {
        log.debug("StudentService calls studentDao.update({}).", student);
        return studentDao.update(student);
    }

    @Override
    public boolean delete(Student student) {
        log.debug("StudentService calls studentDao.delete({}).", student);
        return studentDao.delete(student);
    }

    @Override
    public List<Student> getByGroup(Group group) {
        log.debug("StudentService calls studentDao.getByGroup({}).", group);
        List<Student> students = studentDao.getByGroup(group);
        setGroup(students, group);
        return students;
    }

    @Override
    public boolean assignToGroup(Student student, Group group) {
        if (group == null) {
            log.warn("StudentService.assignToGroup was invoked with group = null.");
            return false;
        }
        if (student.getGroup() == null) {
            log.debug("StudentService calls studentDao.assignToGroup({}, {}).", student, group);
            if (studentDao.assignToGroup(student, group)) {
                log.debug("Assignment student to group was successful. Set group to student.");
                student.setGroup(group);
                return true;
            }
            log.warn("Assignment {} to {} was cancelled in DAO-layer.", student, group);
            return false;
        } else if (student.getGroup() != group) {
            log.debug("Backup old {} of {}.", student.getGroup(), student);
            Group oldGroup = student.getGroup();
            log.debug("Set new {} to the {}.", group, student);
            student.setGroup(group);
            if (!updateAssignment(student)) {
                log.warn("Update group of {} was cancelled in DAO-layer.", student);
                log.warn("Set backed up {} to the {}.", oldGroup, student);
                student.setGroup(oldGroup);
                return false;
            }
            return true;
        }
        log.warn("Update {} group was cancelled, " +
                "because passed {} is the same as current student's group.", student, group);
        return false;
    }

    @Override
    public boolean updateAssignment(Student student) {
        if (student.getGroup() != null) {
            log.debug("StudentService calls studentDao.updateAssignment({}).", student);
            return studentDao.updateAssignment(student);
        }
        log.debug("Updating cancelled: student's group is 'null'.");
        return false;
    }

    @Override
    public boolean deleteAssignment(Student student) {
        log.debug("StudentService calls studentDao.deleteAssignment({}).", student);
        if (studentDao.deleteAssignment(student)) {
            log.debug("Assignment's deletion was successful. Set null to student's group.");
            student.setGroup(null);
            return true;
        }
        log.warn("Assignment's deletion of {} was cancelled in DAO-layer.", student);
        return false;
    }

    @Override
    public List<Lesson> getTimetable(Student student) {
        log.debug("Call lessonService.getByStudent({}).", student);
        return lessonService.getByStudent(student);
    }

    private void setGroup(Student student) {
        if (student.getGroup() != null) {
            log.debug("Call groupService.getById({}) and set result to the {}.", student.getGroup().getId(), student);
            student.setGroup(groupService.getById(student.getGroup().getId()));
        }
    }

    private void setGroup(List<Student> students) {
        for (Student student : students) {
            setGroup(student);
        }
    }

    private void setGroup(Student student, Group group) {
        log.debug("Set passed {} to the {}", group, student);
        student.setGroup(group);
    }

    private void setGroup(List<Student> students, Group group) {
        for (Student student : students) {
            setGroup(student, group);
        }
    }
}
