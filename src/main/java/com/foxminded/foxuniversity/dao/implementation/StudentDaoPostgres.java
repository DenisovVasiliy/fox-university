package com.foxminded.foxuniversity.dao.implementation;

import com.foxminded.foxuniversity.dao.StudentDao;
import com.foxminded.foxuniversity.dao.exceptions.EntityNotFoundException;
import com.foxminded.foxuniversity.dao.exceptions.QueryNotExecuteException;
import com.foxminded.foxuniversity.dao.exceptions.QueryRestrictedException;
import com.foxminded.foxuniversity.dao.mappers.StudentMapper;
import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.domain.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
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
public class StudentDaoPostgres implements StudentDao {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    private StudentMapper studentMapper;

    @Value("${student.getAll}")
    private String getAll;
    @Value("${student.getById}")
    private String getById;
    @Value("${student.getByGroup}")
    private String getByGroup;
    @Value("${student.delete}")
    private String delete;
    @Value("${student.assignToGroup}")
    private String assignToGroup;
    @Value("${student.update}")
    private String update;
    @Value("${student.updateAssignment}")
    private String updateAssignment;
    @Value("${student.deleteAssignment}")
    private String deleteAssignment;

    private String entity = "Student";

    @Autowired
    public StudentDaoPostgres(JdbcTemplate jdbcTemplate, SimpleJdbcInsert jdbcInsert, StudentMapper studentMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = jdbcInsert.withTableName("students").usingGeneratedKeyColumns("id");
        this.studentMapper = studentMapper;
    }

    @Override
    public List<Student> getAll() {
        log.debug("getAll()");
        List<Student> students;
        try {
            students = jdbcTemplate.query(getAll, studentMapper);
        } catch (DataAccessException e) {
            String msg = format(UNABLE_GET_ALL, entity);
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        log.trace("Found {} {}s", students.size(), entity);
        return students;
    }

    @Override
    public Student getById(int id) {
        log.debug("getById({})", id);
        Student student;
        try {
            student = jdbcTemplate.queryForObject(getById, new Object[]{id}, studentMapper);
        } catch (EmptyResultDataAccessException e) {
            String msg = format(ENTITY_NOT_FOUND, entity, id);
            log.warn(msg);
            throw new EntityNotFoundException(msg);
        } catch (DataAccessException e) {
            String msg = format(UNABLE_GET_BY_ID, entity, id);
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        log.trace("Found {}", student);
        return student;
    }

    @Override
    public List<Student> getByGroup(Group group) {
        log.debug("getByLesson({})", group);
        List<Student> students;
        try {
            students = jdbcTemplate.query(getByGroup, new Object[]{group.getId()}, studentMapper);
        } catch (DataAccessException e) {
            String msg = format(UNABLE_GET_BY_ENTITY, entity, group);
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        log.trace("Found {} {}s", students.size(), entity);
        return students;
    }

    @Override
    public void save(Student student) {
        log.debug("save({})", student);
        try {
            SqlParameterSource parameterSource = new MapSqlParameterSource()
                    .addValue("first_name", student.getFirstName())
                    .addValue("last_name", student.getLastName());
            Number generatedId = jdbcInsert.executeAndReturnKey(parameterSource);
            student.setId(generatedId.intValue());
            if (student.getGroup() != null) {
                assignToGroup(student, student.getGroup());
            }
        } catch (DataAccessException e) {
            String msg = format(UNABLE_SAVE, student);
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        log.trace("{} saved.", student);
    }

    @Override
    public boolean update(Student student) {
        log.debug("update({})", student);
        int updatedRows;
        try {
            updatedRows = jdbcTemplate.update(update, student.getFirstName(), student.getLastName(), student.getId());
            log.trace("Updated '{}' {}", updatedRows, student);
            if (updatedRows > 0 && student.getGroup() != null) {
                if (updateAssignment(student)) {
                    return true;
                } else return assignToGroup(student, student.getGroup());
            }
        } catch (DataAccessException e) {
            String msg = format(UNABLE_UPDATE, student);
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        return updatedRows > 0;
    }

    @Override
    public boolean updateAssignment(Student student) {
        log.debug("updateAssignment({})", student);
        int updatedRows;
        try {
            updatedRows = jdbcTemplate.update(updateAssignment, student.getGroup().getId(), student.getId());
        } catch (DataAccessException e) {
            String msg = format(UNABLE_UPDATE_ASSIGNMENT, student, student.getGroup());
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        log.trace("Updated '{}' assignment(s) of the {}", updatedRows, student);
        return updatedRows > 0;
    }

    @Override
    public boolean assignToGroup(Student student, Group group) {
        log.debug("assignToGroup({}, {})", student, group);
        int counter;
        try {
            counter = jdbcTemplate.update(assignToGroup, student.getId(), group.getId());
        } catch (DuplicateKeyException e) {
            String msg = QUERY_RESTRICTED_DUPLICATE_KEY;
            log.warn(msg);
            throw new QueryRestrictedException(msg, e);
        } catch (DataIntegrityViolationException e) {
            String msg = format(QUERY_RESTRICTED_NO_SUCH_ID, student, group);
            log.warn(msg);
            throw new QueryRestrictedException(msg, e);
        } catch (DataAccessException e) {
            String msg = format(UNABLE_ASSIGN, student, group);
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        if (counter > 0) {
            log.trace("{} assigned to {}", student, group);
            return true;
        } else {
            log.trace("{} wasn't assigned to {}", student, group);
            return false;
        }
    }

    @Override
    public boolean delete(Student student) {
        log.debug("delete({})", student);
        int counter;
        try {
            counter = jdbcTemplate.update(delete, student.getId());
        } catch (DataAccessException e) {
            String msg = format(UNABLE_DELETE, student);
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        log.trace("Deleted '{}' {}", counter, student);
        return counter > 0;
    }

    @Override
    public boolean deleteAssignment(Student student) {
        log.debug("deleteAssignment({})", student);
        int counter;
        try {
            counter = jdbcTemplate.update(deleteAssignment, student.getId());
        } catch (DataAccessException e) {
            String msg = format(UNABLE_DELETE_FROM, student, "group");
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        if (counter > 0) {
            log.trace("{} deleted from his {}", student, "group");
            return true;
        } else {
            log.trace("{} wasn't deleted from his {}", student, "group");
            return false;
        }
    }
}
