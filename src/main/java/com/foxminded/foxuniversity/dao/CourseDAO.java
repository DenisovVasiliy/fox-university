package com.foxminded.foxuniversity.dao;

import com.foxminded.foxuniversity.dao.infra.DataSource;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Student;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CourseDAO {
    DataSource dataSource = DataSource.getInstance();
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource.getDataSource());

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
