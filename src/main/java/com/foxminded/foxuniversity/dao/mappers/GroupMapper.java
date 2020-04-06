package com.foxminded.foxuniversity.dao.mappers;

import com.foxminded.foxuniversity.domain.Group;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GroupMapper implements RowMapper<Group> {

    @Override
    public Group mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Group(resultSet.getInt("id"), resultSet.getString("name"));
    }
}
