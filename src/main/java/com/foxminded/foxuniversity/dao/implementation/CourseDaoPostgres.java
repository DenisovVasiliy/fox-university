package com.foxminded.foxuniversity.dao.implementation;

import com.foxminded.foxuniversity.dao.CourseDao;
import com.foxminded.foxuniversity.dao.exceptions.EntityNotFoundException;
import com.foxminded.foxuniversity.dao.exceptions.QueryNotExecuteException;
import com.foxminded.foxuniversity.dao.mappers.CourseMapper;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Group;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.foxminded.foxuniversity.dao.exceptions.ExceptionsMessageConstants.*;
import static java.lang.String.format;

@Repository
@PropertySource("classpath:queries.properties")
@Slf4j
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

    private String entity = "Course";

    @Autowired
    public CourseDaoPostgres(JdbcTemplate jdbcTemplate, SimpleJdbcInsert jdbcInsert, CourseMapper courseMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = jdbcInsert.withTableName("courses").usingGeneratedKeyColumns("id");
        this.courseMapper = courseMapper;
    }

    @Override
    public List<Course> getAll() {
        log.debug("getAll()");
        List<Course> courses;
        try {
            courses = jdbcTemplate.query(getAll, courseMapper);
        } catch (DataAccessException e) {
            String msg = format(UNABLE_GET_ALL, entity);
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        log.trace("Found {} courses", courses.size());
        return courses;
    }

    @Override
    public Course getById(int id) {
        log.debug("getById({})", id);
        Course course;
        try {
            course = jdbcTemplate.queryForObject(getById, new Object[]{id}, courseMapper);
        } catch (EmptyResultDataAccessException e) {
            String msg = format(ENTITY_NOT_FOUND, entity, id);
            log.warn(msg);
            throw new EntityNotFoundException(msg);
        } catch (DataAccessException e) {
            String msg = format(UNABLE_GET_BY_ID, entity, id);
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        log.trace("Found {}", course);
        return course;
    }

    @Override
    public List<Course> getByGroup(Group group) {
        log.debug("getByGroup({})", group);
        List<Course> courses;
        try {
            courses = jdbcTemplate.query(getByGroup, new Object[]{group.getId()}, courseMapper);
        } catch (DataAccessException e) {
            String msg = format(UNABLE_GET_BY_ENTITY, entity, group.toString());
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        log.trace("Found {} courses", courses.size());
        return courses;
    }

    @Override
    public boolean delete(Course course) {
        log.debug("delete({})", course);
        int counter;
        try {
            counter = jdbcTemplate.update(delete, course.getId());
        } catch (DataAccessException e) {
            String msg = format(UNABLE_DELETE, course.toString());
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        log.trace("Deleted '{}' {}", counter, course);
        return counter > 0;
    }

    @Override
    public void save(Course course) {
        log.debug("save({})", course);
        try {
            SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(course);
            Number id = jdbcInsert.executeAndReturnKey(parameterSource);
            course.setId(id.intValue());
        } catch (DataAccessException e) {
            String msg = format(UNABLE_SAVE, course.toString());
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        log.trace("{} saved.", course);
    }

    @Override
    public boolean update(Course course) {
        log.debug("update({})", course);
        int counter;
        try {
            counter = jdbcTemplate.update(update, course.getName(), course.getDescription(), course.getId());
        } catch (DataAccessException e) {
            String msg = format(UNABLE_UPDATE, course.toString());
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        log.trace("Updated '{}' {}", counter, course);
        return counter > 0;
    }
}
