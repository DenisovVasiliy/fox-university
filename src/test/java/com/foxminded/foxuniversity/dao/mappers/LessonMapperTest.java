package com.foxminded.foxuniversity.dao.mappers;

import com.foxminded.foxuniversity.dao.DaoTestConfig;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Teacher;
import com.foxminded.foxuniversity.domain.Lesson;
import com.foxminded.foxuniversity.domain.LessonsType;
import com.foxminded.foxuniversity.domain.Day;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DaoTestConfig.class})
class LessonMapperTest {
    @Mock
    private ResultSet resultSet;
    @InjectMocks
    @Autowired
    private LessonMapper lessonMapper;
    private static Course course = new Course(2, "Name", "Desc.");
    private static Teacher teacher = new Teacher(3, "Name", "LastName", course);
    private static Lesson expectedLesson = new Lesson(1, course, teacher, 10, Day.MONDAY,
            new Time(9, 30, 0), LessonsType.LECTURE);

    @Test
    public void shouldReturnStudentWithCorrectSettings() throws SQLException {
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getInt("course_id")).thenReturn(2);
        when(resultSet.getInt("teacher_id")).thenReturn(3);
        when(resultSet.getInt("classroom")).thenReturn(10);
        when(resultSet.getString("day")).thenReturn(Day.MONDAY.toString());
        when(resultSet.getTime("time")).thenReturn(new Time(9, 30, 0));
        when(resultSet.getString("type")).thenReturn(LessonsType.LECTURE.toString());

        Lesson lesson = lessonMapper.mapRow(resultSet, 1);

        assertEquals(expectedLesson, lesson);
    }
}
