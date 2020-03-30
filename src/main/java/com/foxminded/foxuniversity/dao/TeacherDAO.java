package com.foxminded.foxuniversity.dao;

import com.foxminded.foxuniversity.dao.infra.QueriesConstants;
import com.foxminded.foxuniversity.dao.mappers.TeacherMapper;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TeacherDAO {
    @Autowired
    private Environment environment;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SimpleJdbcInsert jdbcInsert;

    public List<Teacher> getAll() {
        return jdbcTemplate.query(environment.getProperty(QueriesConstants.GET_ALL_TEACHERS), new TeacherMapper());
    }

    public Teacher getById(int id) {
        return jdbcTemplate.queryForObject(environment.getProperty(QueriesConstants.GET_TEACHER_BY_ID),
                new Object[]{id}, new TeacherMapper());
    }

    public List<Teacher> getByCourse(Course course) {
        return jdbcTemplate.query(environment.getProperty(QueriesConstants.GET_TEACHERS_BY_COURSE),
                new Object[]{course.getId()}, new TeacherMapper());
    }

    public boolean delete(Teacher teacher) {
        return jdbcTemplate.update(environment.getProperty(QueriesConstants.DELETE_TEACHER), teacher.getId()) > 0;
    }

    public boolean update(Teacher teacher) {
        return jdbcTemplate.update(environment.getProperty(QueriesConstants.UPDATE_TEACHER),
                teacher.getFirstName(), teacher.getLastName(), teacher.getCourse().getId(), teacher.getId()) > 0;
    }

    public boolean save(Teacher teacher) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(teacher);
        Number id = jdbcInsert.withTableName("teachers").usingGeneratedKeyColumns("id")
                .executeAndReturnKey(parameterSource);
        if(id !=null) {
            teacher.setId(id.intValue());
            return true;
        }
        return false;
    }
}
