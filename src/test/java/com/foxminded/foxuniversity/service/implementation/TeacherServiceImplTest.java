package com.foxminded.foxuniversity.service.implementation;

import com.foxminded.foxuniversity.AppConfig;
import com.foxminded.foxuniversity.dao.TeacherDao;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Lesson;
import com.foxminded.foxuniversity.domain.Teacher;
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

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static java.util.Collections.singletonList;

@ExtendWith(MockitoExtension.class)
class TeacherServiceImplTest {
    @Mock
    private Course course;
    @Mock
    private Lesson lesson;
    @Mock
    private static TeacherDao teacherDao;
    @Mock
    private static LessonService lessonService;
    @InjectMocks
    private static TeacherService teacherService;

    private Teacher teacher = new Teacher(1, "Test", "Teacher", course);


    @BeforeAll
    public static void setUp() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        teacherService = context.getBean(TeacherServiceImpl.class);
    }

    @Test
    public void shouldCallGetAllTeachers() {
        teacherService.getAll();
        verify(teacherDao).getAll();
    }

    @Test
    public void shouldCallGetTeacherById() {
        teacherService.getById(1);
        verify(teacherDao).getById(1);
    }

    @Test
    public void shouldCallGetTeacherByCourse() {
        teacherService.getByCourse(course);
        verify(teacherDao).getByCourse(course);
    }

    @Test
    public void shouldCallSaveTeacher() {
        teacherService.save(teacher);
        verify(teacherDao).save(teacher);
    }

    @Test
    public void shouldCallUpdateTeacher() {
        teacherService.update(teacher);
        verify(teacherDao).update(teacher);
    }

    @Test
    public void shouldCallDeleteTeacherIfTeacherHasNoLessons() {
        when(lessonService.getByTeacher(teacher)).thenReturn(emptyList());
        when(teacherDao.delete(teacher)).thenReturn(true);
        assertTrue(teacherService.delete(teacher));
        verify(lessonService).getByTeacher(teacher);
    }

    @Test
    public void shouldNotCallDeleteTeacherIfTeacherHasLessons() {
        when(lessonService.getByTeacher(teacher)).thenReturn(singletonList(lesson));
        assertFalse(teacherService.delete(teacher));
        verify(lessonService).getByTeacher(teacher);
    }

    @Test
    public void shouldCallLessonServiceGetByTeacher() {
        teacherService.getTimetable(teacher);
        verify(lessonService).getByTeacher(teacher);
    }
}