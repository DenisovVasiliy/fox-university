package com.foxminded.foxuniversity.service.implementation;

import com.foxminded.foxuniversity.dao.TeacherDao;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Lesson;
import com.foxminded.foxuniversity.domain.Teacher;
import com.foxminded.foxuniversity.service.CourseService;
import com.foxminded.foxuniversity.service.LessonService;
import com.foxminded.foxuniversity.service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherDao teacherDao;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private CourseService courseService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<Teacher> getAll() {
        if (logger.isDebugEnabled()) {
            logger.debug("TeacherService calls teacherDao.getAll().");
        }
        List<Teacher> teachers = teacherDao.getAll();
        setCourse(teachers);
        return teachers;
    }

    @Override
    public Teacher getById(int id) {
        if (logger.isDebugEnabled()) {
            logger.debug("TeacherService calls teacherDao.getById(" + id + ").");
        }
        Teacher teacher = teacherDao.getById(id);
        setCourse(teacher);
        return teacher;
    }

    @Override
    public void save(Teacher teacher) {
        if (logger.isDebugEnabled()) {
            logger.debug("TeacherService calls teacherDao.save(" + teacher + ").");
        }
        teacherDao.save(teacher);
    }

    @Override
    public boolean update(Teacher teacher) {
        if (logger.isDebugEnabled()) {
            logger.debug("TeacherService calls teacherDao.update(" + teacher + ").");
        }
        return teacherDao.update(teacher);
    }

    @Override
    public boolean delete(Teacher teacher) {
        if (logger.isDebugEnabled()) {
            logger.debug("Checking timetable of the Teacher{id = " + teacher.getId() + "} before deletion.");
        }
        if (getTimetable(teacher).isEmpty()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Teacher's timetable is empty. Call teacherDao.delete({id = " + teacher.getId() + "}).");
            }
            return teacherDao.delete(teacher);
        }
        logger.warn("Deletion of the Teacher{id = " + teacher.getId() + "} was cancelled.");
        return false;
    }

    @Override
    public List<Teacher> getByCourse(Course course) {
        if (logger.isDebugEnabled()) {
            logger.debug("TeacherService calls teacherDao.getByCourse(Course{id = " + course.getId() + "}).");
        }
        List<Teacher> teachers = teacherDao.getByCourse(course);
        setCourse(teachers, course);
        return teachers;
    }

    @Override
    public Teacher getByLesson(Lesson lesson) {
        if (logger.isDebugEnabled()) {
            logger.debug("TeacherService calls teacherDao.getByCourse(Lesson{id = " + lesson.getId() + "}).");
        }
        Teacher teacher = teacherDao.getById(lesson.getTeacher().getId());
        setCourse(teacher, lesson.getCourse());
        return teacher;
    }

    @Override
    public List<Lesson> getTimetable(Teacher teacher) {
        if (logger.isDebugEnabled()) {
            logger.debug("Call lessonService.getByTeacher(Teacher{id = " + teacher.getId() + "}).");
        }
        return lessonService.getByTeacher(teacher);
    }

    private void setCourse(Teacher teacher, Course course) {
        if (logger.isDebugEnabled()) {
            logger.debug("Set passed Course{id = " + course.getId() + " to the Teacher{id = " + teacher.getId() + "}.");
        }
        teacher.setCourse(course);
    }

    private void setCourse(List<Teacher> teachers, Course course) {
        for (Teacher teacher : teachers) {
            setCourse(teacher, course);
        }
    }

    private void setCourse(Teacher teacher) {
        if (logger.isDebugEnabled()) {
            logger.debug("Call courseService.getById(" + teacher.getCourse().getId() + ") " +
                    "and set result to the Teacher{id = " + teacher.getId() + "}.");
        }
        Course course = courseService.getById(teacher.getCourse().getId());
        teacher.setCourse(course);
    }

    private void setCourse(List<Teacher> teachers) {
        for (Teacher teacher : teachers) {
            setCourse(teacher);
        }
    }
}
