package com.foxminded.foxuniversity.dao.implementation;

import com.foxminded.foxuniversity.dao.LessonDao;
import com.foxminded.foxuniversity.dao.mappers.LessonMapper;
import com.foxminded.foxuniversity.domain.*;
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
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.IntStream.of;

@Repository
@PropertySource("classpath:queries.properties")
public class LessonDaoPostgres implements LessonDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SimpleJdbcInsert jdbcInsert;
    @Autowired
    private LessonMapper lessonMapper;

    @Value("${lesson.getAll}")
    private String getAll;
    @Value("${lesson.getById}")
    private String getById;
    @Value("${lesson.assignGroups}")
    private String assignGroups;
    @Value("${lesson.getByCourse}")
    private String getByCourse;
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

    @Override
    public List<Lesson> getAll() {
        return jdbcTemplate.query(getAll, lessonMapper);
    }

    @Override
    public Lesson getById(int id) {
        return jdbcTemplate.queryForObject(getById, new Object[]{id}, lessonMapper);
    }

    @Override
    public List<Lesson> getByCourse(Course course) {
        return jdbcTemplate.query(getByCourse, new Object[]{course.getId()}, lessonMapper);
    }

    @Override
    public List<Lesson> getByStudent(Student student) {
        if (student.getGroup() != null) {
            return jdbcTemplate.query(getByStudent, new Object[]{student.getGroup().getId()}, lessonMapper);
        }
        return new ArrayList<>();
    }

    @Override
    public List<Lesson> getByTeacher(Teacher teacher) {
        return jdbcTemplate.query(getByTeacher, new Object[]{teacher.getId()}, lessonMapper);
    }

    @Override
    public boolean save(Lesson lesson) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("course_id", lesson.getCourse().getId())
                .addValue("teacher_id", lesson.getTeacher().getId())
                .addValue("classroom", lesson.getClassroom())
                .addValue("day", lesson.getDay().toString())
                .addValue("time", lesson.getStartTime())
                .addValue("type", lesson.getType().toString());
        Number generatedId = jdbcInsert.withTableName("lessons").usingGeneratedKeyColumns("id")
                .executeAndReturnKey(parameterSource);
        if (generatedId != null) {
            lesson.setId(generatedId.intValue());
            if (lesson.getGroups() != null) {
                return assignGroups(lesson, lesson.getGroups());
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean assignGroups(Lesson lesson, List<Group> groups) {
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
        return of(result).sum() == groups.size();
    }

    @Override
    public boolean update(Lesson lesson) {
        return jdbcTemplate.update(update,
                lesson.getCourse().getId(), lesson.getTeacher().getId(), lesson.getClassroom(),
                lesson.getDay().toString(), lesson.getStartTime(), lesson.getType().toString(), lesson.getId()) > 0;
    }

    @Override
    public boolean delete(Lesson lesson) {
        return jdbcTemplate.update(delete, lesson.getId()) > 0;
    }

    @Override
    public boolean deleteGroup(Lesson lesson, Group group) {
        return jdbcTemplate.update(deleteGroup, lesson.getId(), group.getId()) > 0;
    }
}
