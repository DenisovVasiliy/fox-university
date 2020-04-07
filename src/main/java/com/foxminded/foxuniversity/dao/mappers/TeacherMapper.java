package com.foxminded.foxuniversity.dao.mappers;

import com.foxminded.foxuniversity.dao.springJdbcDao.CourseDAO;
import com.foxminded.foxuniversity.domain.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TeacherMapper implements RowMapper<Teacher> {
    @Autowired
    CourseDAO courseDAO;

    @Override
    public Teacher mapRow(ResultSet set, int i) throws SQLException {
        return new Teacher(set.getInt("id"), set.getString("first_name"),
                set.getString("last_name"), courseDAO.getById(set.getInt("course_id")));
    }
}
