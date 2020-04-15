package com.foxminded.foxuniversity.service.implementation;

import com.foxminded.foxuniversity.dao.TeacherDao;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Lesson;
import com.foxminded.foxuniversity.domain.Teacher;
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

    @Override
    public List<Teacher> getAll() {
        return teacherDao.getAll();
    }

    @Override
    public Teacher getById(int id) {
        return teacherDao.getById(id);
    }

    @Override
    public boolean save(Teacher teacher) {
        return teacherDao.save(teacher);
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
        return teacherDao.getByCourse(course);
    }

    @Override
    public List<Lesson> getTimetable(Teacher teacher) {
        return lessonService.getByTeacher(teacher);
    }
}
