package com.foxminded.foxuniversity.dao.implementation;

import com.foxminded.foxuniversity.dao.StudentDao;
import com.foxminded.foxuniversity.dao.mappers.StudentMapper;
import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.domain.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@PropertySource("classpath:queries.properties")
public class StudentDaoPostgres implements StudentDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SimpleJdbcInsert jdbcInsert;
    @Autowired
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

    @Override
    public List<Student> getAll() {
        return jdbcTemplate.query(getAll, studentMapper);
    }

    @Override
    public Student getById(int id) {
        return jdbcTemplate.queryForObject(getById, new Object[]{id}, studentMapper);
    }

    @Override
    public List<Student> getByGroup(Group group) {
        return jdbcTemplate.query(getByGroup, new Object[]{group.getId()}, studentMapper);
    }

    @Override
    public void save(Student student) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("first_name", student.getFirstName())
                .addValue("last_name", student.getLastName());
        Number generatedId = jdbcInsert.withTableName("students").usingGeneratedKeyColumns("id")
                .executeAndReturnKey(parameterSource);
        student.setId(generatedId.intValue());
        if (student.getGroup() != null) {
            assignToGroup(student, student.getGroup());
        }
    }

    @Override
    public boolean update(Student student) {
        int updatedRows = jdbcTemplate.update(update, student.getFirstName(), student.getLastName(), student.getId());
        if (student.getGroup() != null) {
            if (updateAssignment(student)) {
                return true;
            } else return assignToGroup(student, student.getGroup());
        }
        return updatedRows > 0;
    }

    @Override
    public boolean updateAssignment(Student student) {
        return jdbcTemplate.update(updateAssignment, student.getGroup().getId(), student.getId()) > 0;
    }

    @Override
    public boolean assignToGroup(Student student, Group group) {
        return jdbcTemplate.update(assignToGroup, student.getId(), group.getId()) > 0;
    }

    @Override
    public boolean delete(Student student) {
        return jdbcTemplate.update(delete, student.getId()) > 0;
    }

    @Override
    public boolean deleteAssignment(Student student) {
        return jdbcTemplate.update(deleteAssignment, student.getId()) > 0;
    }
}
