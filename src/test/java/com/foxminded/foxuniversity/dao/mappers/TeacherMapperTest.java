package com.foxminded.foxuniversity.dao.mappers;

import com.foxminded.foxuniversity.AppConfig;
import com.foxminded.foxuniversity.dao.CourseDao;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Teacher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TeacherMapperTest {
    @Mock
    private static ResultSet resultSet;
    @Mock
    private static CourseDao courseDao;
    @InjectMocks
    private static TeacherMapper teacherMapper;
    private static ApplicationContext context;
    private Course course = new Course(1, "C-Name", "Desc.");
    private Teacher expectedTeacher = new Teacher(1, "Name", "LastName", course);

    @BeforeAll
    public static void setUp() {
        context = new AnnotationConfigApplicationContext(AppConfig.class);
        teacherMapper = context.getBean(TeacherMapper.class);
    }

    @Test
    public void shouldReturnTeacherWithCorrectSettings() throws SQLException {
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("first_name")).thenReturn("Name");
        when(resultSet.getString("last_name")).thenReturn("LastName");
        when(resultSet.getInt("course_id")).thenReturn(1);
        when(courseDao.getById(1)).thenReturn(course);
        Teacher teacher = teacherMapper.mapRow(resultSet, 1);

        assertEquals(expectedTeacher, teacher);
    }
}