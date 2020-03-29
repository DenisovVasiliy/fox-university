package com.foxminded.foxuniversity.dao;

import com.foxminded.foxuniversity.dao.infra.QueriesConstants;
import com.foxminded.foxuniversity.dao.mappers.CourseMapper;
import com.foxminded.foxuniversity.domain.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@PropertySource("classpath:queries.properties")
public class CourseDAO {

    @Autowired
    private Environment environment;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SimpleJdbcInsert jdbcInsert;

    public List<Course> getAll() {
        return jdbcTemplate.query(environment.getProperty(QueriesConstants.GET_ALL_COURSES), new CourseMapper());
    }

    public Course getById(int id) {
        return jdbcTemplate.queryForObject(
                environment.getProperty(QueriesConstants.GET_COURSE_BY_ID), new Object[]{id}, new CourseMapper());
    }

    public boolean delete(Course course) {
        return jdbcTemplate.update(environment.getProperty(QueriesConstants.DELETE_COURSE), course.getId()) > 0;
    }

    public boolean save(Course course) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(course);
        Number id = jdbcInsert.withTableName("courses").usingGeneratedKeyColumns("id")
                .executeAndReturnKey(parameterSource);
        if (id != null) {
            course.setId(id.intValue());
            if(course.getLessons() != null) {
                //call save course's lessons
            }
            return true;
        }
        return false;
    }
}
