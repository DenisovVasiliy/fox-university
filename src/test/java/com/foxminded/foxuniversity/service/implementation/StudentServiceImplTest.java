package com.foxminded.foxuniversity.service.implementation;

import com.foxminded.foxuniversity.AppConfig;
import com.foxminded.foxuniversity.dao.StudentDao;
import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.domain.Lesson;
import com.foxminded.foxuniversity.domain.Student;
import com.foxminded.foxuniversity.service.GroupService;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static java.util.Collections.singletonList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {
    @Mock
    private Lesson lesson;
    @Mock
    private static StudentDao studentDao;
    @Mock
    private static LessonService lessonService;
    @Mock
    private static GroupService groupService;
    @InjectMocks
    private static StudentService studentService;

    private static Student student = new Student(1, "Test", "With-Group");
    private static List<Student> students = new ArrayList<>();
    private static Group group = new Group(1, "TestGroup");
    private static Group groupWithStudents = new Group(1, "TestGroup");

    @BeforeAll
    public static void setUp() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        studentService = context.getBean(StudentServiceImpl.class);
        student.setGroup(group);
        students.add(student);
        students.add(new Student(2, "Test", "WithOut-Group"));
        groupWithStudents.setStudents(students.subList(0, 1));
    }

    @Test
    public void shouldCallGetAllStudentsAndReturnResult() {
        when(studentDao.getAll()).thenReturn(students);
        when(groupService.getById(1)).thenReturn(groupWithStudents);

        List<Student> actual = studentService.getAll();

        verify(studentDao).getAll();
        verify(groupService, times(1)).getById(1);
        assertEquals(students, actual);
        assertEquals(groupWithStudents, actual.get(0).getGroup());
        assertNull(actual.get(1).getGroup());
        assertEquals(groupWithStudents.getStudents(), actual.get(0).getGroup().getStudents());
    }

    @Test
    public void shouldCallGetStudentByIdAndReturnResult() {
        when(studentDao.getById(1)).thenReturn(student);
        when(groupService.getById(1)).thenReturn(groupWithStudents);

        Student actual = studentService.getById(1);

        verify(studentDao).getById(1);
        verify(groupService).getById(1);
        assertEquals(student, actual);
        assertEquals(groupWithStudents, actual.getGroup());
        assertEquals(groupWithStudents.getStudents(), actual.getGroup().getStudents());
    }

    @Test
    public void shouldCallGetStudentByGroupAndReturnResult() {
        when(studentDao.getByGroup(groupWithStudents)).thenReturn(singletonList(student));

        List<Student> actual = studentService.getByGroup(groupWithStudents);

        verify(studentDao).getByGroup(group);
        verifyZeroInteractions(groupService);
        assertEquals(singletonList(student), actual);
        assertEquals(groupWithStudents, actual.get(0).getGroup());
        assertEquals(groupWithStudents.getStudents(), actual.get(0).getGroup().getStudents());
    }

    @Test
    public void shouldCallSaveStudent() {
        studentService.save(student);
        verify(studentDao).save(student);
    }

    @Test
    public void shouldCallUpdateStudentAndReturnResult() {
        when(studentDao.update(student)).thenReturn(true);
        assertTrue(studentService.update(student));
        verify(studentDao).update(student);
    }

    @Test
    public void shouldCallDeleteStudentAndReturnResult() {
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
    public void shouldCallUpdateAssignmentAndReturnResult() {
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