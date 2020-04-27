package com.foxminded.foxuniversity.service.implementation;

import com.foxminded.foxuniversity.dao.CourseDao;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.service.CourseService;
import com.foxminded.foxuniversity.service.GroupService;
import com.foxminded.foxuniversity.service.LessonService;
import com.foxminded.foxuniversity.service.TeacherService;
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

    @Override
    public List<Course> getAll() {
        List<Course> courses = courseDao.getAll();
        fillLessons(courses);
        fillGroups(courses);
        return courses;
    }

    @Override
    public Course getById(int id) {
        Course course = courseDao.getById(id);
        fillLessons(course);
        fillGroups(course);
        return course;
    }

    @Override
    public List<Course> getByGroup(Group group) {
        List<Course> courses = courseDao.getByGroup(group);
        fillLessons(courses);
        fillGroups(courses);
        return courses;
    }

    @Override
    public void save(Course course) {
        courseDao.save(course);
    }

    @Override
    public boolean update(Course course) {
        return courseDao.update(course);
    }

    @Override
    public boolean delete(Course course) {
        if (teacherService.getByCourse(course).isEmpty()) {
            return courseDao.delete(course);
        }
        return false;
    }

    private void fillLessons(Course course) {
        course.setLessons(lessonService.getByCourse(course));
    }

    private void fillLessons(List<Course> courses) {
        for (Course course : courses) {
            fillLessons(course);
        }
    }

    private void fillGroups(Course course) {
        course.setGroups(groupService.getByCourse(course));
    }

    private void fillGroups(List<Course> courses) {
        for (Course course : courses) {
            fillGroups(course);
        }
    }
}
