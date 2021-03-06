package com.foxminded.foxuniversity.dao.implementation;

import com.foxminded.foxuniversity.dao.DaoTestConfig;
import com.foxminded.foxuniversity.dao.StudentDao;
import com.foxminded.foxuniversity.dao.exceptions.EntityNotFoundException;
import com.foxminded.foxuniversity.dao.exceptions.QueryRestrictedException;
import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.domain.Student;
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

import static com.foxminded.foxuniversity.dao.exceptions.ExceptionsMessageConstants.*;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DaoTestConfig.class})
class StudentDaoPostgresTest {
    @Autowired
    private ApplicationContext context;
    @Autowired
    private StudentDao studentDao;
    @Autowired
    private DataSource dataSource;
    private static ScriptRunner runner;

    private static Student student = new Student(0, "New", "Student");
    private static List<Student> students = new ArrayList<>();
    private static List<Group> groups = new ArrayList<>();

    @BeforeAll
    public static void initDatabase() {
        for (int i = 0; i < 3; i++) {
            students.add(new Student(i + 1, "S-0" + (i + 1), "Student"));
            groups.add(new Group(i + 1));
        }
        students.add(new Student(4, "S-04", "Student"));
        students.get(0).setGroup(groups.get(0));
        students.get(1).setGroup(groups.get(0));
        students.get(2).setGroup(groups.get(1));
        student.setGroup(groups.get(2));
    }

    @BeforeEach
    public void fillDatabase() throws Exception {
        runner = new ScriptRunner(dataSource.getConnection());
        Reader reader = new BufferedReader(
                new FileReader(context.getClassLoader().getResource("fillDatabase.sql").getFile()));
        runner.runScript(reader);
    }

    @Test
    public void shouldGetAllStudents() {
        List<Student> actual = studentDao.getAll();
        assertEquals(students, actual);
    }

    @Test
    public void shouldGetStudentById() {
        Student actual = studentDao.getById(2);
        assertEquals(students.get(1), actual);
        assertEquals(students.get(1).getGroup(), actual.getGroup());
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenCantFindEntity() {
        int id = 10;
        Throwable thrown = assertThrows(EntityNotFoundException.class, () -> studentDao.getById(id));
        String actualMessage = thrown.getMessage();
        String expectedMessage = format(ENTITY_NOT_FOUND, "Student", id);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void shouldGetStudentsByGroup() {
        List<Student> actual = studentDao.getByGroup(groups.get(0));
        List<Student> expected = students.subList(0, 2);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldSaveStudentAndSetId() {
        studentDao.save(student);
        assertNotEquals(0, student.getId());
        Student actual = studentDao.getById(student.getId());
        assertEquals(student, actual);
        assertEquals(student.getGroup(), actual.getGroup());
    }

    @Test
    public void shouldAssignStudentToGroup() {
        Student expected = studentDao.getById(4);
        assertNull(expected.getGroup());

        assertTrue(studentDao.assignToGroup(expected, groups.get(2)));

        expected.setGroup(groups.get(2));
        Student actual = studentDao.getById(expected.getId());
        assertEquals(expected, actual);
        assertEquals(expected.getGroup(), actual.getGroup());
    }

    @Test
    public void shouldThrowQueryRestrictedExceptionWhenSuchRecordAlreadyExist() {
        Throwable thrown = assertThrows(QueryRestrictedException.class,
                () -> studentDao.assignToGroup(students.get(0), groups.get(0)));
        String actualMessage = thrown.getMessage();
        String expectedMessage = format(QUERY_RESTRICTED_DUPLICATE_KEY, students.get(0));
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void shouldThrowQueryRestrictedExceptionWhenNoStudentsWithSuchId() {
        Student unsavedStudent = new Student(100, "1", "2");
        Throwable thrown = assertThrows(QueryRestrictedException.class,
                () -> studentDao.assignToGroup(unsavedStudent, groups.get(0)));
        String actualMessage = thrown.getMessage();
        String expectedMessage = format(QUERY_RESTRICTED_NO_SUCH_ID, unsavedStudent, groups.get(0));
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void shouldThrowQueryRestrictedExceptionWhenNoGroupsWithSuchId() {
        Group unsavedGroup = new Group(100);
        Throwable thrown = assertThrows(QueryRestrictedException.class,
                () -> studentDao.assignToGroup(students.get(3), unsavedGroup));
        String actualMessage = thrown.getMessage();
        String expectedMessage = format(QUERY_RESTRICTED_NO_SUCH_ID, students.get(3), unsavedGroup);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void shouldUpdateStudent() {
        Student updatedStudent = studentDao.getById(students.get(1).getId());
        assertEquals(updatedStudent, students.get(1));
        assertEquals(updatedStudent.getGroup(), students.get(1).getGroup());

        updatedStudent.setFirstName("S-02U");
        updatedStudent.setLastName("Updated Student");
        updatedStudent.setGroup(groups.get(1));

        assertTrue(studentDao.update(updatedStudent));

        Student actual = studentDao.getById(updatedStudent.getId());
        assertEquals(updatedStudent, actual);
        assertEquals(updatedStudent.getGroup(), actual.getGroup());
    }

    @Test
    public void shouldUpdateAssignment() {
        Student updatedStudent = studentDao.getById(students.get(1).getId());
        assertEquals(updatedStudent, students.get(1));
        assertEquals(updatedStudent.getGroup(), students.get(1).getGroup());

        updatedStudent.setGroup(groups.get(1));

        assertTrue(studentDao.update(updatedStudent));

        Student actual = studentDao.getById(updatedStudent.getId());
        assertEquals(updatedStudent, actual);
        assertEquals(updatedStudent.getGroup(), actual.getGroup());
    }

    @Test
    public void shouldDeleteStudent() {
        List<Student> actual = studentDao.getAll();
        assertEquals(students, actual);

        assertTrue(studentDao.delete(students.get(0)));

        actual = studentDao.getAll();
        List<Student> expected = students.subList(1, 4);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldDeleteAssignment() {
        Student updatedStudent = studentDao.getById(1);
        assertNotNull(updatedStudent.getGroup());

        assertTrue(studentDao.deleteAssignment(updatedStudent));

        updatedStudent.setGroup(null);

        Student actual = studentDao.getById(updatedStudent.getId());
        assertEquals(updatedStudent, actual);
        assertEquals(updatedStudent.getGroup(), actual.getGroup());
    }

    @AfterEach
    public void clearDatabase() throws Exception {
        Reader reader = new BufferedReader(
                new FileReader(context.getClassLoader().getResource("clearDatabase.sql").getFile()));
        runner.runScript(reader);
    }
}