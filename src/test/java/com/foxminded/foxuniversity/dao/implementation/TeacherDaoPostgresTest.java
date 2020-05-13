package com.foxminded.foxuniversity.dao.implementation;

import com.foxminded.foxuniversity.dao.DaoTestConfig;
import com.foxminded.foxuniversity.dao.TeacherDao;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Teacher;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
@ContextConfiguration(classes = {DaoTestConfig.class})
class TeacherDaoPostgresTest {
    @Autowired
    private ApplicationContext context;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private TeacherDao teacherDao;
    private ScriptRunner runner;

    private static List<Teacher> teachers = new ArrayList<>();
    private static List<Course> courses = new ArrayList<>();
    private static Teacher teacher;

    @BeforeAll
    public static void initDatabase() {
        for (int i = 0; i < 3; i++) {
            courses.add(new Course(i + 1));
            teachers.add(new Teacher(i + 1, "T-0" + (i + 1), "Teacher", courses.get(i)));
        }
        teachers.get(2).setCourse(courses.get(1));
        teacher = new Teacher(0, "T-test", "New", courses.get(2));
    }

    @BeforeEach
    public void fillDatabase() throws Exception {
        runner = new ScriptRunner(dataSource.getConnection());
        Reader reader = new BufferedReader(
                new FileReader(context.getClassLoader().getResource("fillDatabase.sql").getFile()));
        runner.runScript(reader);
    }

    @Test
    public void shouldGetAllTeachers() {
        List<Teacher> actual = teacherDao.getAll();
        assertEquals(teachers, actual);
    }

    @Test
    public void shouldGetTeacherById() {
        Teacher actual = teacherDao.getById(2);
        assertEquals(teachers.get(1), actual);
        assertEquals(teachers.get(1).getCourse(), actual.getCourse());
    }

    @Test
    public void shouldGetCourse() {
        List<Teacher> actual = teacherDao.getByCourse(courses.get(1));
        List<Teacher> expected = teachers.subList(1, 3);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldSaveTeacherAndSetId() {
        teacherDao.save(teacher);
        assertNotEquals(0, teacher.getId());
        Teacher actual = teacherDao.getById(teacher.getId());
        assertEquals(teacher, actual);
        assertEquals(teacher.getCourse(), actual.getCourse());
    }

    @Test
    public void shouldUpdateTeacher() {
        Teacher updatedTeacher = teacherDao.getById(2);
        assertEquals(updatedTeacher, teachers.get(1));
        assertEquals(updatedTeacher.getCourse(), teachers.get(1).getCourse());

        updatedTeacher.setFirstName("T-Up");
        updatedTeacher.setLastName("Updated");
        updatedTeacher.setCourse(courses.get(2));

        assertTrue(teacherDao.update(updatedTeacher));

        Teacher actual = teacherDao.getById(updatedTeacher.getId());
        assertEquals(updatedTeacher, actual);
        assertEquals(updatedTeacher.getCourse(), actual.getCourse());
    }

    @Test
    public void shouldDeleteTeacher() {
        List<Teacher> actual = teacherDao.getAll();
        assertEquals(teachers, actual);

        assertTrue(teacherDao.delete(teachers.get(2)));

        actual = teacherDao.getAll();
        List<Teacher> expected = teachers.subList(0, 2);
        assertEquals(expected, actual);
    }

    @AfterEach
    public void clearDatabase() throws Exception {
        Reader reader = new BufferedReader(
                new FileReader(context.getClassLoader().getResource("clearDatabase.sql").getFile()));
        runner.runScript(reader);
    }
}