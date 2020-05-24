package com.foxminded.foxuniversity.controllers;

import com.foxminded.foxuniversity.domain.*;
import com.foxminded.foxuniversity.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static java.lang.String.format;

@Controller
@RequestMapping("/courses")
public class CourseController {
    private CourseService courseService;
    private TeacherService teacherService;

    private static final String REDIRECT_TO_INFO_PAGE = "redirect:/courses/info/?id=%s";
    private static final String DELETION_CANCELLED = "Deletion was cancelled: there are some teachers on the course";

    @Autowired
    public CourseController(CourseService courseService, TeacherService teacherService) {
        this.courseService = courseService;
        this.teacherService = teacherService;
    }

    @GetMapping
    public String showAllCourses(Model model) {
        List<Course> courses = courseService.getAll();
        model.addAttribute("courses", courses);
        model.addAttribute("newCourse", new Course());
        return "courses/courses";
    }

    @GetMapping("/info")
    public String showCourseById(Model model, int id,
                                 @RequestParam(value = "deletionWarning", required = false) Boolean deletionWarning) {
        if (deletionWarning != null && deletionWarning) {
            model.addAttribute("message", DELETION_CANCELLED);
        }
        Course course = courseService.getById(id);
        List<Teacher> teachers = teacherService.getByCourse(course);
        model.addAttribute("course", course);
        model.addAttribute("teachers", teachers);
        return "courses/course-info";
    }

    @PostMapping("/new")
    public ModelAndView saveCourse(@ModelAttribute("newCourse") Course course) {
        courseService.save(course);
        String redirect = format(REDIRECT_TO_INFO_PAGE, course.getId());
        return new ModelAndView(redirect);
    }

    @PostMapping("/delete")
    public ModelAndView deleteCourse(@ModelAttribute("course") Course course) {
        if (courseService.delete(course)) {
            return new ModelAndView("redirect:/courses/");
        }
        String redirect = format(REDIRECT_TO_INFO_PAGE, course.getId() + "&deletionWarning=true");
        return new ModelAndView(redirect);
    }

    @PostMapping("/edit")
    public ModelAndView updateStudent(@ModelAttribute("course") Course course, int id) {
        if (course.getId() == id) {
            courseService.update(course);
        }
        String redirect = format(REDIRECT_TO_INFO_PAGE, course.getId());
        return new ModelAndView(redirect);
    }
}
