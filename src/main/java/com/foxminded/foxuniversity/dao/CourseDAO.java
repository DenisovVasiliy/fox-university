package com.foxminded.foxuniversity.dao;

import com.foxminded.foxuniversity.dao.infra.QueriesConstants;
import com.foxminded.foxuniversity.dao.mappers.CourseMapper;
import com.foxminded.foxuniversity.domain.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@PropertySource("classpath:queries.properties")
public class CourseDAO {

    @Autowired
    private Environment environment;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Course> getAll() {
        return jdbcTemplate.query(environment.getProperty(QueriesConstants.GET_ALL_COURSES), new CourseMapper());
    }

    public Course getById(int id) {
        return jdbcTemplate.queryForObject(
                environment.getProperty(QueriesConstants.GET_COURSE_BY_ID), new Object[] {id}, new CourseMapper());
    }
}
