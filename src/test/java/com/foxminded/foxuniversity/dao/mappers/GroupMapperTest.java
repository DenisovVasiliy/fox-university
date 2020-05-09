package com.foxminded.foxuniversity.dao.mappers;

import com.foxminded.foxuniversity.dao.DaoTestConfig;
import com.foxminded.foxuniversity.domain.Group;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class GroupMapperTest {
    @Mock
    private ResultSet resultSet;
    @Autowired
    private GroupMapper groupMapper;
    private Group expectedGroup = new Group(1, "Name");

    @Test
    public void shouldReturnGroupWithCorrectSettings() throws SQLException {
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("Name");
        Group group = groupMapper.mapRow(resultSet, 1);

        assertEquals(expectedGroup, group);
    }
}