package com.foxminded.foxuniversity.dao.implementation;

import com.foxminded.foxuniversity.dao.CourseDao;
import com.foxminded.foxuniversity.dao.mappers.CourseMapper;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@PropertySource("classpath:queries.properties")
public class CourseDaoPostgres implements CourseDao {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
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

    @Autowired
    public CourseDaoPostgres(JdbcTemplate jdbcTemplate, SimpleJdbcInsert jdbcInsert, CourseMapper courseMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = jdbcInsert.withTableName("courses").usingGeneratedKeyColumns("id");
        this.courseMapper = courseMapper;
    }

    @Override
    public List<Course> getAll() {
        return jdbcTemplate.query(getAll, courseMapper);
    }

    @Override
    public Course getById(int id) {
        return jdbcTemplate.queryForObject(getById, new Object[]{id}, courseMapper);
    }

    @Override
    public List<Course> getByGroup(Group group) {
        return jdbcTemplate.query(getByGroup, new Object[]{group.getId()}, courseMapper);
    }

    @Override
    public boolean delete(Course course) {
        return jdbcTemplate.update(delete, course.getId()) > 0;
    }

    @Override
    public void save(Course course) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(course);
        Number id = jdbcInsert.executeAndReturnKey(parameterSource);
        course.setId(id.intValue());
    }

    @Override
    public boolean update(Course course) {
        return jdbcTemplate.update(update, course.getName(), course.getDescription(), course.getId()) > 0;
    }
}
