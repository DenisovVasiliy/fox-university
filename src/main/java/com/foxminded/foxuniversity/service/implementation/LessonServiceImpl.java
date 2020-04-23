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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LessonServiceImpl implements LessonService {
    @Autowired
    private LessonDao lessonDao;
    @Autowired
    private GroupService groupService;
    @Autowired
    private CourseService courseService;

    @Override
    public List<Lesson> getAll() {
        List<Lesson> lessons = fillGroups(lessonDao.getAll());
        setCourse(lessons);
        return lessons;
    }

    @Override
    public Lesson getById(int id) {
        Lesson lesson = fillGroups(lessonDao.getById(id));
        setCourse(lesson);
        return lesson;
    }

    @Override
    public List<Lesson> getByCourse(Course course) {
        List<Lesson> lessons = fillGroups(lessonDao.getByCourse(course));
        setCourse(lessons, course);
        return lessons;
    }

    @Override
    public List<Lesson> getByStudent(Student student) {
        List<Lesson> lessons = fillGroups(lessonDao.getByStudent(student));
        setCourse(lessons);
        return lessons;
    }

    @Override
    public List<Lesson> getByTeacher(Teacher teacher) {
        List<Lesson> lessons = fillGroups(lessonDao.getByTeacher(teacher));
        setTeacher(lessons, teacher);
        setCourse(lessons, teacher.getCourse());
        return lessons;
    }

    @Override
    public void save(Lesson lesson) {
        lessonDao.save(lesson);
    }

    @Override
    public boolean update(Lesson lesson) {
        return lessonDao.update(lesson);
    }

    @Override
    public boolean delete(Lesson lesson) {
        return lessonDao.delete(lesson);
    }

    @Override
    public boolean assignGroups(Lesson lesson, List<Group> groups) {
        if (lessonDao.assignGroups(lesson, groups)) {
            lesson.setGroups(groups);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteGroup(Lesson lesson, Group group) {
        if (lessonDao.deleteGroup(lesson, group)) {
            lesson.getGroups().remove(group);
            return true;
        }
        return false;
    }

    private Lesson fillGroups(Lesson lesson) {
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
            lesson.setTeacher(teacher);
        }
    }

    private void setCourse(Lesson lesson, Course course) {
        lesson.setCourse(course);
        lesson.getTeacher().setCourse(course);
    }

    private void setCourse(List<Lesson> lessons, Course course) {
        for (Lesson lesson : lessons) {
            setCourse(lesson, course);
        }
    }

    private void setCourse(Lesson lesson) {
        Course course = courseService.getById(lesson.getCourse().getId());
        lesson.setCourse(course);
        lesson.getTeacher().setCourse(course);
    }

    private void setCourse(List<Lesson> lessons) {
        for (Lesson lesson : lessons) {
            Course course = courseService.getById(lesson.getCourse().getId());
            setCourse(lesson, course);
        }
    }
}
