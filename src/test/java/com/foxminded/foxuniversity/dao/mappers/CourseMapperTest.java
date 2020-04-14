package com.foxminded.foxuniversity.dao.mappers;


import com.foxminded.foxuniversity.AppConfig;
import com.foxminded.foxuniversity.domain.Course;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CourseMapperTest {
    @Mock
    private static ResultSet resultSet;
    private static CourseMapper courseMapper;
    private static ApplicationContext context;
    private Course expectedCourse = new Course(1, "Name", "Desc.");

    @BeforeAll
    public static void setUp() {
        context = new AnnotationConfigApplicationContext(AppConfig.class);
        courseMapper = context.getBean(CourseMapper.class);
    }

    @Test
    public void shouldReturnCourseWithCorrectSettings() throws SQLException {
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("Name");
        when(resultSet.getString("description")).thenReturn("Desc.");
        Course course = courseMapper.mapRow(resultSet, 1);

        assertEquals(expectedCourse, course);
    }

}