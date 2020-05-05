package com.foxminded.foxuniversity.service.implementation;

import com.foxminded.foxuniversity.dao.GroupDao;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.domain.Lesson;
import com.foxminded.foxuniversity.service.GroupService;
import com.foxminded.foxuniversity.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GroupServiceImpl implements GroupService {
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private StudentService studentService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<Group> getAll() {
        if (logger.isDebugEnabled()) {
            logger.debug("GroupService calls groupDao.getAll().");
        }
        List<Group> groups = groupDao.getAll();
        fillStudents(groups);
        return groups;
    }

    @Override
    public Group getById(int id) {
        if (logger.isDebugEnabled()) {
            logger.debug("GroupService calls groupDao.getById(" + id + ").");
        }
        Group group = groupDao.getById(id);
        fillStudents(group);
        return group;
    }

    @Override
    public List<Group> getByLesson(Lesson lesson) {
        if (logger.isDebugEnabled()) {
            logger.debug("GroupService calls groupDao.getByLesson(Lesson{id = " + lesson.getId() + "}).");
        }
        List<Group> groups = groupDao.getByLesson(lesson);
        fillStudents(groups);
        return groups;
    }

    @Override
    public List<Group> getByCourse(Course course) {
        if (logger.isDebugEnabled()) {
            logger.debug("GroupService calls groupDao.getByCourse(Course{id = " + course.getId() + "}).");
        }
        List<Group> groups = groupDao.getByCourse(course);
        fillStudents(groups);
        return groups;
    }

    @Override
    public void save(Group group) {
        if (logger.isDebugEnabled()) {
            logger.debug("GroupService calls groupDao.save(" + group + ").");
        }
        groupDao.save(group);
    }

    @Override
    public boolean update(Group group) {
        if (logger.isDebugEnabled()) {
            logger.debug("GroupService calls groupDao.update(" + group + ").");
        }
        return groupDao.update(group);
    }

    @Override
    public boolean delete(Group group) {
        if (logger.isDebugEnabled()) {
            logger.debug("GroupService calls groupDao.delete(" + group + ").");
        }
        return groupDao.delete(group);
    }

    @Override
    public boolean assignToCourses(Group group, List<Course> courses) {
        if (logger.isDebugEnabled()) {
            logger.debug("GroupService calls groupDao.assignToCourses(" + group + ", List<Course>[" + courses.size() + "])");
        }
        if (groupDao.assignToCourses(group, courses)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Assignment was successful. Set group to each course in the list.");
            }
            for (Course course : courses) {
                course.getGroups().add(group);
            }
            return true;
        }
        logger.warn("Assignment " + group + " to courses was cancelled.");
        return false;
    }

    @Override
    public boolean deleteFromCourse(Group group, Course course) {
        if (course.getGroups().contains(group)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Call groupDao.deleteFromCourse(" + group + ", Course{id = " + course.getId() + "}).");
            }
            if (groupDao.deleteFromCourse(group, course)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Deletion was successful. Remove group from course.");
                }
                course.getGroups().remove(group);
                return true;
            }
            logger.warn("Deletion " + group + " from Course{id = " + course.getId() + " was cancelled in DAO-layer.");
            return false;
        }
        logger.warn("Deletion was cancelled: " + group + " isn't assigned to the Course{id = " + course.getId() + "}.");
        return false;
    }

    @Override
    public boolean deleteFromCourse(Group group, List<Course> courses) {
        for (Course course : courses) {
            if (!course.getGroups().contains(group)) {
                if (logger.isDebugEnabled()) {
                    logger.warn("Deletion was cancelled: " + group +
                            " isn't assigned to the Course{id = " + course.getId() + "}.");
                }
                return false;
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Call groupDao.deleteFromCourse(" + group + ", Courses[" + courses.size() + "]).");
        }
        if (groupDao.deleteFromCourse(group, courses)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Deletion was successful. Remove group from courses.");
            }
            for (Course course : courses) {
                course.getGroups().remove(group);
            }
            return true;
        }
        logger.warn("Deletion " + group + " from courses was cancelled in DAO-layer.");
        return false;
    }

    private void fillStudents(Group group) {
        if (logger.isDebugEnabled()) {
            logger.debug("Set students to group: call studentService.getByGroup(" + group + ")");
        }
        group.setStudents(studentService.getByGroup(group));
    }

    private void fillStudents(List<Group> groups) {
        for (Group group : groups) {
            fillStudents(group);
        }
    }
}
