package com.foxminded.foxuniversity.dao.implementation;

import com.foxminded.foxuniversity.dao.CourseDao;
import com.foxminded.foxuniversity.dao.DaoTestConfig;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Group;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.ContextConfiguration;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { DaoTestConfig.class })
class CourseDaoPostgresTest {
    @Autowired
    private ApplicationContext context;
    @Autowired
    private CourseDao courseDAO;
    @Autowired
    private DataSource dataSource;
    private Course course = new Course(0, "Testing", "Test course.");
    private ScriptRunner runner;

    private static List<Course> courses = new ArrayList<>();
    private static List<Group> groups = new ArrayList<>();

    @BeforeAll
    public static void initDataBase() throws Exception {
        for (int i = 0; i < 3; i++) {
            courses.add(new Course(i + 1, "C-0" + (i + 1), "C-0" + (i + 1) + " course"));
            groups.add(new Group(i + 1, "gr-0" + (i + 1)));
        }
    }

    @BeforeEach
    public void fillDatabase() throws Exception {
        runner = new ScriptRunner(dataSource.getConnection());
        Reader fillDatabaseReader = new BufferedReader(
                new FileReader(context.getClassLoader().getResource("fillDatabase.sql").getFile()));
        runner.runScript(fillDatabaseReader);
    }

    @Test
    public void shouldGetAllCourses() {
        List<Course> actual = courseDAO.getAll();
        assertEquals(courses, actual);
    }

    @Test
    public void shouldSaveCourseAndSetId() {
        courseDAO.save(course);
        Course actual = courseDAO.getById(course.getId());
        assertNotEquals(0, course.getId());
        assertEquals(course, actual);
    }

    @Test
    public void shouldGetCourseById() {
        Course actual = courseDAO.getById(1);
        assertEquals(courses.get(0), actual);
    }

    @Test
    public void shouldDeleteCourse() {
        List<Course> actual = courseDAO.getAll();
        assertEquals(courses, actual);

        assertTrue(courseDAO.delete(courses.get(2)));

        actual = courseDAO.getAll();
        List<Course> expected = courses.subList(0, 2);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldGetCoursesByGroup() {
        List<Course> actual = courseDAO.getByGroup(groups.get(0));
        List<Course> expected = courses.subList(0, 2);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldUpdateCourse() {
        Course updatedCourse = courseDAO.getById(courses.get(1).getId());
        assertEquals(courses.get(1), updatedCourse);
        updatedCourse.setName("New name");
        updatedCourse.setDescription("New Desc.");

        assertTrue(courseDAO.update(updatedCourse));

        Course actual = courseDAO.getById(updatedCourse.getId());

        assertEquals(updatedCourse, actual);
    }

    @AfterEach
    public void clearDatabase() throws Exception {
        Reader reader = new BufferedReader(
                new FileReader(context.getClassLoader().getResource("clearDatabase.sql").getFile()));
        runner.runScript(reader);
    }
}