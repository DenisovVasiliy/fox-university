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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    @Autowired
    private TeacherService teacherService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<Lesson> getAll() {
        if (logger.isDebugEnabled()) {
            logger.debug("LessonService calls lessonDao.getAll().");
        }
        List<Lesson> lessons = fillGroups(lessonDao.getAll());
        setCourse(lessons);
        setTeacher(lessons);
        return lessons;
    }

    @Override
    public Lesson getById(int id) {
        if (logger.isDebugEnabled()) {
            logger.debug("LessonService calls lessonDao.getById(" + id + ").");
        }
        Lesson lesson = fillGroups(lessonDao.getById(id));
        setCourse(lesson);
        setTeacher(lesson);
        return lesson;
    }

    @Override
    public List<Lesson> getByCourse(Course course) {
        if (logger.isDebugEnabled()) {
            logger.debug("LessonService calls lessonDao.getByCourse(Course{id = " + course.getId() + "}).");
        }
        List<Lesson> lessons = fillGroups(lessonDao.getByCourse(course));
        setCourse(lessons, course);
        setTeacher(lessons);
        return lessons;
    }

    @Override
    public List<Lesson> getByStudent(Student student) {
        if (logger.isDebugEnabled()) {
            logger.debug("LessonService calls lessonDao.getByStudent(Student{id = " + student.getId() + "}).");
        }
        List<Lesson> lessons = fillGroups(lessonDao.getByStudent(student));
        setCourse(lessons);
        setTeacher(lessons);
        return lessons;
    }

    @Override
    public List<Lesson> getByTeacher(Teacher teacher) {
        if (logger.isDebugEnabled()) {
            logger.debug("LessonService calls lessonDao.getByTeacher(Teacher{id = " + teacher.getId() + "}).");
        }
        List<Lesson> lessons = fillGroups(lessonDao.getByTeacher(teacher));
        setTeacher(lessons, teacher);
        setCourse(lessons, teacher.getCourse());
        return lessons;
    }

    @Override
    public void save(Lesson lesson) {
        if (logger.isDebugEnabled()) {
            logger.debug("LessonService calls lessonDao.save(Lesson{id = " + lesson.getId() + "}).");
        }
        lessonDao.save(lesson);
    }

    @Override
    public boolean update(Lesson lesson) {
        if (logger.isDebugEnabled()) {
            logger.debug("LessonService calls lessonDao.update(Lesson{id = " + lesson.getId() + "}).");
        }
        return lessonDao.update(lesson);
    }

    @Override
    public boolean delete(Lesson lesson) {
        if (logger.isDebugEnabled()) {
            logger.debug("LessonService calls lessonDao.delete(Lesson{id = " + lesson.getId() + "}).");
        }
        return lessonDao.delete(lesson);
    }

    @Override
    public boolean assignGroups(Lesson lesson, List<Group> groups) {
        if (logger.isDebugEnabled()) {
            logger.debug("LessonService calls lessonDao.assignGroups(Lesson{id = " + lesson.getId() + "}, " +
                    "List<Group>[" + groups.size() + "]).");
        }
        if (lessonDao.assignGroups(lesson, groups)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Assignment was successful. Set groups to the lesson.");
            }
            lesson.setGroups(groups);
            return true;
        }
        logger.warn("Assignment groups to lesson was cancelled. (Lesson{id = " + lesson.getId() + "}, " + groups);
        return false;
    }

    @Override
    public boolean deleteGroup(Lesson lesson, Group group) {
        if (logger.isDebugEnabled()) {
            logger.debug("LessonService calls lessonDao.assignGroups(" +
                    "Lesson{id = " + lesson.getId() + "}, " + group + ").");
        }
        if (lessonDao.deleteGroup(lesson, group)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Deletion was successful. Remove group from the lesson.");
            }
            lesson.getGroups().remove(group);
            return true;
        }
        logger.warn("Deletion " + group + " from Lesson{id = " + lesson.getId() + " was cancelled in DAO-layer.");
        return false;
    }

    private Lesson fillGroups(Lesson lesson) {
        if (logger.isDebugEnabled()) {
            logger.debug("Set groups to the lesson: call groupService.getByLesson(" +
                    "Lesson{id = " + lesson.getId() + "})");
        }
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
            if (logger.isDebugEnabled()) {
                logger.debug("Set inputted Teacher{id = " + teacher.getId() + "} " +
                        "to Lesson{id = " + lesson.getId() + "}.");
            }
            lesson.setTeacher(teacher);
        }
    }

    private void setTeacher(List<Lesson> lessons) {
        for (Lesson lesson : lessons) {
            setTeacher(lesson);
        }
    }

    private void setTeacher(Lesson lesson) {
        if (logger.isDebugEnabled()) {
            logger.debug("Call teacherService.getByLesson(Lesson{id = " + lesson.getId() + "}) " +
                    "and set result to the lesson.");
        }
        lesson.setTeacher(teacherService.getByLesson(lesson));
    }

    private void setCourse(Lesson lesson, Course course) {
        if (logger.isDebugEnabled()) {
            logger.debug("Set inputted Course{id = " + course.getId() + "} " +
                    "to Lesson{id = " + lesson.getId() + "}.");
        }
        lesson.setCourse(course);
        if (logger.isDebugEnabled()) {
            logger.debug("Set inputted Course{id = " + course.getId() + "} " +
                    "to lesson's Teacher{id = " + lesson.getTeacher().getId() + "}.");
        }
        lesson.getTeacher().setCourse(course);
    }

    private void setCourse(List<Lesson> lessons, Course course) {
        for (Lesson lesson : lessons) {
            setCourse(lesson, course);
        }
    }

    private void setCourse(Lesson lesson) {
        if (logger.isDebugEnabled()) {
            logger.debug("Call courseService.getById(" + lesson.getCourse().getId() + ").");
        }
        Course course = courseService.getById(lesson.getCourse().getId());
        if (logger.isDebugEnabled()) {
            logger.debug("Set returned Course{id = " + course.getId() + "} " +
                    "to Lesson{id = " + lesson.getId() + "}.");
        }
        lesson.setCourse(course);
        if (logger.isDebugEnabled()) {
            logger.debug("Set returned Course{id = " + course.getId() + "} " +
                    "to lesson's Teacher{id = " + lesson.getTeacher().getId() + "}.");
        }
        lesson.getTeacher().setCourse(course);
    }

    private void setCourse(List<Lesson> lessons) {
        for (Lesson lesson : lessons) {
            if (logger.isDebugEnabled()) {
                logger.debug("Call courseService.getById(" + lesson.getCourse().getId() + ").");
            }
            Course course = courseService.getById(lesson.getCourse().getId());
            setCourse(lesson, course);
        }
    }
}
