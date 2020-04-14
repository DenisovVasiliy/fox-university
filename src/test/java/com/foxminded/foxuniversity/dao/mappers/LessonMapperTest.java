package com.foxminded.foxuniversity.dao.mappers;

import com.foxminded.foxuniversity.MappersTestConfig;
import com.foxminded.foxuniversity.dao.implementation.CourseDaoPostgres;
import com.foxminded.foxuniversity.dao.implementation.GroupDaoPostgres;
import com.foxminded.foxuniversity.dao.implementation.TeacherDaoPostgres;
import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Teacher;
import com.foxminded.foxuniversity.domain.Lesson;
import com.foxminded.foxuniversity.domain.LessonsType;
import com.foxminded.foxuniversity.domain.Day;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static java.util.Collections.singletonList;

@ExtendWith(MockitoExtension.class)
class LessonMapperTest {
    @Mock
    private static ResultSet resultSet;
    private static CourseDaoPostgres courseDAO;
    private static GroupDaoPostgres groupDao;
    private static TeacherDaoPostgres teacherDao;
    private static LessonMapper lessonMapper;
    private static ApplicationContext context;
    private static Group group = new Group(4, "C-Name");
    private static List<Group> groups = singletonList(group);
    private static Course course = new Course(2, "Name", "Desc.");
    private static Teacher teacher = new Teacher(3, "Name", "LastName", course);
    private static Lesson expectedLesson = new Lesson(1, course, teacher, 10, Day.MONDAY,
            new Time(9, 30, 0), LessonsType.LECTURE);

    @BeforeAll
    public static void setUp() {
        context = new AnnotationConfigApplicationContext(MappersTestConfig.class);
        lessonMapper = context.getBean(LessonMapper.class);
        courseDAO = context.getBean(CourseDaoPostgres.class);
        groupDao = context.getBean(GroupDaoPostgres.class);
        teacherDao = context.getBean(TeacherDaoPostgres.class);
        expectedLesson.setGroups(groups);
    }

    @Test
    public void shouldReturnStudentWithCorrectSettings() throws SQLException {
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getInt("course_id")).thenReturn(2);
        when(resultSet.getInt("teacher_id")).thenReturn(3);
        when(resultSet.getInt("classroom")).thenReturn(10);
        when(resultSet.getString("day")).thenReturn(Day.MONDAY.toString());
        when(resultSet.getTime("time")).thenReturn(new Time(9, 30, 0));
        when(resultSet.getString("type")).thenReturn(LessonsType.LECTURE.toString());
        when(courseDAO.getById(2)).thenReturn(course);
        when(teacherDao.getById(3)).thenReturn(teacher);
        when(groupDao.getByLesson(expectedLesson)).thenReturn(groups);

        Lesson lesson = lessonMapper.mapRow(resultSet, 1);

        assertEquals(expectedLesson, lesson);
    }
}