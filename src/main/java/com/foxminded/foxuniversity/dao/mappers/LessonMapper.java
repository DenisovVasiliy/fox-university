package com.foxminded.foxuniversity.dao.mappers;


import com.foxminded.foxuniversity.dao.CourseDao;
import com.foxminded.foxuniversity.dao.GroupDao;
import com.foxminded.foxuniversity.dao.TeacherDao;
import com.foxminded.foxuniversity.domain.Day;
import com.foxminded.foxuniversity.domain.Lesson;
import com.foxminded.foxuniversity.domain.LessonsType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LessonMapper implements RowMapper<Lesson> {
    @Autowired
    private CourseDao courseDAO;
    @Autowired
    private TeacherDao teacherDAO;
    @Autowired
    private GroupDao groupDAO;

    @Override
    public Lesson mapRow(ResultSet set, int i) throws SQLException {
        Lesson lesson = new Lesson(set.getInt("id"), courseDAO.getById(set.getInt("course_id")),
                teacherDAO.getById(set.getInt("teacher_id")), set.getInt("classroom"),
                Day.valueOf(set.getString("day")), set.getTime("time"),
                LessonsType.valueOf(set.getString("type")));
        lesson.setGroups(groupDAO.getByLesson(lesson));
        return lesson;
    }
}
