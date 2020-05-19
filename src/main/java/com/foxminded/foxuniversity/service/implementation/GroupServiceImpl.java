package com.foxminded.foxuniversity.service.implementation;

import com.foxminded.foxuniversity.dao.GroupDao;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.domain.Lesson;
import com.foxminded.foxuniversity.service.GroupService;
import com.foxminded.foxuniversity.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class GroupServiceImpl implements GroupService {
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private StudentService studentService;

    @Override
    public List<Group> getAll() {
        log.debug("GroupService calls groupDao.getAll().");
        List<Group> groups = groupDao.getAll();
        fillStudents(groups);
        return groups;
    }

    @Override
    public Group getById(int id) {
        log.debug("GroupService calls groupDao.getById({}).", id);
        Group group = groupDao.getById(id);
        fillStudents(group);
        return group;
    }

    @Override
    public List<Group> getByLesson(Lesson lesson) {
        log.debug("GroupService calls groupDao.getByLesson(Lesson{id = {}).", lesson.getId());
        List<Group> groups = groupDao.getByLesson(lesson);
        fillStudents(groups);
        return groups;
    }

    @Override
    public List<Group> getByCourse(Course course) {
        log.debug("GroupService calls groupDao.getByCourse(Course{id = {}).", course.getId());
        List<Group> groups = groupDao.getByCourse(course);
        fillStudents(groups);
        return groups;
    }

    @Override
    public void save(Group group) {
        log.debug("GroupService calls groupDao.save({}).", group);
        groupDao.save(group);
    }

    @Override
    public boolean update(Group group) {
        log.debug("GroupService calls groupDao.update({}).", group);
        return groupDao.update(group);
    }

    @Override
    public boolean delete(Group group) {
        log.debug("GroupService calls groupDao.delete({}).", group);
        return groupDao.delete(group);
    }

    @Override
    public boolean assignToCourses(Group group, List<Course> courses) {
        log.debug("GroupService calls groupDao.assignToCourses({}, , List<Course>[{}]).", group, courses.size());
        if (groupDao.assignToCourses(group, courses)) {
            log.debug("Assignment was successful. Set group to each course in the list.");
            for (Course course : courses) {
                course.getGroups().add(group);
            }
            return true;
        }
        log.warn("Assignment {} to courses was cancelled.", group);
        return false;
    }

    @Override
    public boolean deleteFromCourse(Group group, Course course) {
        if (course.getGroups().contains(group)) {
            log.debug("Call groupDao.deleteFromCourse({},{}).", group, course);
            if (groupDao.deleteFromCourse(group, course)) {
                log.debug("Deletion was successful. Remove group from course.");
                course.getGroups().remove(group);
                return true;
            }
            log.warn("Deletion {} from {} was cancelled in DAO-layer.", group, course);
            return false;
        }
        log.warn("Deletion was cancelled: {} isn't assigned to the {}", group, course);
        return false;
    }

    @Override
    public boolean deleteFromCourse(Group group, List<Course> courses) {
        for (Course course : courses) {
            if (!course.getGroups().contains(group)) {
                log.warn("Deletion was cancelled: {} isn't assigned to the {}", group, course);
                return false;
            }
        }
        log.debug("Call groupDao.deleteFromCourse({}, {}).", group, courses.size());
        if (groupDao.deleteFromCourse(group, courses)) {
            log.debug("Deletion was successful. Remove group from courses.");
            for (Course course : courses) {
                course.getGroups().remove(group);
            }
            return true;
        }
        log.warn("Deletion {} from courses was cancelled in DAO-layer.", group);
        return false;
    }

    private void fillStudents(Group group) {
        log.debug("Set students to group: call studentService.getByGroup({})", group);
        group.setStudents(studentService.getByGroup(group));
    }

    private void fillStudents(List<Group> groups) {
        for (Group group : groups) {
            fillStudents(group);
        }
    }
}
