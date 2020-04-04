package com.foxminded.foxuniversity.dao;

import com.foxminded.foxuniversity.dao.mappers.TeacherMapper;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@PropertySource("classpath:queries.properties")
public class TeacherDAO {
    @Autowired
    private Environment environment;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SimpleJdbcInsert jdbcInsert;
    @Autowired
    private TeacherMapper teacherMapper;

    @Value("${teacher.getAll}")
    private String getAll;
    @Value("${teacher.getById}")
    private String getById;
    @Value("${teacher.getByCourse}")
    private String getByCourse;
    @Value("${teacher.update}")
    private String update;
    @Value("${teacher.updateCourse}")
    private String updateCourse;
    @Value("${teacher.delete}")
    private String delete;

    public List<Teacher> getAll() {
        return jdbcTemplate.query(getAll, teacherMapper);
    }

    public Teacher getById(int id) {
        return jdbcTemplate.queryForObject(getById, new Object[]{id}, teacherMapper);
    }

    public List<Teacher> getByCourse(Course course) {
        return jdbcTemplate.query(getByCourse, new Object[]{course.getId()}, teacherMapper);
    }

    public boolean delete(Teacher teacher) {
        return jdbcTemplate.update(delete, teacher.getId()) > 0;
    }

    public boolean update(Teacher teacher) {
        return jdbcTemplate.update(update,
                teacher.getFirstName(), teacher.getLastName(), teacher.getCourse().getId(), teacher.getId()) > 0;
    }

    public boolean save(Teacher teacher) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("first_name", teacher.getFirstName())
                .addValue("last_name", teacher.getLastName())
                .addValue("course_id", teacher.getCourse().getId());
        Number id = jdbcInsert.withTableName("teachers").usingGeneratedKeyColumns("id")
                .executeAndReturnKey(parameters);
        if(id != null) {
            teacher.setId(id.intValue());
            return true;
        }
        return false;
    }
}
