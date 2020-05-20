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
    public String showCourseById(Model model, int id) {
        Student student = studentService.getById(id);
        model.addAttribute("student", student);
        model.addAttribute("editStudent", student);
        return "students/student-info";
    }

    @PostMapping("/new")
    public ModelAndView saveStudent(@ModelAttribute("newStudent") Student student) {
        studentService.save(student);
        String redirect = format("redirect:/students/info/?id=%s", student.getId());
        return new ModelAndView(redirect);
    }

    @PostMapping("/edit")
    public ModelAndView updateStudent(@ModelAttribute("editStudent") Student student, int id) {
        studentService.update(student);
        String redirect = format("redirect:/students/info/?id=%s", id);
        return new ModelAndView(redirect);
    }
}
