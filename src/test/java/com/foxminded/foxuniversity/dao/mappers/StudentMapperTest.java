package com.foxminded.foxuniversity.dao.mappers;

import com.foxminded.foxuniversity.AppConfig;
import com.foxminded.foxuniversity.dao.GroupDao;
import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.domain.Student;
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
class StudentMapperTest {
    @Mock
    private static ResultSet resultSet;
    @Mock
    private static GroupDao groupDao;
    @InjectMocks
    private static StudentMapper studentMapper;
    private static ApplicationContext context;
    private static Group group = new Group(1, "C-Name");
    private static Student expectedStudent = new Student(1, "Name", "LastName");

    @BeforeAll
    public static void setUp() {
        context = new AnnotationConfigApplicationContext(AppConfig.class);
        studentMapper = context.getBean(StudentMapper.class);
    }

    @Test
    public void shouldReturnStudentWithCorrectSettings() throws SQLException {
        expectedStudent.setGroup(group);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("first_name")).thenReturn("Name");
        when(resultSet.getString("last_name")).thenReturn("LastName");
        when(resultSet.getInt("group_id")).thenReturn(1);
        when(groupDao.getById(1)).thenReturn(group);

        Student student = studentMapper.mapRow(resultSet, 1);

        assertEquals(expectedStudent, student);
    }

    @Test
    public void shouldReturnStudentWithCorrectSettingsWhenGroupIsNull() throws SQLException {
        expectedStudent.setGroup(null);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("first_name")).thenReturn("Name");
        when(resultSet.getString("last_name")).thenReturn("LastName");
        when(resultSet.getInt("group_id")).thenReturn(0);

        Student student = studentMapper.mapRow(resultSet, 1);

        assertEquals(expectedStudent, student);
    }
}