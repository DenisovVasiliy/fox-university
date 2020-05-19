package com.foxminded.foxuniversity.service.implementation;

import com.foxminded.foxuniversity.dao.LessonDao;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.domain.Lesson;
import com.foxminded.foxuniversity.domain.Student;
import com.foxminded.foxuniversity.domain.Teacher;
import com.foxminded.foxuniversity.service.CourseService;
import com.foxminded.foxuniversity.service.GroupService;
import com.foxminded.foxuniversity.service.LessonService;
import com.foxminded.foxuniversity.service.TeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class LessonServiceImpl implements LessonService {
    @Autowired
    private LessonDao lessonDao;
    @Autowired
    private GroupService groupService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private TeacherService teacherService;

    @Override
    public List<Lesson> getAll() {
        log.debug("LessonService calls lessonDao.getAll().");
        List<Lesson> lessons = fillGroups(lessonDao.getAll());
        setCourse(lessons);
        setTeacher(lessons);
        return lessons;
    }

    @Override
    public Lesson getById(int id) {
        log.debug("LessonService calls lessonDao.getById({}).", id);
        Lesson lesson = fillGroups(lessonDao.getById(id));
        setCourse(lesson);
        setTeacher(lesson);
        return lesson;
    }

    @Override
    public List<Lesson> getByCourse(Course course) {
        log.debug("LessonService calls lessonDao.getByCourse({}).", course);
        List<Lesson> lessons = fillGroups(lessonDao.getByCourse(course));
        setCourse(lessons, course);
        setTeacher(lessons);
        return lessons;
    }

    @Override
    public List<Lesson> getByStudent(Student student) {
        log.debug("LessonService calls lessonDao.getByStudent({}).", student);
        List<Lesson> lessons = fillGroups(lessonDao.getByStudent(student));
        setCourse(lessons);
        setTeacher(lessons);
        return lessons;
    }

    @Override
    public List<Lesson> getByTeacher(Teacher teacher) {
        log.debug("LessonService calls lessonDao.getByTeacher({}).", teacher);
        List<Lesson> lessons = fillGroups(lessonDao.getByTeacher(teacher));
        setTeacher(lessons, teacher);
        setCourse(lessons, teacher.getCourse());
        return lessons;
    }

    @Override
    public void save(Lesson lesson) {
        log.debug("LessonService calls lessonDao.save({}).", lesson);
        lessonDao.save(lesson);
    }

    @Override
    public boolean update(Lesson lesson) {
        log.debug("LessonService calls lessonDao.update({}).", lesson);
        return lessonDao.update(lesson);
    }

    @Override
    public boolean delete(Lesson lesson) {
        log.debug("LessonService calls lessonDao.delete({}).", lesson);
        return lessonDao.delete(lesson);
    }

    @Override
    public boolean assignGroups(Lesson lesson, List<Group> groups) {
        log.debug("LessonService calls lessonDao.assignGroups({}, {}).", lesson, groups);
        if (lessonDao.assignGroups(lesson, groups)) {
            log.debug("Assignment was successful. Set groups to the lesson.");
            lesson.setGroups(groups);
            return true;
        }
        log.warn("Assignment groups to lesson was cancelled: ({}, {}).", lesson, groups);
        return false;
    }

    @Override
    public boolean deleteGroup(Lesson lesson, Group group) {
        log.debug("LessonService calls lessonDao.assignGroups({}, {}).", lesson, group);
        if (lessonDao.deleteGroup(lesson, group)) {
            log.debug("Deletion was successful. Remove group from the lesson.");
            lesson.getGroups().remove(group);
            return true;
        }
        log.warn("Deletion {} from {} was cancelled in DAO-layer.", group, lesson);
        return false;
    }

    private Lesson fillGroups(Lesson lesson) {
        log.debug("Set groups to the lesson: call groupService.getByLesson({}).", lesson);
        lesson.setGroups(groupService.getByLesson(lesson));
        return lesson;
    }

    private List<Lesson> fillGroups(List<Lesson> lessons) {
        for (Lesson lesson : lessons) {
            fillGroups(lesson);
        }
        return lessons;
    }

    private void setTeacher(List<Lesson> lessons, Teacher teacher) {
        for (Lesson lesson : lessons) {
            log.debug("Set inputted {} to {}.", teacher, lesson);
            lesson.setTeacher(teacher);
        }
    }

    private void setTeacher(List<Lesson> lessons) {
        for (Lesson lesson : lessons) {
            setTeacher(lesson);
        }
    }

    private void setTeacher(Lesson lesson) {
        log.debug("Call teacherService.getByLesson({}) and set result to the lesson.", lesson);
        lesson.setTeacher(teacherService.getByLesson(lesson));
    }

    private void setCourse(Lesson lesson, Course course) {
        log.debug("Set inputted {} to {}",course, lesson);
        lesson.setCourse(course);
        log.debug("Set inputted {} to lesson's {}.", course, lesson.getTeacher());
        lesson.getTeacher().setCourse(course);
    }

    private void setCourse(List<Lesson> lessons, Course course) {
        for (Lesson lesson : lessons) {
            setCourse(lesson, course);
        }
    }

    private void setCourse(Lesson lesson) {
        log.debug("Call courseService.getById({}).", lesson.getCourse().getId());
        Course course = courseService.getById(lesson.getCourse().getId());
        log.debug("Set returned {} to {}.", course, lesson);
        lesson.setCourse(course);
        log.debug("Set returned {} to lesson's {}",course, lesson.getTeacher());
        lesson.getTeacher().setCourse(course);
    }

    private void setCourse(List<Lesson> lessons) {
        for (Lesson lesson : lessons) {
            log.debug("Call courseService.getById({}).", lesson.getCourse().getId());
            Course course = courseService.getById(lesson.getCourse().getId());
            setCourse(lesson, course);
        }
    }
}
