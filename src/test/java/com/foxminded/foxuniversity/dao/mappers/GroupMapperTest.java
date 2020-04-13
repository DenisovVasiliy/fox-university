package com.foxminded.foxuniversity.dao.mappers;

import com.foxminded.foxuniversity.AppConfig;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Group;
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
class GroupMapperTest {
    @Mock
    private static ResultSet resultSet;
    private static GroupMapper groupMapper;
    private static ApplicationContext context;
    private Group expectedGroup = new Group(1, "Name");

    @BeforeAll
    public static void setUp() {
        context = new AnnotationConfigApplicationContext(AppConfig.class);
        groupMapper = context.getBean(GroupMapper.class);
    }

    @Test
    public void shouldReturnCourseWithCorrectSettings() throws SQLException {
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("Name");
        Group group = groupMapper.mapRow(resultSet, 1);

        assertEquals(expectedGroup, group);
    }
}