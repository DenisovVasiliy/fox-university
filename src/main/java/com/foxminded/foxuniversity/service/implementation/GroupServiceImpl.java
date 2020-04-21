package com.foxminded.foxuniversity.service.implementation;

import com.foxminded.foxuniversity.dao.GroupDao;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.domain.Lesson;
import com.foxminded.foxuniversity.service.CourseService;
import com.foxminded.foxuniversity.service.GroupService;
import com.foxminded.foxuniversity.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GroupServiceImpl implements GroupService {
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private CourseService courseService;
    @Autowired
    private StudentService studentService;

    public List<Group> getAll() {
        List<Group> groups = groupDao.getAll();
        fillCourses(groups);
        fillStudents(groups);
        return groups;
    }

    public Group getById(int id) {
        Group group = groupDao.getById(id);
        fillCourses(group);
        fillStudents(group);
        return group;
    }

    public List<Group> getByLesson(Lesson lesson) {
        List<Group> groups = groupDao.getByLesson(lesson);
        fillCourses(groups);
        fillStudents(groups);
        return groups;
    }

    public boolean save(Group group) {
        return groupDao.save(group);
    }

    public boolean update(Group group) {
        return groupDao.update(group);
    }

    public boolean delete(Group group) {
        return groupDao.delete(group);
    }

    public boolean assignToCourses(Group group, List<Course> courses) {
        if (groupDao.assignToCourses(group, courses)) {
            group.setCourses(courses);
            return true;
        }
        return false;
    }

    public boolean deleteFromCourse(Group group, Course course) {
        if (groupDao.deleteFromCourse(group, course)) {
            group.getCourses().remove(course);
            return true;
        }
        return false;
    }

    public boolean deleteFromCourse(Group group, List<Course> courses) {
        if (groupDao.deleteFromCourse(group, courses)) {
            for (Course course : courses) {
                group.getCourses().remove(course);
            }
            return true;
        }
        return false;
    }

    private void fillCourses(Group group) {
        group.setCourses(courseService.getByGroup(group));
    }

    private void fillCourses(List<Group> groups) {
        for (Group group : groups) {
            fillCourses(group);
        }
    }

    private void fillStudents(Group group) {
        group.setStudents(studentService.getByGroup(group));
    }

    private void fillStudents(List<Group> groups) {
        for (Group group : groups) {
            fillStudents(group);
        }
    }
}
