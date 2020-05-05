package com.foxminded.foxuniversity.service.implementation;

import com.foxminded.foxuniversity.dao.StudentDao;
import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.domain.Lesson;
import com.foxminded.foxuniversity.domain.Student;
import com.foxminded.foxuniversity.service.GroupService;
import com.foxminded.foxuniversity.service.LessonService;
import com.foxminded.foxuniversity.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<Student> getAll() {
        if (logger.isDebugEnabled()) {
            logger.debug("StudentService calls studentDao.getAll().");
        }
        List<Student> students = studentDao.getAll();
        setGroup(students);
        return students;
    }

    @Override
    public Student getById(int id) {
        if (logger.isDebugEnabled()) {
            logger.debug("StudentService calls studentDao.getById(" + id + ").");
        }
        Student student = studentDao.getById(id);
        setGroup(student);
        return student;
    }

    @Override
    public void save(Student student) {
        if (logger.isDebugEnabled()) {
            logger.debug("StudentService calls studentDao.save(Student{id = " + student.getId() + "}).");
        }
        studentDao.save(student);
    }

    @Override
    public boolean update(Student student) {
        if (logger.isDebugEnabled()) {
            logger.debug("StudentService calls studentDao.update(Student{id = " + student.getId() + "}).");
        }
        return studentDao.update(student);
    }

    @Override
    public boolean delete(Student student) {
        if (logger.isDebugEnabled()) {
            logger.debug("StudentService calls studentDao.delete(Student{id = " + student.getId() + "}).");
        }
        return studentDao.delete(student);
    }

    @Override
    public List<Student> getByGroup(Group group) {
        if (logger.isDebugEnabled()) {
            logger.debug("StudentService calls studentDao.getByGroup(" + group + ").");
        }
        List<Student> students = studentDao.getByGroup(group);
        setGroup(students, group);
        return students;
    }

    @Override
    public boolean assignToGroup(Student student, Group group) {
        if (group == null) {
            logger.warn("StudentService.assignToGroup was invoked with group = null.");
            return false;
        }
        if (student.getGroup() == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("StudentService calls studentDao.assignToGroup(" +
                        "Student{id = " + student.getId() + ", " + group + ").");
            }
            if (studentDao.assignToGroup(student, group)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Assignment student to group was successful. Set group to student.");
                }
                student.setGroup(group);
                return true;
            }
            logger.warn("Assignment Student{id = " + student.getId() + "} to " + group +
                    " was cancelled in DAO-layer.");
            return false;
        } else if (student.getGroup() != group) {
            if (logger.isDebugEnabled()) {
                logger.debug("Backup old " + group + " of Student{id = " + student.getId() + "}.");
                logger.debug("Set new " + group + " to the " + "Student{id = " + student.getId() + "}.");
            }
            Group oldGroup = student.getGroup();
            student.setGroup(group);
            if (!updateAssignment(student)) {
                logger.warn("Update Student's{id = " + student.getId() + "} group was cancelled in DAO-layer.");
                logger.warn("Set backed up " + group + " to the student.");
                student.setGroup(oldGroup);
                return false;
            }
            return true;
        }
        logger.warn("Update Student's{id = " + student.getId() + "} group was cancelled, because passed " +
                group + " is the same as current student's group.");
        return false;
    }

    @Override
    public boolean updateAssignment(Student student) {
        if (logger.isDebugEnabled()) {
            logger.debug("StudentService calls studentDao.updateAssignment(" + student + ").");
        }
        return studentDao.updateAssignment(student);
    }

    @Override
    public boolean deleteAssignment(Student student) {
        if (logger.isDebugEnabled()) {
            logger.debug("StudentService calls studentDao.deleteAssignment(" + student + ").");
        }
        if (studentDao.deleteAssignment(student)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Assignment's deletion was successful. Set null to student's group.");
            }
            student.setGroup(null);
            return true;
        }
        logger.warn("Assignment's deletion of " + student + " was cancelled in DAO-layer");
        return false;
    }

    @Override
    public List<Lesson> getTimetable(Student student) {
        if (logger.isDebugEnabled()) {
            logger.debug("Call lessonService.getByStudent(" + student + ").");
        }
        return lessonService.getByStudent(student);
    }

    private void setGroup(Student student) {
        if (student.getGroup() != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Call groupService.getById(" + student.getGroup().getId() + ") " +
                        "and set result to the " + student);
            }
            student.setGroup(groupService.getById(student.getGroup().getId()));
        }
    }

    private void setGroup(List<Student> students) {
        for (Student student : students) {
            setGroup(student);
        }
    }

    private void setGroup(Student student, Group group) {
        if (logger.isDebugEnabled()) {
            logger.debug("Set passed " + group + " to the " + student);
        }
        student.setGroup(group);
    }

    private void setGroup(List<Student> students, Group group) {
        for (Student student : students) {
            setGroup(student, group);
        }
    }
}
