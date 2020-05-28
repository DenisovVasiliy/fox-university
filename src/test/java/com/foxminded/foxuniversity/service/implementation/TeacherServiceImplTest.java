package com.foxminded.foxuniversity.service.implementation;

import com.foxminded.foxuniversity.dao.TeacherDao;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.domain.Lesson;
import com.foxminded.foxuniversity.domain.Teacher;
import com.foxminded.foxuniversity.service.CourseService;
import com.foxminded.foxuniversity.service.LessonService;
import com.foxminded.foxuniversity.service.ServiceTestConfig;
import com.foxminded.foxuniversity.service.TeacherService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static java.util.Collections.singletonList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ServiceTestConfig.class})
class TeacherServiceImplTest {
    @Mock
    private static Lesson lesson;
    @Mock
    private static Group group;
    @Mock
    private TeacherDao teacherDao;
    @Mock
    private LessonService lessonService;
    @Mock
    private CourseService courseService;
    @InjectMocks
    @Autowired
    private TeacherService teacherService;

    private Course course = new Course(1, "Test", "Course");
    private Teacher teacher = new Teacher(1, "Test", "Teacher", course);
    private List<Teacher> teachers = singletonList(teacher);
    private static Course filledCourse = new Course(1, "Test", "Course");

    @BeforeAll
    public static void setUp() {
        filledCourse.setLessons(singletonList(lesson));
        filledCourse.setGroups(singletonList(group));
    }

    @Test
    public void shouldCallGetAllTeachersAndReturnResult() {
        teacher.setCourse(course);
        when(teacherDao.getAll()).thenReturn(teachers);
        when(courseService.getById(1)).thenReturn(filledCourse);

        List<Teacher> actual = teacherService.getAll();

        verify(teacherDao).getAll();
        verify(courseService).getById(1);
        assertEquals(teachers, actual);
        assertSame(filledCourse, actual.get(0).getCourse());
    }

    @Test
    public void shouldCallGetTeacherByIdAndReturnResult() {
        teacher.setCourse(course);
        when(teacherDao.getById(1)).thenReturn(teacher);
        when(courseService.getById(1)).thenReturn(filledCourse);

        Teacher actual = teacherService.getById(1);

        verify(teacherDao).getById(1);
        verify(courseService).getById(1);
        assertEquals(teacher, actual);
        assertSame(filledCourse, actual.getCourse());
    }

    @Test
    public void shouldCallGetTeacherByCourseAndReturnResult() {
        teacher.setCourse(course);
        when(teacherDao.getByCourse(filledCourse)).thenReturn(teachers);

        List<Teacher> actual = teacherService.getByCourse(filledCourse);

        verify(teacherDao).getByCourse(course);
        verifyZeroInteractions(courseService);
        assertEquals(teachers, actual);
        assertSame(filledCourse, actual.get(0).getCourse());
    }

    @Test
    public void shouldCallGetTeacherByLessonAndReturnResult() {
        teacher.setCourse(course);
        when(lesson.getTeacher()).thenReturn(teacher);
        when(lesson.getCourse()).thenReturn(filledCourse);
        when(teacherDao.getById(1)).thenReturn(teacher);

        Teacher actual = teacherService.getByLesson(lesson);

        verify(teacherDao).getById(1);
        verifyZeroInteractions(courseService);
        assertEquals(teacher, actual);
        assertSame(filledCourse, actual.getCourse());
    }

    @Test
    public void shouldCallSaveTeacher() {
        teacherService.save(teacher);
        verify(teacherDao).save(teacher);
    }

    @Test
    public void shouldCallUpdateTeacherAndReturnResult() {
        when(teacherDao.update(teacher)).thenReturn(true);
        assertTrue(teacherService.update(teacher));
        verify(teacherDao).update(teacher);
    }

    @Test
    public void shouldCallUpdateTeacherWithHisCourseAndReturnResult() {
        when(teacherDao.updateWithCourse(teacher)).thenReturn(true);
        assertTrue(teacherService.updateWithCourse(teacher));
        verify(teacherDao).updateWithCourse(teacher);
    }

    @Test
    public void shouldCallDeleteTeacherIfTeacherHasNoLessons() {
        when(lessonService.getByTeacher(teacher)).thenReturn(emptyList());
        when(teacherDao.delete(teacher)).thenReturn(true);
        assertTrue(teacherService.delete(teacher));
        verify(lessonService).getByTeacher(teacher);
        verify(teacherDao).delete(teacher);
    }

    @Test
    public void shouldNotCallDeleteTeacherIfTeacherHasLessons() {
        when(lessonService.getByTeacher(teacher)).thenReturn(singletonList(lesson));
        assertFalse(teacherService.delete(teacher));
        verify(lessonService).getByTeacher(teacher);
        verifyZeroInteractions(teacherDao);
    }

    @Test
    public void shouldCallLessonServiceGetByTeacher() {
        when(lessonService.getByTeacher(teacher)).thenReturn(singletonList(lesson));
        List<Lesson> actual = teacherService.getTimetable(teacher);
        verify(lessonService).getByTeacher(teacher);
        assertEquals(singletonList(lesson), actual);
    }
}