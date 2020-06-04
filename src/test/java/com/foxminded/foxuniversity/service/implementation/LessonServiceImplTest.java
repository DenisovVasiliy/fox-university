package com.foxminded.foxuniversity.service.implementation;

import com.foxminded.foxuniversity.dao.LessonDao;
import com.foxminded.foxuniversity.domain.*;
import com.foxminded.foxuniversity.service.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ServiceTestConfig.class})
class LessonServiceImplTest {
    @Mock
    private Student student;
    @Mock
    private LessonDao lessonDao;
    @Mock
    private GroupService groupService;
    @Mock
    private CourseService courseService;
    @Mock
    private TeacherService teacherService;
    @InjectMocks
    @Autowired
    private LessonService lessonService;

    private static List<Group> groups = new ArrayList<>();
    private static Course course = new Course(1, "C-Name", "Desc.");
    private static Course filledCourse = new Course(1, "C-Name", "Desc.");
    private static Teacher teacher = new Teacher(1, "Name", "LastName", course);
    private static Teacher filledTeacher = new Teacher(1, "Name", "LastName", filledCourse);
    private static Lesson lesson = new Lesson(2, course, teacher, 10, Day.MONDAY,
            new Time(9, 30, 0), LessonsType.LECTURE);
    private static List<Lesson> lessons = singletonList(lesson);

    @BeforeAll
    public static void setUp() {
        for (int i = 0; i < 3; i++) {
            groups.add(new Group(i + 1, "gr-0" + (i + 1)));
        }
    }
    
    @BeforeEach
    public void rollbackLesson() {
        lesson.setTeacher(teacher);
        lesson.setCourse(course);
        lesson.setGroups(new ArrayList<>());
    }

    @Test
    public void shouldCallGetAllLessonsAndReturnResult() {
        when(lessonDao.getAll()).thenReturn(lessons);
        when(groupService.getByLesson(lesson)).thenReturn(groups);
        when(courseService.getById(1)).thenReturn(filledCourse);
        when(teacherService.getByLesson(lesson)).thenReturn(filledTeacher);

        List<Lesson> actual = lessonService.getAll();

        verify(lessonDao).getAll();
        verify(groupService).getByLesson(lesson);
        verify(courseService).getById(1);
        verify(teacherService).getByLesson(lesson);
        assertEquals(lessons, actual);
        assertSame(filledCourse, actual.get(0).getCourse());
        assertSame(filledTeacher, actual.get(0).getTeacher());
        assertEquals(groups, actual.get(0).getGroups());
    }

    @Test
    public void shouldCallGetLessonByIdAndReturnResult() {
        when(lessonDao.getById(1)).thenReturn(lesson);
        when(groupService.getByLesson(lesson)).thenReturn(groups);
        when(courseService.getById(1)).thenReturn(filledCourse);
        when(teacherService.getByLesson(lesson)).thenReturn(filledTeacher);

        Lesson actual = lessonService.getById(1);

        verify(lessonDao).getById(1);
        verify(groupService).getByLesson(lesson);
        verify(courseService).getById(1);
        verify(teacherService).getByLesson(lesson);
        assertEquals(lesson, actual);
        assertSame(filledCourse, actual.getCourse());
        assertSame(filledTeacher, actual.getTeacher());
        assertEquals(groups, actual.getGroups());
    }

    @Test
    public void shouldCallGetLessonsByCourseAndReturnResult() {
        when(lessonDao.getByCourse(filledCourse)).thenReturn(lessons);
        when(groupService.getByLesson(lesson)).thenReturn(groups);
        when(teacherService.getByLesson(lesson)).thenReturn(filledTeacher);

        List<Lesson> actual = lessonService.getByCourse(filledCourse);

        verify(lessonDao).getByCourse(course);
        verify(groupService).getByLesson(lesson);
        verifyZeroInteractions(courseService);
        verify(teacherService).getByLesson(lesson);
        assertEquals(lessons, actual);
        assertSame(filledCourse, actual.get(0).getCourse());
        assertSame(filledTeacher, actual.get(0).getTeacher());
        assertEquals(groups, actual.get(0).getGroups());
    }

