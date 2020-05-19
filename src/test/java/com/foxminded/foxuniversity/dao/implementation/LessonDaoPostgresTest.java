package com.foxminded.foxuniversity.dao.implementation;

import com.foxminded.foxuniversity.dao.DaoTestConfig;
import com.foxminded.foxuniversity.dao.GroupDao;
import com.foxminded.foxuniversity.dao.LessonDao;
import com.foxminded.foxuniversity.dao.TeacherDao;
import com.foxminded.foxuniversity.dao.exceptions.EntityNotFoundException;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.domain.Day;
import com.foxminded.foxuniversity.domain.Teacher;
import com.foxminded.foxuniversity.domain.LessonsType;
import com.foxminded.foxuniversity.domain.Lesson;
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
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static com.foxminded.foxuniversity.dao.exceptions.ExceptionsMessageConstants.ENTITY_NOT_FOUND;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DaoTestConfig.class})
class LessonDaoPostgresTest {
    @Autowired
    private ApplicationContext context;
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private LessonDao lessonDao;
    @Autowired
    private DataSource dataSource;
    private static ScriptRunner runner;

    private static List<Lesson> lessons = new ArrayList<>();
    private static Lesson lesson;
    private static Student student = new Student(1, "S-01", "Student");
    private static List<Teacher> teachers = new ArrayList<>();
    private static List<Course> courses = new ArrayList<>();
    private static List<Group> groups = new ArrayList<>();

    @BeforeAll
    public static void setUp() {
        for (int i = 0; i < 3; i++) {
            courses.add(new Course(i + 1, "C-0" + (i + 1), "C-0" + (i + 1) + " course"));
            teachers.add(new Teacher(i + 1, "T-0" + (i + 1), "Teacher", courses.get(i)));
            groups.add(new Group(i + 1, "gr-0" + (i + 1)));
        }
        teachers.get(2).setCourse(courses.get(1));
        lessons.add(new Lesson(1, courses.get(0), teachers.get(0), 10, Day.MONDAY,
                new Time(9, 30, 0), LessonsType.LECTURE));
        lessons.add(new Lesson(2, courses.get(1), teachers.get(0), 10, Day.TUESDAY,
                new Time(9, 30, 0), LessonsType.LECTURE));
        lessons.add(new Lesson(3, courses.get(1), teachers.get(1), 20, Day.FRIDAY,
                new Time(9, 30, 0), LessonsType.LECTURE));
        lesson = new Lesson(0, courses.get(2), teachers.get(2), 30, Day.THURSDAY,
                new Time(12, 30, 0), LessonsType.PRACTICE);
        lessons.get(0).setGroups(groups.subList(0, 1));
        lessons.get(1).setGroups(groups.subList(0, 2));
        lessons.get(2).setGroups(groups.subList(1, 2));
        lesson.setGroups(groups.subList(2, 3));

        student.setGroup(groups.get(0));
    }

    @BeforeEach
    public void fillDatabase() throws Exception {
        runner = new ScriptRunner(dataSource.getConnection());
        Reader reader = new BufferedReader(
                new FileReader(context.getClassLoader().getResource("fillDatabase.sql").getFile()));
        runner.runScript(reader);
    }

    @Test
    public void shouldGetAllLessons() {
        List<Lesson> actual = lessonDao.getAll();
        assertEquals(lessons, actual);
    }

    @Test
    public void shouldGetLessonById() {
        Lesson actual = lessonDao.getById(1);
        assertEquals(lessons.get(0), actual);
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenCantFindEntity() {
        int id = 10;
        Throwable thrown = assertThrows(EntityNotFoundException.class, () -> lessonDao.getById(id));
        String actualMessage = thrown.getMessage();
        String expectedMessage = format(ENTITY_NOT_FOUND, "Lesson", id);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void shouldGetLessonByCourse() {
        List<Lesson> actual = lessonDao.getByCourse(courses.get(1));
        assertEquals(lessons.subList(1, 3), actual);
    }

    @Test
    public void shouldGetLessonByStudent() {
        List<Lesson> actual = lessonDao.getByStudent(student);
        assertEquals(lessons.subList(0, 2), actual);
    }

    @Test
    public void shouldGetLessonByTeacher() {
        List<Lesson> actual = lessonDao.getByTeacher(teachers.get(0));
        assertEquals(lessons.subList(0, 2), actual);
    }

    @Test
    public void shouldSaveLessonAndSetId() {
        lessonDao.save(lesson);
        assertNotEquals(0, lesson.getId());
        Lesson actual = lessonDao.getById(lesson.getId());
        assertEquals(lesson, actual);
    }

    @Test
    public void shouldAssignGroupsToLesson() {
        Lesson operatedLesson = lessonDao.getById(3);
        List<Group> actualGroups = groupDao.getByLesson(operatedLesson);
        assertEquals(groups.subList(1, 2), actualGroups);

        assertTrue(lessonDao.assignGroups(operatedLesson, groups.subList(0, 1)));

        List<Group> expectedGroups = groups.subList(0, 2);
        List<Group> actual = groupDao.getByLesson(operatedLesson);
        assertEquals(expectedGroups, actual);
    }

    @Test
    public void shouldUpdateLesson() {
        Lesson updatedLesson = lessonDao.getById(1);
        assertEquals(lessons.get(0), updatedLesson);

        updatedLesson.setTeacher(teachers.get(2));
        updatedLesson.setCourse(courses.get(2));

        assertTrue(lessonDao.update(updatedLesson));

        Lesson actualLesson = lessonDao.getById(updatedLesson.getId());

        assertEquals(updatedLesson, actualLesson);
    }

    @Test
    public void shouldDeleteGroupFromLesson() {
        Lesson updatedLesson = lessonDao.getById(1);
        assertEquals(lessons.get(0), updatedLesson);
        updatedLesson.setGroups(groupDao.getByLesson(updatedLesson));
        List<Group> emptyGroups = new ArrayList<>();

        assertTrue(lessonDao.deleteGroup(updatedLesson, updatedLesson.getGroups().get(0)));

        List<Group> actual = groupDao.getByLesson(updatedLesson);
        assertEquals(emptyGroups, actual);
    }

    @Test
    public void shouldDeleteLesson() {
        List<Lesson> actual = lessonDao.getAll();
        assertEquals(lessons, actual);

        lessonDao.delete(lessons.get(2));

        actual = lessonDao.getAll();
        assertEquals(lessons.subList(0, 2), actual);
    }

    @AfterEach
    public void clearDatabase() throws Exception {
        Reader reader = new BufferedReader(
                new FileReader(context.getClassLoader().getResource("clearDatabase.sql").getFile()));
        runner.runScript(reader);
    }
}