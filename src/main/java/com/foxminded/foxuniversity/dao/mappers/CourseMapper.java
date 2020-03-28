package com.foxminded.foxuniversity.dao.mappers;

import com.foxminded.foxuniversity.domain.Course;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class CourseMapper implements RowMapper<Course> {

    public Course mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Course(resultSet.getInt("id"), resultSet.getString("name"),
                resultSet.getString("description"));
    }
}
