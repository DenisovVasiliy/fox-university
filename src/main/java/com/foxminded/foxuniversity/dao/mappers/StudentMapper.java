package com.foxminded.foxuniversity.dao.mappers;

import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.domain.Student;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StudentMapper implements RowMapper<Student> {

    @Override
    public Student mapRow(ResultSet resultSet, int i) throws SQLException {
        Student student = new Student(resultSet.getInt("id"), resultSet.getString("first_name"),
                resultSet.getString("last_name"));
        int groupId = resultSet.getInt("group_id");
        if (groupId > 0) {
            student.setGroup(new Group(groupId));
        }
        return student;
    }
}
