package com.foxminded.foxuniversity.dao.implementation;

import com.foxminded.foxuniversity.dao.LessonDao;
import com.foxminded.foxuniversity.dao.exceptions.EntityNotFoundException;
import com.foxminded.foxuniversity.dao.exceptions.QueryNotExecuteException;
import com.foxminded.foxuniversity.dao.mappers.LessonMapper;
import com.foxminded.foxuniversity.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.foxminded.foxuniversity.dao.exceptions.ExceptionsMessageConstants.*;
import static java.lang.String.format;
import static java.util.stream.IntStream.of;

@Repository
@PropertySource("classpath:queries.properties")
@Slf4j
public class LessonDaoPostgres implements LessonDao {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    private LessonMapper lessonMapper;

    @Value("${lesson.getAll}")
    private String getAll;
    @Value("${lesson.getById}")
    private String getById;
    @Value("${lesson.assignGroups}")
    private String assignGroups;
    @Value("${lesson.getByCourse}")
    private String getByCourse;
    @Value("${lesson.getByGroup}")
    private String getByGroup;
    @Value("${lesson.getByStudent}")
    private String getByStudent;
    @Value("${lesson.getByTeacher}")
    private String getByTeacher;
    @Value("${lesson.update}")
    private String update;
    @Value("${lesson.delete}")
    private String delete;
    @Value("${lesson.deleteGroup}")
    private String deleteGroup;

    private String entity = "Lesson";

    @Autowired
    public LessonDaoPostgres(JdbcTemplate jdbcTemplate, SimpleJdbcInsert jdbcInsert, LessonMapper lessonMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = jdbcInsert.withTableName("lessons").usingGeneratedKeyColumns("id");
        this.lessonMapper = lessonMapper;
    }

    @Override
    public List<Lesson> getAll() {
        log.debug("getAll()");
        List<Lesson> lessons;
        try {
            lessons = jdbcTemplate.query(getAll, lessonMapper);
        } catch (
                DataAccessException e) {
            String msg = format(UNABLE_GET_ALL, entity);
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        log.trace("Found {} {}s", lessons.size(), entity);
        return lessons;
    }

    @Override
    public Lesson getById(int id) {
        log.debug("getById({})", id);
        Lesson lesson;
        try {
            lesson = jdbcTemplate.queryForObject(getById, new Object[]{id}, lessonMapper);
        } catch (EmptyResultDataAccessException e) {
            String msg = format(ENTITY_NOT_FOUND, entity, id);
            log.warn(msg);
            throw new EntityNotFoundException(msg);
        } catch (DataAccessException e) {
            String msg = format(UNABLE_GET_BY_ID, entity, id);
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        log.trace("Found {}", lesson);
        return lesson;
    }

    @Override
    public List<Lesson> getByCourse(Course course) {
        log.debug("getByCourse({})", course);
        List<Lesson> lessons;
        try {
            lessons = jdbcTemplate.query(getByCourse, new Object[]{course.getId()}, lessonMapper);
        } catch (DataAccessException e) {
            String msg = format(UNABLE_GET_BY_ENTITY, entity, course);
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        log.trace("Found {} {}s", lessons.size(), entity);
        return lessons;
    }

    @Override
    public List<Lesson> getByGroup(Group group) {
        log.debug("getByGroup({})", group);
        List<Lesson> lessons;
        try {
            lessons = jdbcTemplate.query(getByGroup, new Object[]{group.getId()}, lessonMapper);
        } catch (DataAccessException e) {
            String msg = format(UNABLE_GET_BY_ENTITY, entity, group);
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        log.trace("Found {} {}s", lessons.size(), entity);
        return lessons;
    }

    @Override
    public List<Lesson> getByStudent(Student student) {
        log.debug("getByStudent({})", student);
        List<Lesson> lessons = new ArrayList<>();
        try {
            if (student.getGroup() != null) {
                lessons = jdbcTemplate.query(getByStudent, new Object[]{student.getGroup().getId()}, lessonMapper);
            }
        } catch (DataAccessException e) {
            String msg = format(UNABLE_GET_BY_ENTITY, entity, student);
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        log.trace("Found {} {}s", lessons.size(), entity);
        return lessons;
    }

    @Override
    public List<Lesson> getByTeacher(Teacher teacher) {
        log.debug("getByTeacher({})", teacher);
        List<Lesson> lessons;
        try {
            lessons = jdbcTemplate.query(getByTeacher, new Object[]{teacher.getId()}, lessonMapper);
        } catch (DataAccessException e) {
            String msg = format(UNABLE_GET_BY_ENTITY, entity, teacher);
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        log.trace("Found {} {}s", lessons.size(), entity);
        return lessons;
    }

    @Override
    public void save(Lesson lesson) {
        log.debug("save({})", lesson);
        try {
            SqlParameterSource parameterSource = new MapSqlParameterSource()
                    .addValue("course_id", lesson.getCourse().getId())
                    .addValue("teacher_id", lesson.getTeacher().getId())
                    .addValue("classroom", lesson.getClassroom())
                    .addValue("day", lesson.getDay().toString())
                    .addValue("time", lesson.getStartTime())
                    .addValue("type", lesson.getType().toString());
            Number generatedId = jdbcInsert.executeAndReturnKey(parameterSource);
            lesson.setId(generatedId.intValue());
            if (lesson.getGroups() != null) {
                assignGroups(lesson, lesson.getGroups());
            }
        } catch (DataAccessException e) {
            String msg = format(UNABLE_SAVE, lesson);
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        log.trace("{} saved.", lesson);
    }

    @Override
    public boolean assignGroups(Lesson lesson, List<Group> groups) {
        log.debug("assignToCourses({}, {})", lesson, groups);
        try {
            int[] result = jdbcTemplate.batchUpdate(assignGroups, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    preparedStatement.setInt(1, lesson.getId());
                    preparedStatement.setInt(2, groups.get(i).getId());
                }

                @Override
                public int getBatchSize() {
                    return groups.size();
                }
            });
            log.trace("{} assigned to '{}' {}", lesson, of(result).sum(), "groups");
            return of(result).sum() == groups.size();
        } catch (DataAccessException e) {
            String msg = format(UNABLE_ASSIGN, lesson, groups);
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
    }

    @Override
    public boolean update(Lesson lesson) {
        log.debug("update({})", lesson);
        int counter;
        try {
            counter = jdbcTemplate.update(update,
                    lesson.getCourse().getId(), lesson.getTeacher().getId(), lesson.getClassroom(),
                    lesson.getDay().toString(), lesson.getStartTime(), lesson.getType().toString(), lesson.getId());
        } catch (DataAccessException e) {
            String msg = format(UNABLE_UPDATE, lesson);
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        log.trace("Updated '{}' {}", counter, lesson);
        return counter > 0;
    }

    @Override
    public boolean delete(Lesson lesson) {
        log.debug("delete({})", lesson);
        int counter;
        try {
            counter = jdbcTemplate.update(delete, lesson.getId());
        } catch (DataAccessException e) {
            String msg = format(UNABLE_DELETE, lesson);
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        log.trace("Deleted '{}' {}", counter, lesson);
        return counter > 0;
    }

    @Override
    public boolean deleteGroup(Lesson lesson, Group group) {
        log.debug("deleteGroup({}, {})", lesson, group);
        int counter;
        try {
            counter = jdbcTemplate.update(deleteGroup, lesson.getId(), group.getId());
        } catch (DataAccessException e) {
            String msg = format(UNABLE_DELETE_FROM, group, lesson);
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        log.trace("{} deleted from '{}' {}.", group, counter, lesson);
        return counter > 0;
    }
}
