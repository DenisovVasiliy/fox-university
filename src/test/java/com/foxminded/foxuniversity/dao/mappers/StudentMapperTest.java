package com.foxminded.foxuniversity.dao.mappers;

import com.foxminded.foxuniversity.dao.DaoTestConfig;
import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.domain.Student;
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
class StudentMapperTest {
    @Mock
    private ResultSet resultSet;

    @InjectMocks
    @Autowired
    private StudentMapper studentMapper;
    private Group group = new Group(1);
    private Student expectedStudent = new Student(1, "Name", "LastName");

    @Test
    public void shouldReturnStudentWithCorrectSettings() throws SQLException {
        expectedStudent.setGroup(group);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("first_name")).thenReturn("Name");
        when(resultSet.getString("last_name")).thenReturn("LastName");
        when(resultSet.getInt("group_id")).thenReturn(1);

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