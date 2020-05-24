package com.foxminded.foxuniversity.controllers;

import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Teacher;
import com.foxminded.foxuniversity.service.CourseService;
import com.foxminded.foxuniversity.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static java.lang.String.format;

@Controller
@RequestMapping("/teachers")
public class TeacherController {
    private TeacherService teacherService;
    private CourseService courseService;
    private static final String REDIRECT_TO_INFO_PAGE = "redirect:/teachers/info/?id=%s";
    private static final String DELETION_CANCELLED = "Deletion was cancelled: the teacher is assigned to some classes.";

    @Autowired
    public TeacherController(TeacherService teacherService, CourseService courseService) {
        this.teacherService = teacherService;
        this.courseService = courseService;
    }

    @GetMapping
    public String showAllTeachers(Model model) {
        List<Teacher> teachers = teacherService.getAll();
        model.addAttribute("teachers", teachers);
        return "teachers/teachers";
    }

    @GetMapping("/info")
    public String showTeacherById(Model model, int id,
                                  @RequestParam(value = "deletionWarning", required = false) Boolean deletionWarning) {
        if (deletionWarning != null && deletionWarning) {
            model.addAttribute("message", DELETION_CANCELLED);
        }
        Teacher teacher = teacherService.getById(id);
        model.addAttribute(teacher);
        return "teachers/teacher-info";
    }

    @GetMapping("/new")
    public String showNewTeacherPage(Model model) {
        List<Course> courses = courseService.getAll();
        model.addAttribute("newTeacher", new Teacher());
        model.addAttribute("courses", courses);
        return "teachers/teacher-new";
    }

    @GetMapping("/edit")
    public String showEditTeacherPage(Model model, int id) {
        Teacher teacher = teacherService.getById(id);
        List<Course> courses = courseService.getAll();
        model.addAttribute("teacher", teacher);
        model.addAttribute("courses", courses);
        return "teachers/teacher-edit";
    }

    @PostMapping("/new")
    public ModelAndView saveTeacher(@ModelAttribute("newTeacher") Teacher teacher) {
        teacherService.save(teacher);
        String redirect = format(REDIRECT_TO_INFO_PAGE, teacher.getId());
        return new ModelAndView(redirect);
    }

    @PostMapping("/edit")
    public ModelAndView updateTeacher(@ModelAttribute("teacher") Teacher teacher) {
        teacherService.update(teacher);
        String redirect = format(REDIRECT_TO_INFO_PAGE, teacher.getId());
        return new ModelAndView(redirect);
    }

    @PostMapping("/delete")
    public ModelAndView deleteCourse(@ModelAttribute("teacher") Teacher teacher) {
        if (teacherService.delete(teacher)) {
            return new ModelAndView("redirect:/teachers/");
        }
        String redirect = format(REDIRECT_TO_INFO_PAGE, teacher.getId() + "&deletionWarning=true");
        return new ModelAndView(redirect);
    }
}
