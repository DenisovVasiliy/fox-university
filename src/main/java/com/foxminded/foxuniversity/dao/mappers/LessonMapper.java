package com.foxminded.foxuniversity.dao.mappers;

import com.foxminded.foxuniversity.domain.*;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LessonMapper implements RowMapper<Lesson> {

    @Override
    public Lesson mapRow(ResultSet set, int i) throws SQLException {
        return new Lesson(set.getInt("id"), new Course(set.getInt("course_id")),
                new Teacher(set.getInt("teacher_id")), set.getInt("classroom"),
                Day.valueOf(set.getString("day")), set.getTime("time"),
                LessonsType.valueOf(set.getString("type")));
    }
}
