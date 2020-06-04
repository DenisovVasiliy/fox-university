package com.foxminded.foxuniversity.dao.mappers;

import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Teacher;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TeacherMapper implements RowMapper<Teacher> {

    @Override
    public Teacher mapRow(ResultSet set, int i) throws SQLException {
        return new Teacher(set.getInt("id"), set.getString("first_name"),
                set.getString("last_name"), new Course(set.getInt("course_id")));
    }
}
