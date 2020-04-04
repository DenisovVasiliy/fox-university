package com.foxminded.foxuniversity.dao;

import com.foxminded.foxuniversity.dao.mappers.CourseMapper;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Autowired
    private CourseMapper courseMapper;

    @Value("${course.getAll}")
    private String getAll;
    @Value("${course.getById}")
    private String getById;
    @Value("${course.getByGroup}")
    private String getByGroup;
    @Value("${course.update}")
    private String update;
    @Value("${course.delete}")
    private String delete;

    public List<Course> getAll() {
        return jdbcTemplate.query(getAll, courseMapper);
    }

    public Course getById(int id) {
        return jdbcTemplate.queryForObject(getById, new Object[]{id}, courseMapper);
    }

    public List<Course> getByGroup(Group group) {
        return jdbcTemplate.query(getByGroup, new Object[]{group.getId()}, courseMapper);
    }

    public boolean delete(Course course) {
        return jdbcTemplate.update(delete, course.getId()) > 0;
    }

    public boolean saveCourse(Course course) {
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

    public boolean update(Course course) {
        return jdbcTemplate.update(update, course.getName(), course.getDescription(), course.getId()) > 0;
    }
}
