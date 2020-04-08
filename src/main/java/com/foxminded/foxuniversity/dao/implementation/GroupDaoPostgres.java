package com.foxminded.foxuniversity.dao.implementation;

import com.foxminded.foxuniversity.dao.GroupDao;
import com.foxminded.foxuniversity.dao.mappers.GroupMapper;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.domain.Lesson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static java.util.stream.IntStream.of;

@Repository
@PropertySource("classpath:queries.properties")
public class GroupDaoPostgres implements GroupDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SimpleJdbcInsert jdbcInsert;
    @Autowired
    private GroupMapper groupMapper;

    @Value("${group.getAll}")
    private String getAll;
    @Value("${group.getById}")
    private String getById;
    @Value("${group.getByLesson}")
    private String getByLesson;
    @Value("${group.update}")
    private String update;
    @Value("${group.delete}")
    private String delete;
    @Value("${group.deleteFromCourse}")
    private String deleteFromCourse;
    @Value("${group.assignToCourse}")
    private String assignToCourse;

    @Override
    public List<Group> getAll() {
        return jdbcTemplate.query(getAll, groupMapper);
    }

    @Override
    public Group getById(int id) {
        return jdbcTemplate.queryForObject(getById, new Object[]{id}, groupMapper);
    }

    @Override
    public List<Group> getByLesson(Lesson lesson) {
        return jdbcTemplate.query(getByLesson, new Object[]{lesson.getId()}, groupMapper);
    }

    @Override
    public boolean delete(Group group) {
        return jdbcTemplate.update(delete, group.getId()) > 0;
    }

    @Override
    public boolean update(Group group) {
        return jdbcTemplate.update(update, group.getName()) > 0;
    }

    @Override
    public boolean assignToCourses(Group group, List<Course> courses) {
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
        return of(result).sum() == courses.size();
    }

    @Override
    public boolean save(Group group) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("name", group.getName());
        Number generatedId = jdbcInsert.withTableName("groups").usingGeneratedKeyColumns("id")
                .executeAndReturnKey(parameterSource);
        if (generatedId != null) {
            group.setId(generatedId.intValue());
            if (group.getCourses() != null) {
                return assignToCourses(group, group.getCourses());
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteFromCourse(Group group, Course course) {
        return jdbcTemplate.update(deleteFromCourse, group.getId(), course.getId()) > 0;
    }

    @Override
    public boolean deleteFromCourse(Group group, List<Course> courses) {
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
        return of(result).sum() == courses.size();
    }
}
