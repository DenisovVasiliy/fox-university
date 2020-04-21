package com.foxminded.foxuniversity.service.implementation;

import com.foxminded.foxuniversity.dao.CourseDao;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.service.CourseService;
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

    @Override
    public List<Course> getAll() {
        return fillCoursesLessons(courseDao.getAll());
    }

    @Override
    public Course getById(int id) {
        return fillCoursesLessons(courseDao.getById(id));
    }

    @Override
    public List<Course> getByGroup(Group group) {
        return fillCoursesLessons(courseDao.getByGroup(group));
    }

    @Override
    public boolean save(Course course) {
        return courseDao.save(course);
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

    private Course fillCoursesLessons(Course course) {
        course.setLessons(lessonService.getByCourse(course));
        return course;
    }

    private List<Course> fillCoursesLessons(List<Course> courses) {
        for (Course course : courses) {
            fillCoursesLessons(course);
        }
        return courses;
    }
}
