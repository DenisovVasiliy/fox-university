package com.foxminded.foxuniversity.service.implementation;

import com.foxminded.foxuniversity.AppConfig;
import com.foxminded.foxuniversity.dao.LessonDao;
import com.foxminded.foxuniversity.domain.*;
import com.foxminded.foxuniversity.service.GroupService;
import com.foxminded.foxuniversity.service.LessonService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LessonServiceImplTest {
    @Mock
    private static Student student;
    @Mock
    private static Course course;
    @Mock
    private static Teacher teacher;
    @Mock
    private static LessonDao lessonDao;
    @Mock
    private static GroupService groupService;
    @InjectMocks
    private static LessonService lessonService;

    private static List<Group> groups = new ArrayList<>();
    private static Lesson lesson;
    private static List<Lesson> lessons;

    @BeforeAll
    public static void setUp() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        lessonService = context.getBean(LessonServiceImpl.class);
        for (int i = 0; i < 3; i++) {
            groups.add(new Group(i + 1, "gr-0" + (i + 1)));
        }
        lesson = new Lesson(2, course, teacher, 10, Day.MONDAY,
                new Time(9, 30, 0), LessonsType.LECTURE);
        lessons = singletonList(lesson);
    }

    @Test
    public void shouldCallGetAllLessonsAndReturnResult() {
        when(lessonDao.getAll()).thenReturn(lessons);
        List<Lesson> actual = lessonService.getAll();
        verify(lessonDao).getAll();
        assertEquals(lessons, actual);
    }

    @Test
    public void shouldCallGetLessonByIdAndReturnResult() {
        when(lessonDao.getById(1)).thenReturn(lesson);
        Lesson actual = lessonService.getById(1);
        verify(lessonDao).getById(1);
        assertEquals(lesson, actual);
    }

    @Test
    public void shouldCallGetLessonsByCourseAndReturnResult() {
        when(lessonDao.getByCourse(course)).thenReturn(lessons);
        List<Lesson> actual = lessonService.getByCourse(course);
        verify(lessonDao).getByCourse(course);
        assertEquals(lessons, actual);
    }

    @Test
    public void shouldCallGetLessonByStudentAndReturnResult() {
        when(lessonDao.getByStudent(student)).thenReturn(lessons);
        List<Lesson> actual = lessonService.getByStudent(student);
        verify(lessonDao).getByStudent(student);
        assertEquals(lessons, actual);
    }

    @Test
    public void shouldCallGetLessonByTeacherAndReturnResult() {
        when(lessonDao.getByTeacher(teacher)).thenReturn(lessons);
        List<Lesson> actual = lessonService.getByTeacher(teacher);
        verify(lessonDao).getByTeacher(teacher);
        assertEquals(lessons, actual);
    }

    @Test
    public void shouldCallSaveLessonAndReturnResult() {
        when(lessonDao.save(lesson)).thenReturn(true);
        assertTrue(lessonService.save(lesson));
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
        lesson.setGroups(null);
        when(lessonDao.assignGroups(lesson, groups)).thenReturn(true);
        assertTrue(lessonService.assignGroups(lesson, groups));
        verify(lessonDao).assignGroups(lesson, groups);
        assertEquals(groups, lesson.getGroups());
    }

    @Test
    public void shouldCallAssignGroupsAndIfItReturnsFalseDoNotSetGroupsToLesson() {
        lesson.setGroups(null);
        when(lessonDao.assignGroups(lesson, groups)).thenReturn(false);
        assertFalse(lessonService.assignGroups(lesson, groups));
        verify(lessonDao).assignGroups(lesson, groups);
        assertNull(lesson.getGroups());
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

    @Test
    public void shouldCallGroupServiceAndSetGroups() {
        lesson.setGroups(null);
        when(groupService.getByLesson(lesson)).thenReturn(groups);
        lessonService.fillGroups(lesson);
        verify(groupService).getByLesson(lesson);
        assertEquals(groups, lesson.getGroups());
    }
}