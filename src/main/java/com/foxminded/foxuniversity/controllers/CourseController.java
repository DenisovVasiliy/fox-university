package com.foxminded.foxuniversity.controllers;

import com.foxminded.foxuniversity.domain.*;
import com.foxminded.foxuniversity.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseController {
    private CourseService courseService;
    private TeacherService teacherService;

    @Autowired
    public CourseController(CourseService courseService, TeacherService teacherService) {
        this.courseService = courseService;
        this.teacherService = teacherService;
    }

    @GetMapping
    public String showAllCourses(Model model) {
        List<Course> courses = courseService.getAll();
        model.addAttribute("courses", courses);
        return "courses/courses";
    }

    @GetMapping("/info")
    public String showCourseById(Model model, int id) {
        Course course = courseService.getById(id);
        if (course != null) {
            List<Teacher> teachers = teacherService.getByCourse(course);
            model.addAttribute("course", course);
            model.addAttribute("teachers", teachers);
            return "courses/courseInfo";
        }
        model.addAttribute("entity", "course");
        model.addAttribute("id", id);
        return "notFound";
    }

}
