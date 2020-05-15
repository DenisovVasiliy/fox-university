package com.foxminded.foxuniversity.dao.implementation;

import com.foxminded.foxuniversity.dao.GroupDao;
import com.foxminded.foxuniversity.dao.exceptions.EntityNotFoundException;
import com.foxminded.foxuniversity.dao.exceptions.QueryNotExecuteException;
import com.foxminded.foxuniversity.dao.mappers.GroupMapper;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.domain.Lesson;
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
import java.util.List;

import static com.foxminded.foxuniversity.dao.exceptions.ExceptionsMessageConstants.*;
import static java.lang.String.format;
import static java.util.stream.IntStream.of;

@Repository
@PropertySource("classpath:queries.properties")
@Slf4j
public class GroupDaoPostgres implements GroupDao {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    private GroupMapper groupMapper;

    @Value("${group.getAll}")
    private String getAll;
    @Value("${group.getById}")
    private String getById;
    @Value("${group.getByLesson}")
    private String getByLesson;
    @Value("${group.getByCourse}")
    private String getByCourse;
    @Value("${group.update}")
    private String update;
    @Value("${group.delete}")
    private String delete;
    @Value("${group.deleteFromCourse}")
    private String deleteFromCourse;
    @Value("${group.assignToCourse}")
    private String assignToCourse;

    private String entity = "Group";

    @Autowired
    public GroupDaoPostgres(JdbcTemplate jdbcTemplate, SimpleJdbcInsert jdbcInsert, GroupMapper groupMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = jdbcInsert.withTableName("groups").usingGeneratedKeyColumns("id");
        this.groupMapper = groupMapper;
    }

    @Override
    public List<Group> getAll() {
        log.debug("getAll()");
        List<Group> groups;
        try {
            groups = jdbcTemplate.query(getAll, groupMapper);
        } catch (DataAccessException e) {
            String msg = format(UNABLE_GET_ALL, entity);
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        log.trace("Found {} {}s", groups.size(), entity);
        return groups;
    }

    @Override
    public Group getById(int id) {
        log.debug("getById({})", id);
        Group group;
        try {
            group = jdbcTemplate.queryForObject(getById, new Object[]{id}, groupMapper);
        } catch (EmptyResultDataAccessException e) {
            String msg = format(ENTITY_NOT_FOUND, entity, id);
            log.warn(msg);
            throw new EntityNotFoundException(msg);
        } catch (DataAccessException e) {
            String msg = format(UNABLE_GET_BY_ID, entity, id);
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        log.trace("Found {}", group);
        return group;
    }

    @Override
    public List<Group> getByLesson(Lesson lesson) {
        log.debug("getByLesson({})", lesson);
        List<Group> groups;
        try {
            groups = jdbcTemplate.query(getByLesson, new Object[]{lesson.getId()}, groupMapper);
        } catch (DataAccessException e) {
            String msg = format(UNABLE_GET_BY_ENTITY, entity, lesson);
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        log.trace("Found {} {}s", groups.size(), entity);
        return groups;
    }

    @Override
    public List<Group> getByCourse(Course course) {
        log.debug("getByCourse({})", course);
        List<Group> groups;
        try {
            groups = jdbcTemplate.query(getByCourse, new Object[]{course.getId()}, groupMapper);
        } catch (DataAccessException e) {
            String msg = format(UNABLE_GET_BY_ENTITY, entity, course);
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        log.trace("Found {} {}s", groups.size(), entity);
        return groups;
    }

    @Override
    public boolean delete(Group group) {
        log.debug("delete({})", group);
        int counter;
        try {
            counter = jdbcTemplate.update(delete, group.getId());
        } catch (DataAccessException e) {
            String msg = format(UNABLE_DELETE, group);
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        log.trace("Deleted '{}' {}", counter, group);
        return counter > 0;
    }

    @Override
    public boolean update(Group group) {
        log.debug("update({})", group);
        int counter;
        try {
            counter = jdbcTemplate.update(update, group.getName(), group.getId());
        } catch (DataAccessException e) {
            String msg = format(UNABLE_UPDATE, group);
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        log.trace("Updated '{}' {}", counter, group);
        return counter > 0;
    }

    @Override
    public boolean assignToCourses(Group group, List<Course> courses) {
        log.debug("assignToCourses({}, {})", group, courses);
        try {
            int[] result = jdbcTemplate.batchUpdate(assignToCourse, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    preparedStatement.setInt(1, group.getId());
                    preparedStatement.setInt(2, courses.get(i).getId());
                }

                @Override
                public int getBatchSize() {
                    return courses.size();
                }
            });
            log.trace("{} assigned to '{}' {}", group, of(result).sum(), "courses");
            return of(result).sum() == courses.size();
        } catch (DataAccessException e) {
            String msg = format(UNABLE_ASSIGN, group, courses);
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
    }

    @Override
    public void save(Group group) {
        log.debug("save({})", group);
        try {
            SqlParameterSource parameterSource = new MapSqlParameterSource()
                    .addValue("name", group.getName());
            Number generatedId = jdbcInsert.executeAndReturnKey(parameterSource);
            group.setId(generatedId.intValue());
        } catch (DataAccessException e) {
            String msg = format(UNABLE_SAVE, group);
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        log.trace("{} saved.", group);
    }

    @Override
    public boolean deleteFromCourse(Group group, Course course) {
        log.debug("deleteFromCourse({}, {})", group, course);
        int counter;
        try {
            counter = jdbcTemplate.update(deleteFromCourse, group.getId(), course.getId());
        } catch (DataAccessException e) {
            String msg = format(UNABLE_DELETE_FROM, group, course);
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
        log.trace("{} deleted from '{}' {}.", group, counter, course);
        return counter > 0;
    }

    @Override
    public boolean deleteFromCourse(Group group, List<Course> courses) {
        log.debug("deleteFromCourse({}, {})", group, courses);
        try {
            int[] result = jdbcTemplate.batchUpdate(deleteFromCourse, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    preparedStatement.setInt(1, group.getId());
                    preparedStatement.setInt(2, courses.get(i).getId());
                }

                @Override
                public int getBatchSize() {
                    return courses.size();
                }
            });
            log.trace("{} deleted from '{}' {}.", group, of(result).sum(), courses);
            return of(result).sum() == courses.size();
        } catch (DataAccessException e) {
            String msg = format(UNABLE_DELETE_FROM, group, courses);
            log.error(msg);
            throw new QueryNotExecuteException(msg, e);
        }
    }
}
