package com.foxminded.foxuniversity.dao.implementation;

import com.foxminded.foxuniversity.AppConfig;
import com.foxminded.foxuniversity.dao.CourseDao;
import com.foxminded.foxuniversity.dao.GroupDao;
import com.foxminded.foxuniversity.domain.*;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class GroupDaoPostgresTest {
    private static ApplicationContext context;
    private static GroupDao groupDao;
    private static CourseDao courseDao;
    private static ScriptRunner runner;
    private static Lesson lesson;

    private static List<Group> groups = new ArrayList<>();
    private static List<Course> courses = new ArrayList<>();
    private Group group = new Group(0, "test");


    @BeforeAll
    public static void setUp() throws Exception {
        context = new AnnotationConfigApplicationContext(AppConfig.class);
        groupDao = context.getBean(GroupDaoPostgres.class);
        courseDao = context.getBean(CourseDaoPostgres.class);
        runner = new ScriptRunner(context.getBean(DataSource.class).getConnection());
        Reader reader = new BufferedReader(
                new FileReader(context.getClassLoader().getResource("createTables.sql").getFile()));
        runner.runScript(reader);

        for (int i = 0; i < 3; i++) {
            courses.add(new Course(i + 1, "C-0" + (i + 1), "C-0" + (i + 1) + " course"));
            groups.add(new Group(i + 1, "gr-0" + (i + 1)));
        }
        Teacher teacher = new Teacher(0, "Test", "Teacher", courses.get(0));
        lesson = new Lesson(2, courses.get(0), teacher, 10, Day.MONDAY,
                new Time(9, 30, 0), LessonsType.LECTURE);
    }

    @BeforeEach
    public void fillDatabase() throws Exception {
        Reader reader = new BufferedReader(
                new FileReader(context.getClassLoader().getResource("fillDatabase.sql").getFile()));
        runner.runScript(reader);
    }

    @Test
    public void shouldGetAllGroups() {
        List<Group> actual = groupDao.getAll();
        assertEquals(groups, actual);
    }

    @Test
    public void shouldGetGroupById() {
        Group actual = groupDao.getById(1);
        assertEquals(groups.get(0), actual);
    }

    @Test
    public void shouldGetGroupsByLesson() {
        List<Group> actual = groupDao.getByLesson(lesson);
        List<Group> expected = groups.subList(0, 2);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldSaveGroupAndSetId() {
        groupDao.save(group);
        assertNotEquals(0, group.getId());
        Group actual = groupDao.getById(group.getId());
        assertEquals(group, actual);
    }

    @Test
    public void shouldUpdateGroup() {
        Group updatedGroup = groupDao.getById(1);
        assertEquals(updatedGroup, groups.get(0));

        updatedGroup.setName("New name");
        groupDao.update(updatedGroup);

        Group actual = groupDao.getById(1);
        assertEquals(updatedGroup, actual);
    }

    @Test
    public void shouldAssignGroupToCourse() {
        Group updatedGroup = groups.get(2);
        updatedGroup.setCourses(courseDao.getByGroup(updatedGroup));
        assertEquals(0, updatedGroup.getCourses().size());

        groupDao.assignToCourses(updatedGroup, courses);

        updatedGroup.setCourses(courseDao.getByGroup(updatedGroup));
        assertEquals(courses, updatedGroup.getCourses());
    }

    @Test
    public void shouldDeleteGroupFromCourse() {
        Group updatedGroup = groups.get(0);
        updatedGroup.setCourses(courseDao.getByGroup(updatedGroup));
        assertEquals(courses.subList(0, 2), updatedGroup.getCourses());

        groupDao.deleteFromCourse(updatedGroup, courses.get(1));

        updatedGroup.setCourses(courseDao.getByGroup(updatedGroup));
        assertEquals(courses.subList(0, 1), updatedGroup.getCourses());
    }

    @Test
    public void shouldDeleteGroupFromCourses() {
        Group updatedGroup = groups.get(0);
        updatedGroup.setCourses(courseDao.getByGroup(updatedGroup));
        assertEquals(courses.subList(0, 2), updatedGroup.getCourses());

        groupDao.deleteFromCourse(updatedGroup, courses.subList(0, 2));

        updatedGroup.setCourses(courseDao.getByGroup(updatedGroup));
        assertEquals(0, updatedGroup.getCourses().size());
    }

    @Test
    public void shouldDeleteGroup() {
        List<Group> actual = groupDao.getAll();
        assertEquals(groups, actual);

        groupDao.delete(groups.get(2));

        actual = groupDao.getAll();
        List<Group> expected = groups.subList(0, 2);
        assertEquals(expected, actual);
    }

    @AfterEach
    public void clearDatabase() throws Exception {
        Reader reader = new BufferedReader(
                new FileReader(context.getClassLoader().getResource("clearDatabase.sql").getFile()));
        runner.runScript(reader);
    }
}