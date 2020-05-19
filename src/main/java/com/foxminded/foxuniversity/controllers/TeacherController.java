package com.foxminded.foxuniversity.controllers;

import com.foxminded.foxuniversity.domain.Teacher;
import com.foxminded.foxuniversity.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/teachers")
public class TeacherController {
    private TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public String showAllTeachers(Model model) {
        List<Teacher> teachers = teacherService.getAll();
        model.addAttribute("teachers", teachers);
        return "teachers/teachers";
    }

    @GetMapping("/info")
    public String showTeacherById(Model model, int id) {
        Teacher teacher = teacherService.getById(id);
        model.addAttribute(teacher);
        return "teachers/teacher-info";
    }
}
