package com.foxminded.foxuniversity.controllers;

import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.domain.Student;
import com.foxminded.foxuniversity.service.GroupService;
import com.foxminded.foxuniversity.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static java.lang.String.format;

@Controller
@RequestMapping("/students")
public class StudentController {
    private StudentService studentService;
    private GroupService groupService;

    private static final String REDIRECT_TO_INFO_PAGE = "redirect:/students/info/?id=%s";

    @Autowired
    public StudentController(StudentService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }

    @GetMapping
    public String showAllStudents(Model model) {
        List<Student> students = studentService.getAll();
        model.addAttribute("students", students);
        model.addAttribute("newStudent", new Student());
        return "students/students";
    }

    @GetMapping("/info")
    public String showStudentById(Model model, int id) {
        Student student = studentService.getById(id);
        model.addAttribute("student", student);
        return "students/student-info";
    }

    @GetMapping("/assign")
    public String showAssignStudentPage(Model model, int id) {
        Student student = studentService.getById(id);
        List<Group> groups = groupService.getAll();
        model.addAttribute("student", student);
        model.addAttribute("groups", groups);
        model.addAttribute("newGroup", new Group(0));
        return "students/student-assign";
    }

    @PostMapping("/assign")
    public ModelAndView assignStudent(@ModelAttribute("newGroup") Group group, int studentId) {
        Student student = studentService.getById(studentId);
        if (student.getGroup() != null) {
            student.setGroup(group);
            studentService.updateAssignment(student);
        } else {
            studentService.assignToGroup(student, group);
        }
        String redirect = format(REDIRECT_TO_INFO_PAGE, student.getId());
        return new ModelAndView(redirect);
    }

    @PostMapping("/delete")
    public ModelAndView deleteStudent(@ModelAttribute("student") Student student) {
        if (studentService.delete(student)) {
            return new ModelAndView("redirect:/students/");
        }
        String redirect = format(REDIRECT_TO_INFO_PAGE, student.getId());
        return new ModelAndView(redirect);
    }

    @PostMapping("/new")
    public ModelAndView saveStudent(@ModelAttribute("newStudent") Student student) {
        studentService.save(student);
        String redirect = format(REDIRECT_TO_INFO_PAGE, student.getId());
        return new ModelAndView(redirect);
    }

    @PostMapping("/edit")
    public ModelAndView updateStudent(@ModelAttribute("student") Student student, int id) {
        studentService.update(student);
        String redirect = format(REDIRECT_TO_INFO_PAGE, id);
        return new ModelAndView(redirect);
    }

    @PostMapping("/delete-from-group/")
    public ModelAndView deleteStudentFromGroup(@ModelAttribute("student") Student student) {
        studentService.deleteAssignment(student);
        String redirect = format(REDIRECT_TO_INFO_PAGE, student.getId());
        return new ModelAndView(redirect);
    }
}
