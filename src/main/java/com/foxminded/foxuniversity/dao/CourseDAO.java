package com.foxminded.foxuniversity.dao;

import com.foxminded.foxuniversity.domain.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CourseDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Course> getAll() {
        return jdbcTemplate.query("select * from courses;", new RowMapper<Course>() {
            @Override
            public Course mapRow(ResultSet resultSet, int i) throws SQLException {
                return new Course(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getString("description"));
            }
        });
    }
}
