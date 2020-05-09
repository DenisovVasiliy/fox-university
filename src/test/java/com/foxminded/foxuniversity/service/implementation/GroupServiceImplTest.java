package com.foxminded.foxuniversity.service.implementation;

import com.foxminded.foxuniversity.dao.GroupDao;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.domain.Lesson;
import com.foxminded.foxuniversity.domain.Student;
import com.foxminded.foxuniversity.service.GroupService;
import com.foxminded.foxuniversity.service.ServiceTestConfig;
import com.foxminded.foxuniversity.service.StudentService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static java.util.Collections.singletonList;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ServiceTestConfig.class})
class GroupServiceImplTest {
    @Mock
    private Lesson lesson;
    @Mock
    private Student student;
    @Mock
    private GroupDao groupDao;
    @Mock
    private StudentService studentService;
    @InjectMocks
    @Autowired
    private GroupService groupService;

    private static Group group = new Group(1, "test");
    private static List<Group> groups = new ArrayList<>();
    private List<Student> students = singletonList(student);
    private static List<Course> courses = new ArrayList<>();

    @BeforeAll
    public static void setUp() {
        for (int i = 0; i < 3; i++) {
            courses.add(new Course(i + 1, "C-0" + (i + 1), "C-0" + (i + 1) + " course"));
        }
        groups.add(group);
    }

    @Test
    public void shouldCallGetAllGroupsAndReturnResult() {
        when(groupDao.getAll()).thenReturn(groups);
        when(studentService.getByGroup(group)).thenReturn(students);

        List<Group> actual = groupService.getAll();

        verify(groupDao).getAll();
        verify(studentService).getByGroup(group);
        assertEquals(groups, actual);
        assertEquals(students, actual.get(0).getStudents());
    }

    @Test
    public void shouldCallGetGroupByIdAndReturnResult() {
        when(groupDao.getById(1)).thenReturn(group);
        when(studentService.getByGroup(group)).thenReturn(students);

        Group actual = groupService.getById(1);

        verify(groupDao).getById(1);
        verify(studentService).getByGroup(group);
        assertEquals(group, actual);
        assertEquals(students, actual.getStudents());
    }

    @Test
    public void shouldCallGetGroupByLessonAndReturnResult() {
        when(groupDao.getByLesson(lesson)).thenReturn(groups);
        when(studentService.getByGroup(group)).thenReturn(students);

        List<Group> actual = groupService.getByLesson(lesson);

        verify(groupDao).getByLesson(lesson);
        verify(studentService).getByGroup(group);
        assertEquals(groups, actual);
        assertEquals(students, actual.get(0).getStudents());
    }

    @Test
    public void shouldCallSaveGroup() {
        groupService.save(group);
        verify(groupDao).save(group);
    }

    @Test
    public void shouldCallUpdateGroupAndReturnResult() {
        when(groupDao.update(group)).thenReturn(true);
        assertTrue(groupService.update(group));
        verify(groupDao).update(group);
    }

    @Test
    public void shouldCallDeleteGroupAndReturnResult() {
        when(groupDao.delete(group)).thenReturn(true);
        assertTrue(groupService.delete(group));
        verify(groupDao).delete(group);
    }

    @Test
    public void shouldCallAssignToCoursesAndIfItReturnsTrueSetGroupToCourses() {
        for (Course course : courses) {
            course.setGroups(new ArrayList<>());
        }
        when(groupDao.assignToCourses(group, courses)).thenReturn(true);

        assertTrue(groupService.assignToCourses(group, courses));

        verify(groupDao).assignToCourses(group, courses);
        for (Course course : courses) {
            assertTrue(course.getGroups().contains(group));
        }
    }

    @Test
    public void shouldCallAssignToCoursesAndIfItReturnsFalseDoNotSetGroupToCourses() {
        for (Course course : courses) {
            course.setGroups(new ArrayList<>());
        }
        when(groupDao.assignToCourses(group, courses)).thenReturn(false);

        assertFalse(groupService.assignToCourses(group, courses));

        verify(groupDao).assignToCourses(group, courses);
        for (Course course : courses) {
            assertFalse(course.getGroups().contains(group));
        }
    }

    @Test
    public void shouldCallDeleteFromCourseAndIfItReturnsTrueRemoveGroupFromCourse() {
        Course course = courses.get(0);
        course.setGroups(new ArrayList<>(groups));
        when(groupDao.deleteFromCourse(group, course)).thenReturn(true);

        assertTrue(groupService.deleteFromCourse(group, course));

        verify(groupDao).deleteFromCourse(group, course);
        assertFalse(course.getGroups().contains(group));
    }

    @Test
    public void shouldCallDeleteFromCourseAndIfItReturnsFalseDoNotRemoveGroupFromCourse() {
        Course course = courses.get(0);
        course.setGroups(new ArrayList<>(groups));
        when(groupDao.deleteFromCourse(group, courses.get(0))).thenReturn(false);

        assertFalse(groupService.deleteFromCourse(group, courses.get(0)));

        verify(groupDao).deleteFromCourse(group, courses.get(0));
        assertTrue(course.getGroups().contains(group));
    }

    @Test
    public void shouldCallDeleteFromCoursesAndIfItReturnsTrueRemoveGroupFromCourses() {
        for (Course course : courses) {
            course.setGroups(new ArrayList<>(groups));
        }
        List<Course> coursesToDelete = courses.subList(1, 3);
        when(groupDao.deleteFromCourse(group, coursesToDelete)).thenReturn(true);

        assertTrue(groupService.deleteFromCourse(group, coursesToDelete));

        verify(groupDao).deleteFromCourse(group, coursesToDelete);
        assertTrue(courses.get(0).getGroups().contains(group));
        assertFalse(courses.get(1).getGroups().contains(group));
        assertFalse(courses.get(2).getGroups().contains(group));
    }

    @Test
    public void shouldCallDeleteFromCoursesAndIfItReturnsFalseDoNotRemoveGroupFromCourses() {
        for (Course course : courses) {
            course.setGroups(new ArrayList<>(groups));
        }
        List<Course> coursesToDelete = courses.subList(1, 3);
        when(groupDao.deleteFromCourse(group, coursesToDelete)).thenReturn(false);

        assertFalse(groupService.deleteFromCourse(group, coursesToDelete));

        verify(groupDao).deleteFromCourse(group, coursesToDelete);
        for (Course course : courses) {
            assertTrue(course.getGroups().contains(group));
        }
    }
}