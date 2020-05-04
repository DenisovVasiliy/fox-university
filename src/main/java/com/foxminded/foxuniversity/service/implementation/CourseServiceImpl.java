package com.foxminded.foxuniversity.service.implementation;

import com.foxminded.foxuniversity.dao.CourseDao;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.service.CourseService;
import com.foxminded.foxuniversity.service.GroupService;
import com.foxminded.foxuniversity.service.LessonService;
import com.foxminded.foxuniversity.service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private GroupService groupService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<Course> getAll() {
        logger.info("CourseService calls courseDao.getAll().");
        List<Course> courses = courseDao.getAll();
        fillLessons(courses);
        fillGroups(courses);
        return courses;
    }

    @Override
    public Course getById(int id) {
        logger.info("CourseService calls courseDao.getById(" + id + ").");
        Course course = courseDao.getById(id);
        fillLessons(course);
        fillGroups(course);
        return course;
    }

    @Override
    public List<Course> getByGroup(Group group) {
        logger.info("CourseService calls courseDao.getByGroup(" + group + ").");
        List<Course> courses = courseDao.getByGroup(group);
        fillLessons(courses);
        fillGroups(courses);
        return courses;
    }

    @Override
    public void save(Course course) {
        logger.info("CourseService calls courseDao.save(Course{id = " + course.getId() + "}).");
        courseDao.save(course);
    }

    @Override
    public boolean update(Course course) {
        logger.info("CourseService calls courseDao.update(Course{id = " + course.getId() + "}).");
        return courseDao.update(course);
    }

    @Override
    public boolean delete(Course course) {
        logger.info("Checking for teachers in the Course{id = " + course.getId() + "}).");
        if (teacherService.getByCourse(course).isEmpty()) {
            logger.info("There are no teachers in the course. " +
                    "Call courseDao.delete(Course{id = " + course.getId() + "}).");
            return courseDao.delete(course);
        }
        logger.info("There are some teachers in the Course{id = " + course.getId() + "}). Deletion canceled.");
        return false;
    }

    private void fillLessons(Course course) {
        logger.info("Set lessons to course: call lessonService.getByCourse(Course{id = " + course.getId() + "}).");
        course.setLessons(lessonService.getByCourse(course));
    }

    private void fillLessons(List<Course> courses) {
        for (Course course : courses) {
            fillLessons(course);
        }
    }

    private void fillGroups(Course course) {
        logger.info("Set lessons to course: call groupService.getByCourse(Course{id = " + course.getId() + "}).");
        course.setGroups(groupService.getByCourse(course));
    }

    private void fillGroups(List<Course> courses) {
        for (Course course : courses) {
            fillGroups(course);
        }
    }
}
