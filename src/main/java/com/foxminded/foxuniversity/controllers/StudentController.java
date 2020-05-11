package com.foxminded.foxuniversity.controllers;

import com.foxminded.foxuniversity.domain.Student;
import com.foxminded.foxuniversity.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static java.lang.Integer.parseInt;

@Controller
@RequestMapping("/students")
public class StudentController {
    private StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public String showAllStudents(Model model) {
        List<Student> students = studentService.getAll();
        model.addAttribute("students", students);
        return "students/students";
    }

    @GetMapping("/{id}")
    public String showCourseById(Model model, @PathVariable String id) {
        Student student = studentService.getById(parseInt(id));
        model.addAttribute("student", student);
        return "students/personalPage";
    }
}
