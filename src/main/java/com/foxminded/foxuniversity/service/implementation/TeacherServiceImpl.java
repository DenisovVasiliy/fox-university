package com.foxminded.foxuniversity.service.implementation;

import com.foxminded.foxuniversity.dao.TeacherDao;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Lesson;
import com.foxminded.foxuniversity.domain.Teacher;
import com.foxminded.foxuniversity.service.CourseService;
import com.foxminded.foxuniversity.service.LessonService;
import com.foxminded.foxuniversity.service.TeacherService;
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

    @Override
    public List<Teacher> getAll() {
        List<Teacher> teachers = teacherDao.getAll();
        setCourse(teachers);
        return teachers;
    }

    @Override
    public Teacher getById(int id) {
        Teacher teacher = teacherDao.getById(id);
        setCourse(teacher);
        return teacher;
    }

    @Override
    public void save(Teacher teacher) {
        teacherDao.save(teacher);
    }

    @Override
    public boolean update(Teacher teacher) {
        return teacherDao.update(teacher);
    }

    @Override
    public boolean delete(Teacher teacher) {
        if (getTimetable(teacher).isEmpty()) {
            return teacherDao.delete(teacher);
        }
        return false;
    }

    @Override
    public List<Teacher> getByCourse(Course course) {
        List<Teacher> teachers = teacherDao.getByCourse(course);
        setCourse(teachers, course);
        return teachers;
    }

    @Override
    public Teacher getByLesson(Lesson lesson) {
        Teacher teacher = teacherDao.getById(lesson.getTeacher().getId());
        setCourse(teacher, lesson.getCourse());
        return teacher;
    }

    @Override
    public List<Lesson> getTimetable(Teacher teacher) {
        return lessonService.getByTeacher(teacher);
    }

    private void setCourse(Teacher teacher, Course course) {
        teacher.setCourse(course);
    }

    private void setCourse(List<Teacher> teachers, Course course) {
        for (Teacher teacher : teachers) {
            setCourse(teacher, course);
        }
    }

    private void setCourse(Teacher teacher) {
        Course course = courseService.getById(teacher.getCourse().getId());
        teacher.setCourse(course);
    }

    private void setCourse(List<Teacher> teachers) {
        for (Teacher teacher : teachers) {
            Course course = courseService.getById(teacher.getCourse().getId());
            setCourse(teacher, course);
        }
    }
}
