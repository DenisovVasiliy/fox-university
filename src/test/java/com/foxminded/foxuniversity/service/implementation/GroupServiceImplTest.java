package com.foxminded.foxuniversity.service.implementation;

import com.foxminded.foxuniversity.AppConfig;
import com.foxminded.foxuniversity.dao.GroupDao;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.domain.Lesson;
import com.foxminded.foxuniversity.domain.Student;
import com.foxminded.foxuniversity.service.CourseService;
import com.foxminded.foxuniversity.service.GroupService;
import com.foxminded.foxuniversity.service.StudentService;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static java.util.Collections.singletonList;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {
    @Mock
    private Lesson lesson;
    @Mock
    private Student student;
    @Mock
    private static GroupDao groupDao;
    @Mock
    private static CourseService courseService;
    @Mock
    private static StudentService studentService;
    @InjectMocks
    private static GroupService groupService;

    private Group group = new Group(1, "test");
    private static List<Course> courses = new ArrayList<>();

    @BeforeAll
    public static void setUp() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        groupService = context.getBean(GroupServiceImpl.class);
        for (int i = 0; i < 3; i++) {
            courses.add(new Course(i + 1, "C-0" + (i + 1), "C-0" + (i + 1) + " course"));
        }
    }

    @Test
    public void shouldCallGetAllGroups() {
        groupService.getAll();
        verify(groupDao).getAll();
    }

    @Test
    public void shouldCallGetGroupById() {
        groupService.getById(1);
        verify(groupDao).getById(1);
    }

    @Test
    public void shouldCallGetGroupByLesson() {
        groupService.getByLesson(lesson);
        verify(groupDao).getByLesson(lesson);
    }

    @Test
    public void shouldCallSaveGroup() {
        groupService.save(group);
        verify(groupDao).save(group);
    }

    @Test
    public void shouldCallUpdateGroup() {
        groupService.update(group);
        verify(groupDao).update(group);
    }

    @Test
    public void shouldCallDeleteGroup() {
        groupService.delete(group);
        verify(groupDao).delete(group);
    }

    @Test
    public void shouldCallAssignToCoursesAndIfItReturnsTrueSetCoursesToGroup() {
        group.setCourses(null);
        when(groupDao.assignToCourses(group, courses)).thenReturn(true);
        assertTrue(groupService.assignToCourses(group, courses));
        verify(groupDao).assignToCourses(group, courses);
        assertEquals(courses, group.getCourses());
    }

    @Test
    public void shouldCallAssignToCoursesAndIfItReturnsFalseDoNotSetCoursesToGroup() {
        group.setCourses(null);
        when(groupDao.assignToCourses(group, courses)).thenReturn(false);
        assertFalse(groupService.assignToCourses(group, courses));
        verify(groupDao).assignToCourses(group, courses);
        assertNull(group.getCourses());
    }

    @Test
    public void shouldCallDeleteFromCourseAndIfItReturnsTrueRemoveCourseFromGroup() {
        List<Course> newCourses = new ArrayList<>(courses);
        group.setCourses(newCourses);
        when(groupDao.deleteFromCourse(group, courses.get(0))).thenReturn(true);
        assertTrue(groupService.deleteFromCourse(group, courses.get(0)));
        verify(groupDao).deleteFromCourse(group, courses.get(0));
        assertEquals(courses.subList(1, 3), group.getCourses());
    }

    @Test
    public void shouldCallDeleteFromCourseAndIfItReturnsFalseDoNotRemoveCourseFromGroup() {
        group.setCourses(courses);
        when(groupDao.deleteFromCourse(group, courses.get(0))).thenReturn(false);
        assertFalse(groupService.deleteFromCourse(group, courses.get(0)));
        verify(groupDao).deleteFromCourse(group, courses.get(0));
        assertEquals(courses, group.getCourses());
    }

    @Test
    public void shouldCallDeleteFromCoursesAndIfItReturnsTrueRemoveCourseFromGroup() {
        List<Course> newCourses = new ArrayList<>(courses);
        group.setCourses(newCourses);
        when(groupDao.deleteFromCourse(group, courses.subList(1, 3))).thenReturn(true);
        assertTrue(groupService.deleteFromCourse(group, courses.subList(1, 3)));
        verify(groupDao).deleteFromCourse(group, courses.subList(1, 3));
        assertEquals(courses.subList(0, 1), group.getCourses());
    }

    @Test
    public void shouldCallDeleteFromCoursesAndIfItReturnsFalseDoNotRemoveCourseFromGroup() {
        group.setCourses(courses);
        when(groupDao.deleteFromCourse(group, courses.subList(1, 3))).thenReturn(false);
        assertFalse(groupService.deleteFromCourse(group, courses.subList(1, 3)));
        verify(groupDao).deleteFromCourse(group, courses.subList(1, 3));
        assertEquals(courses, group.getCourses());
    }

    @Test
    public void shouldCallCourseServiceAndFillCoursesInTheGroup() {
        group.setCourses(null);
        when(courseService.getByGroup(group)).thenReturn(courses);
        groupService.fillCourses(group);
        verify(courseService).getByGroup(group);
        assertEquals(courses, group.getCourses());
    }

    @Test
    public void shouldCallStudentServiceAndFillStudentsInTheGroup() {
        group.setStudents(null);
        when(studentService.getByGroup(group)).thenReturn(singletonList(student));
        groupService.fillStudents(group);
        verify(studentService).getByGroup(group);
        assertEquals(singletonList(student), group.getStudents());
    }
}