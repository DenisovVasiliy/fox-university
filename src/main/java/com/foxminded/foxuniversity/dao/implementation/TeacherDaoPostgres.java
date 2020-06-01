package com.foxminded.foxuniversity.dao.implementation;

import com.foxminded.foxuniversity.dao.TeacherDao;
import com.foxminded.foxuniversity.dao.exceptions.EntityNotFoundException;
import com.foxminded.foxuniversity.dao.exceptions.QueryNotExecuteException;
import com.foxminded.foxuniversity.dao.exceptions.QueryRestrictedException;
import com.foxminded.foxuniversity.dao.mappers.TeacherMapper;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Teacher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.foxminded.foxuniversity.dao.exceptions.ExceptionsMessageConstants.*;
import static java.lang.String.format;

@Repository
@PropertySource("classpath:queries.properties")
@Slf4j
public class TeacherDaoPostgres implements TeacherDao {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    private TeacherMapper teacherMapper;

    @Value("${teacher.getAll}")
    private String getAll;
    @Value("${teacher.getById}")
    private String getById;
    @Value("${teacher.getByCourse}")
    private String getByCourse;
    @Value("${teacher.update}")
    private String update;
    @Value("${teacher.updateWithCourse}")
    private String updateWithCourse;
    @Value("${teacher.delete}")
    private String delete;

    private String entity = "Teacher";

    @Autowired
    public TeacherDaoPostgres(JdbcTemplate jdbcTemplate, SimpleJdbcInsert jdbcInsert, TeacherMapper teacherMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = jdbcInsert.withTableName("teachers").usingGeneratedKeyColumns("id");
        this.teacherMapper = teacherMapper;
    }

    @Override
    public List<Teacher> getAll() {
        log.debug("getAll()");
        List<Teacher> teachers;
        try {
            teachers = jdbcTemplate.query(getAll, teacherMapper);
        } catch (DataAccessException e) {
            String msg = format(UNABLE_GET_ALL, entity);
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        log.trace("Found {} {}s", teachers.size(), entity);
        return teachers;
    }

    @Override
    public Teacher getById(int id) {
        log.debug("getById({})", id);
        Teacher teacher;
        try {
            teacher = jdbcTemplate.queryForObject(getById, new Object[]{id}, teacherMapper);
        } catch (EmptyResultDataAccessException e) {
            String msg = format(ENTITY_NOT_FOUND, entity, id);
            log.warn(msg);
            throw new EntityNotFoundException(msg);
        } catch (DataAccessException e) {
            String msg = format(UNABLE_GET_BY_ID, entity, id);
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        log.trace("Found {}", teacher);
        return teacher;
    }

    @Override
    public List<Teacher> getByCourse(Course course) {
        log.debug("getByCourse({})", course);
        List<Teacher> teachers;
        try {
            teachers = jdbcTemplate.query(getByCourse, new Object[]{course.getId()}, teacherMapper);
        } catch (DataAccessException e) {
            String msg = format(UNABLE_GET_BY_ENTITY, entity, course);
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        log.trace("Found {} {}s", teachers.size(), entity);
        return teachers;
    }

    @Override
    public boolean delete(Teacher teacher) {
        log.debug("delete({})", teacher);
        int counter;
        try {
            counter = jdbcTemplate.update(delete, teacher.getId());
        } catch (DataIntegrityViolationException e) {
            String msg = format(DELETION_RESTRICTED, teacher, "lessons");
            log.warn(msg);
            throw new QueryRestrictedException(msg, e);
        } catch (DataAccessException e) {
            String msg = format(UNABLE_DELETE, teacher);
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        log.trace("Deleted '{}' {}", counter, teacher);
        return counter > 0;
    }

    @Override
    public boolean update(Teacher teacher) {
        log.debug("update({})", teacher);
        int counter;
        try {
            counter = jdbcTemplate.update(update,
                    teacher.getFirstName(), teacher.getLastName(), teacher.getId());
        } catch (DataAccessException e) {
            String msg = format(UNABLE_UPDATE, teacher);
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        log.trace("Updated '{}' {}", counter, teacher);
        return counter > 0;
    }

    @Override
    public boolean updateWithCourse(Teacher teacher) {
        log.debug("updateWithCourse({})", teacher);
        int counter;
        try {
            counter = jdbcTemplate.update(updateWithCourse,
                    teacher.getFirstName(), teacher.getLastName(), teacher.getCourse().getId(), teacher.getId());
        } catch (DataAccessException e) {
            String msg = format(UNABLE_UPDATE, teacher);
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        log.trace("Updated '{}' {}", counter, teacher);
        return counter > 0;
    }

    @Override
    public void save(Teacher teacher) {
        log.debug("save({})", teacher);
        try {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("first_name", teacher.getFirstName())
                    .addValue("last_name", teacher.getLastName())
                    .addValue("course_id", teacher.getCourse().getId());
            Number generatedId = jdbcInsert.executeAndReturnKey(parameters);
            teacher.setId(generatedId.intValue());
        } catch (DataAccessException e) {
            String msg = format(UNABLE_SAVE, teacher);
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        log.trace("{} saved.", teacher);
    }
}
