package com.foxminded.foxuniversity.service.implementation;

import com.foxminded.foxuniversity.dao.TeacherDao;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Lesson;
import com.foxminded.foxuniversity.domain.Teacher;
import com.foxminded.foxuniversity.service.CourseService;
import com.foxminded.foxuniversity.service.LessonService;
import com.foxminded.foxuniversity.service.TeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherDao teacherDao;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private CourseService courseService;

    @Override
    public List<Teacher> getAll() {
        log.debug("TeacherService calls teacherDao.getAll().");
        List<Teacher> teachers = teacherDao.getAll();
        setCourse(teachers);
        return teachers;
    }

    @Override
    public Teacher getById(int id) {
        log.debug("TeacherService calls teacherDao.getById({}).", id);
        Teacher teacher = teacherDao.getById(id);
        setCourse(teacher);
        return teacher;
    }

    @Override
    public void save(Teacher teacher) {
        log.debug("TeacherService calls teacherDao.save({}).", teacher);
        teacherDao.save(teacher);
    }

    @Override
    public boolean update(Teacher teacher) {
        log.debug("TeacherService calls teacherDao.update({}).", teacher);
        return teacherDao.update(teacher);
    }

    @Override
    public boolean updateWithCourse(Teacher teacher) {
        log.debug("TeacherService calls teacherDao.updateWithCourse({}).", teacher);
        return teacherDao.updateWithCourse(teacher);
    }

    @Override
    public boolean delete(Teacher teacher) {
        log.debug("Checking timetable of the {} before deleting.", teacher);
        if (getTimetable(teacher).isEmpty()) {
            log.debug("Teacher's timetable is empty. Call teacherDao.delete({}).", teacher);
            return teacherDao.delete(teacher);
        }
        log.warn("Deletion of the {} was cancelled.", teacher);
        return false;
    }

    @Override
    public List<Teacher> getByCourse(Course course) {
        log.debug("TeacherService calls teacherDao.getByCourse({}).", course);
        List<Teacher> teachers = teacherDao.getByCourse(course);
        setCourse(teachers, course);
        return teachers;
    }

    @Override
    public Teacher getByLesson(Lesson lesson) {
        log.debug("TeacherService calls teacherDao.getByLesson({}).", lesson);
        Teacher teacher = teacherDao.getById(lesson.getTeacher().getId());
        setCourse(teacher, lesson.getCourse());
        return teacher;
    }

    @Override
    public List<Lesson> getTimetable(Teacher teacher) {
        log.debug("Call lessonService.getByTeacher({}).", teacher);
        return lessonService.getByTeacher(teacher);
    }

    private void setCourse(Teacher teacher, Course course) {
        log.debug("Set passed {} to the {}.", course, teacher);
        teacher.setCourse(course);
    }

    private void setCourse(List<Teacher> teachers, Course course) {
        for (Teacher teacher : teachers) {
            setCourse(teacher, course);
        }
    }

    private void setCourse(Teacher teacher) {
        log.debug("Call courseService.getById({}).", teacher.getCourse().getId());
        Course course = courseService.getById(teacher.getCourse().getId());
        log.debug("Set {} to the {}.", course, teacher);
        teacher.setCourse(course);
    }

    private void setCourse(List<Teacher> teachers) {
        for (Teacher teacher : teachers) {
            setCourse(teacher);
        }
    }
}
