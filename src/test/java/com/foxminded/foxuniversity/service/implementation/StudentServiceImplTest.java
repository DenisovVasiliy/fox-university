package com.foxminded.foxuniversity.service.implementation;

import com.foxminded.foxuniversity.AppConfig;
import com.foxminded.foxuniversity.dao.StudentDao;
import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.domain.Lesson;
import com.foxminded.foxuniversity.domain.Student;
import com.foxminded.foxuniversity.service.LessonService;
import com.foxminded.foxuniversity.service.StudentService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static java.util.Collections.singletonList;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {
    @Mock
    private Group group;
    @Mock
    private Lesson lesson;
    @Mock
    private static StudentDao studentDao;
    @Mock
    private static LessonService lessonService;
    @InjectMocks
    private static StudentService studentService;

    private static Student student = new Student(1, "Test", "Student");

    @BeforeAll
    public static void setUp() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        studentService = context.getBean(StudentServiceImpl.class);
    }

    @Test
    public void shouldCallGetAllStudentsAndReturnResult() {
        when(studentDao.getAll()).thenReturn(singletonList(student));
        List<Student> actual = studentService.getAll();
        verify(studentDao).getAll();
        assertEquals(singletonList(student), actual);
    }

    @Test
    public void shouldCallGetStudentByIdAndReturnResult() {
        when(studentDao.getById(1)).thenReturn(student);
        Student actual = studentService.getById(1);
        verify(studentDao).getById(1);
        assertEquals(student, actual);
    }

    @Test
    public void shouldCallGetStudentByGroupAndReturnResult() {
        when(studentDao.getByGroup(group)).thenReturn(singletonList(student));
        List<Student> actual = studentService.getByGroup(group);
        verify(studentDao).getByGroup(group);
        assertEquals(singletonList(student), actual);
    }

    @Test
    public void shouldCallSaveStudentAndReturnTrue() {
        when(studentDao.save(student)).thenReturn(true);
        assertTrue(studentService.save(student));
        verify(studentDao).save(student);
    }

    @Test
    public void shouldCallUpdateStudentAndReturnTrue() {
        when(studentDao.update(student)).thenReturn(true);
        assertTrue(studentService.update(student));
        verify(studentDao).update(student);
    }

    @Test
    public void shouldCallDeleteStudentAndReturnTrue() {
        when(studentDao.delete(student)).thenReturn(true);
        assertTrue(studentService.delete(student));
        verify(studentDao).delete(student);
    }

    @Test
    public void shouldCallGetLessonsAndReturnResult() {
        when(lessonService.getByStudent(student)).thenReturn(singletonList(lesson));
        List<Lesson> actual = studentService.getTimetable(student);
        verify(lessonService).getByStudent(student);
        assertEquals(singletonList(lesson), actual);
    }

    @Test
    public void shouldReturnFalseIfPassedGroupIsNull() {
        assertFalse(studentService.assignToGroup(student, null));
    }

    @Test
    public void shouldCallAssignToGroupAndSetGroupAndReturnTrueIfPassedGroupNotNullAndStudentsGroupIsNull() {
        student.setGroup(null);
        when(studentDao.assignToGroup(student, group)).thenReturn(true);
        assertTrue(studentService.assignToGroup(student, group));
        verify(studentDao).assignToGroup(student, group);
        assertEquals(group, student.getGroup());
    }

    @Test
    public void shouldReturnFalseIfPassedGroupIsStudentsGroup() {
        student.setGroup(group);
        assertFalse(studentService.assignToGroup(student, group));
    }

    @Test
    public void shouldCallUpdateAssignmentAndSetNewGroupAndReturnTrueIfPassedGroupNotEqualsStudentsGroup() {
        Group newGroup = new Group(1, "new");
        student.setGroup(group);
        when(studentDao.updateAssignment(student)).thenReturn(true);
        assertTrue(studentService.assignToGroup(student, newGroup));
        verify(studentDao).updateAssignment(student);
        assertEquals(newGroup, student.getGroup());
    }

    @Test
    public void shouldCallUpdateAssignmentAndReturnFalseAndDoNotSetNewGroupIfUpdateAssignmentReturnsFalse() {
        Group newGroup = new Group(1, "new");
        student.setGroup(group);
        when(studentDao.updateAssignment(student)).thenReturn(false);
        assertFalse(studentService.assignToGroup(student, newGroup));
        verify(studentDao).updateAssignment(student);
        assertEquals(group, student.getGroup());
    }

    @Test
    public void shouldCallUpdateAssignmentAndReturnTrue() {
        when(studentDao.updateAssignment(student)).thenReturn(true);
        assertTrue(studentService.updateAssignment(student));
        verify(studentDao).updateAssignment(student);
    }

    @Test
    public void shouldCallDeleteAssignmentAndIfItReturnsTrueThenSetNullToStudentsGroupAndReturnTrue() {
        student.setGroup(group);
        when(studentDao.deleteAssignment(student)).thenReturn(true);
        assertTrue(studentService.deleteAssignment(student));
        verify(studentDao).deleteAssignment(student);
        assertNull(student.getGroup());
    }

    @Test
    public void shouldCallDeleteAssignmentAndIfItReturnsFalseThenReturnFalseAndDontChangeStudentsGroup() {
        student.setGroup(group);
        when(studentDao.deleteAssignment(student)).thenReturn(false);
        assertFalse(studentService.deleteAssignment(student));
        verify(studentDao).deleteAssignment(student);
        assertEquals(group, student.getGroup());
    }
}