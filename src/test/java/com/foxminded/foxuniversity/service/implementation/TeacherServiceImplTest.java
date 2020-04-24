package com.foxminded.foxuniversity.service.implementation;

import com.foxminded.foxuniversity.AppConfig;
import com.foxminded.foxuniversity.dao.TeacherDao;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.domain.Lesson;
import com.foxminded.foxuniversity.domain.Teacher;
import com.foxminded.foxuniversity.service.CourseService;
import com.foxminded.foxuniversity.service.LessonService;
import com.foxminded.foxuniversity.service.TeacherService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static java.util.Collections.singletonList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceImplTest {
    @Mock
    private static Lesson lesson;
    @Mock
    private static Group group;
    @Mock
    private static TeacherDao teacherDao;
    @Mock
    private static LessonService lessonService;
    @Mock
    private static CourseService courseService;
    @InjectMocks
    private static TeacherService teacherService;


    private Course course = new Course(1, "Test", "Course");
    private Teacher teacher = new Teacher(1, "Test", "Teacher", course);
    private List<Teacher> teachers = singletonList(teacher);
    private static Course filledCourse = new Course(1, "Test", "Course");

    @BeforeAll
    public static void setUp() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        teacherService = context.getBean(TeacherServiceImpl.class);
        filledCourse.setLessons(singletonList(lesson));
        filledCourse.setGroups(singletonList(group));
    }

    @Test
    public void shouldCallGetAllTeachersAndReturnResult() {
        when(teacherDao.getAll()).thenReturn(teachers);
        when(courseService.getById(1)).thenReturn(filledCourse);

        List<Teacher> actual = teacherService.getAll();

        verify(teacherDao).getAll();
        verify(courseService).getById(1);
        assertEquals(teachers, actual);
        assertEquals(filledCourse, actual.get(0).getCourse());
        assertEquals(filledCourse.getLessons(), actual.get(0).getCourse().getLessons());
    }

    @Test
    public void shouldCallGetTeacherByIdAndReturnResult() {
        when(teacherDao.getById(1)).thenReturn(teacher);
        when(courseService.getById(1)).thenReturn(filledCourse);

        Teacher actual = teacherService.getById(1);

        verify(teacherDao).getById(1);
        verify(courseService).getById(1);
        assertEquals(teacher, actual);
        assertEquals(filledCourse, actual.getCourse());
        assertEquals(filledCourse.getLessons(), actual.getCourse().getLessons());
    }

    @Test
    public void shouldCallGetTeacherByCourseAndReturnResult() {
        when(teacherDao.getByCourse(filledCourse)).thenReturn(teachers);

        List<Teacher> actual = teacherService.getByCourse(filledCourse);

        verify(teacherDao).getByCourse(course);
        verifyZeroInteractions(courseService);
        assertEquals(teachers, actual);
        assertEquals(filledCourse, actual.get(0).getCourse());
        assertEquals(filledCourse.getLessons(), actual.get(0).getCourse().getLessons());
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