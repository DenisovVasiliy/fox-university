package com.foxminded.foxuniversity.service.implementation;

import com.foxminded.foxuniversity.dao.LessonDao;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.domain.Lesson;
import com.foxminded.foxuniversity.domain.Student;
import com.foxminded.foxuniversity.domain.Teacher;
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

    @Override
    public List<Lesson> getAll() {
        return lessonDao.getAll();
    }

    @Override
    public Lesson getById(int id) {
        return lessonDao.getById(id);
    }

    @Override
    public boolean save(Lesson lesson) {
        return lessonDao.save(lesson);
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
    public List<Lesson> getByCourse(Course course) {
        return lessonDao.getByCourse(course);
    }

    @Override
    public List<Lesson> getByStudent(Student student) {
        return lessonDao.getByStudent(student);
    }

    @Override
    public List<Lesson> getByTeacher(Teacher teacher) {
        return lessonDao.getByTeacher(teacher);
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

    @Override
    public void fillGroups(Lesson lesson) {
        lesson.setGroups(groupService.getByLesson(lesson));
    }
}