    @Test
    public void shouldCallGetLessonByStudentAndReturnResult() {
        when(lessonDao.getByStudent(student)).thenReturn(lessons);
        when(groupService.getByLesson(lesson)).thenReturn(groups);
        when(courseService.getById(1)).thenReturn(filledCourse);
        when(teacherService.getByLesson(lesson)).thenReturn(filledTeacher);

        List<Lesson> actual = lessonService.getByStudent(student);

        verify(lessonDao).getByStudent(student);
        verify(groupService).getByLesson(lesson);
        verify(courseService).getById(1);
        verify(teacherService).getByLesson(lesson);
        assertEquals(lessons, actual);
        assertSame(filledCourse, actual.get(0).getCourse());
        assertSame(filledTeacher, actual.get(0).getTeacher());
        assertEquals(groups, actual.get(0).getGroups());
    }

    @Test
    public void shouldCallGetLessonByTeacherAndReturnResult() {
        when(lessonDao.getByTeacher(filledTeacher)).thenReturn(lessons);
        when(groupService.getByLesson(lesson)).thenReturn(groups);

        List<Lesson> actual = lessonService.getByTeacher(filledTeacher);

        verify(lessonDao).getByTeacher(teacher);
        verify(groupService).getByLesson(lesson);
        verifyZeroInteractions(courseService);
        verifyZeroInteractions(teacherService);
        assertEquals(lessons, actual);
        assertSame(filledCourse, actual.get(0).getCourse());
        assertSame(filledTeacher, actual.get(0).getTeacher());
        assertEquals(groups, actual.get(0).getGroups());
    }

    @Test
    public void shouldCallSaveLesson() {
        lessonService.save(lesson);
        verify(lessonDao).save(lesson);
    }

    @Test
    public void shouldCallUpdateLessonAndReturnResult() {
        when(lessonDao.update(lesson)).thenReturn(true);
        assertTrue(lessonService.update(lesson));
        verify(lessonDao).update(lesson);
    }

    @Test
    public void shouldCallDeleteLessonAndReturnResult() {
        when(lessonDao.delete(lesson)).thenReturn(true);
        assertTrue(lessonService.delete(lesson));
        verify(lessonDao).delete(lesson);
    }

    @Test
    public void shouldCallAssignGroupsAndIfItReturnsTrueSetGroupsToLesson() {
        when(lessonDao.assignGroups(lesson, groups)).thenReturn(true);
        assertTrue(lessonService.assignGroups(lesson, groups));
        verify(lessonDao).assignGroups(lesson, groups);
        assertEquals(groups, lesson.getGroups());
    }

    @Test
    public void shouldCallAssignGroupsAndIfItReturnsFalseDoNotSetGroupsToLesson() {
        when(lessonDao.assignGroups(lesson, groups)).thenReturn(false);
        assertFalse(lessonService.assignGroups(lesson, groups));
        verify(lessonDao).assignGroups(lesson, groups);
        assertEquals(0, lesson.getGroups().size());
    }

    @Test
    public void shouldCallDeleteGroupsFromLessonAndIfItReturnsTrueRemoveIt() {
        List<Group> newGroups = new ArrayList<>(groups);
        lesson.setGroups(newGroups);
        when(lessonDao.deleteGroup(lesson, groups.get(0))).thenReturn(true);
        assertTrue(lessonService.deleteGroup(lesson, groups.get(0)));
        verify(lessonDao).deleteGroup(lesson, groups.get(0));
        assertEquals(groups.subList(1, 3), lesson.getGroups());
    }

    @Test
    public void shouldCallDeleteGroupsFromLessonAndIfItReturnsFalseDoNotRemoveIt() {
        lesson.setGroups(groups);
        when(lessonDao.deleteGroup(lesson, groups.get(0))).thenReturn(false);
        assertFalse(lessonService.deleteGroup(lesson, groups.get(0)));
        verify(lessonDao).deleteGroup(lesson, groups.get(0));
        assertEquals(groups, lesson.getGroups());
    }
}