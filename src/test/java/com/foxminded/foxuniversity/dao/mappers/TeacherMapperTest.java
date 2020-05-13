package com.foxminded.foxuniversity.dao.mappers;

import com.foxminded.foxuniversity.dao.DaoTestConfig;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Teacher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DaoTestConfig.class})
class TeacherMapperTest {
    @Mock
    private ResultSet resultSet;

    @InjectMocks
    @Autowired
    private TeacherMapper teacherMapper;
    private Course course = new Course(1);
    private Teacher expectedTeacher = new Teacher(1, "Name", "LastName", course);

    @Test
    public void shouldReturnTeacherWithCorrectSettings() throws SQLException {
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("first_name")).thenReturn("Name");
        when(resultSet.getString("last_name")).thenReturn("LastName");
        when(resultSet.getInt("course_id")).thenReturn(1);

        Teacher teacher = teacherMapper.mapRow(resultSet, 1);

        assertEquals(expectedTeacher, teacher);
    }
}