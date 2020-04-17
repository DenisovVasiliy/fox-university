package com.foxminded.foxuniversity.service.implementation;

import com.foxminded.foxuniversity.AppConfig;
import com.foxminded.foxuniversity.dao.CourseDao;
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

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {
    @Mock
    private static CourseDao courseDao;
    @Mock
    private static LessonService lessonService;
    @Mock
    private static TeacherService teacherService;
    @Mock
    private Lesson lesson;

    @InjectMocks
    private static CourseService courseService;

    private Course course = new Course(1, "name", "Desc.");

    @BeforeAll
    public static void setUp() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        courseService = context.getBean(CourseServiceImpl.class);
    }

    @Test
    public void shouldCallGetAllCoursesAndReturnResult() {
        when(courseDao.getAll()).thenReturn(singletonList(course));
        List<Course> actual = courseService.getAll();
        verify(courseDao).getAll();
        assertEquals(singletonList(course), actual);
    }

    @Test
    public void shouldCallGetCourseByIdAndReturnResult() {
        when(courseDao.getById(1)).thenReturn(course);
        Course actual = courseService.getById(1);
        verify(courseDao).getById(1);
        assertEquals(course, actual);
    }

    @Test
    public void shouldCallGetCoursesByGroupAndReturnResult() {
        Group group = new Group(1, "123");
        when(courseDao.getByGroup(group)).thenReturn(singletonList(course));
        List<Course> actual = courseService.getByGroup(group);
        verify(courseDao).getByGroup(group);
        assertEquals(singletonList(course), actual);
    }

    @Test
    public void shouldCallSaveCourseAndReturnTrue() {
        when(courseDao.save(course)).thenReturn(true);
        assertTrue(courseService.save(course));
        verify(courseDao).save(course);
    }

    @Test
    public void shouldCallUpdateCourseAndReturnTrue() {
        when(courseDao.update(course)).thenReturn(true);
        assertTrue(courseService.update(course));
        verify(courseDao).update(course);
    }

    @Test
    public void shouldCallDeleteCourseAndReturnTrueIfThereAreNoTeachers() {
        when(teacherService.getByCourse(course)).thenReturn(new ArrayList<>());
        when(courseDao.delete(course)).thenReturn(true);
        assertTrue(courseService.delete(course));
        verify(courseDao).delete(course);
    }

    @Test
    public void shouldNotCallDeleteCourseAndReturnFalseIfThereAreSomeTeachers() {
        Teacher teacher = new Teacher(1, "Test", "Teacher", course);
        when(teacherService.getByCourse(course)).thenReturn(singletonList(teacher));
        assertFalse(courseService.delete(course));
        verifyZeroInteractions(courseDao);
    }

    @Test
    public void shouldCallLessonServiceAndFillLessonsInTheCourse() {
        when(lessonService.getByCourse(course)).thenReturn(singletonList(lesson));
        courseService.fillCoursesLessons(course);
        verify(lessonService).getByCourse(course);
        assertEquals(singletonList(lesson), course.getLessons());
    }
}