package com.foxminded.foxuniversity.dao.implementation;

import com.foxminded.foxuniversity.dao.CourseDao;
import com.foxminded.foxuniversity.dao.DaoTestConfig;
import com.foxminded.foxuniversity.dao.GroupDao;
import com.foxminded.foxuniversity.dao.exceptions.EntityNotFoundException;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.domain.Day;
import com.foxminded.foxuniversity.domain.Teacher;
import com.foxminded.foxuniversity.domain.LessonsType;
import com.foxminded.foxuniversity.domain.Lesson;
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
class GroupDaoPostgresTest {
    @Autowired
    private ApplicationContext context;
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private DataSource dataSource;
    private static ScriptRunner runner;
    private static Lesson lesson;

    private static List<Group> groups = new ArrayList<>();
    private static List<Course> courses = new ArrayList<>();
    private Group group = new Group(0, "test");

    @BeforeAll
    public static void setUp() {
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
        runner = new ScriptRunner(dataSource.getConnection());
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
    public void shouldThrowEntityNotFoundExceptionWhenCantFindEntity() {
        int id = 10;
        Throwable thrown = assertThrows(EntityNotFoundException.class, () -> groupDao.getById(id));
        String actualMessage = thrown.getMessage();
        String expectedMessage = format(ENTITY_NOT_FOUND, "Group", id);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void shouldGetGroupsByLesson() {
        List<Group> actual = groupDao.getByLesson(lesson);
        List<Group> expected = groups.subList(0, 2);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldGetGroupsByCourse() {
        List<Group> actual = groupDao.getByCourse(courses.get(1));
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
        assertTrue(groupDao.update(updatedGroup));

        Group actual = groupDao.getById(1);
        assertEquals(updatedGroup, actual);
    }

    @Test
    public void shouldAssignGroupToCourse() {
        Group updatedGroup = groups.get(2);
        List<Course> actualCourses = courseDao.getByGroup(updatedGroup);
        assertEquals(0, actualCourses.size());

        assertTrue(groupDao.assignToCourses(updatedGroup, courses));

        actualCourses = courseDao.getByGroup(updatedGroup);
        assertEquals(courses, actualCourses);
    }

    @Test
    public void shouldDeleteGroupFromCourse() {
        Group updatedGroup = groups.get(0);
        List<Course> actualCourses = courseDao.getByGroup(updatedGroup);
        assertEquals(courses.subList(0, 2), actualCourses);

        assertTrue(groupDao.deleteFromCourse(updatedGroup, courses.get(1)));

        actualCourses = courseDao.getByGroup(updatedGroup);
        assertEquals(courses.subList(0, 1), actualCourses);
    }

    @Test
    public void shouldDeleteGroupFromCourses() {
        Group updatedGroup = groups.get(0);
        List<Course> actualCourses = courseDao.getByGroup(updatedGroup);
        assertEquals(courses.subList(0, 2), actualCourses);

        assertTrue(groupDao.deleteFromCourse(updatedGroup, courses.subList(0, 2)));

        actualCourses = courseDao.getByGroup(updatedGroup);
        assertEquals(0, actualCourses.size());
    }

    @Test
    public void shouldDeleteGroup() {
        List<Group> actual = groupDao.getAll();
        assertEquals(groups, actual);

        assertTrue(groupDao.delete(groups.get(2)));

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