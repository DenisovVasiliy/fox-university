package com.foxminded.foxuniversity.controllers;

import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Lesson;
import com.foxminded.foxuniversity.domain.Teacher;
import com.foxminded.foxuniversity.service.CourseService;
import com.foxminded.foxuniversity.service.LessonService;
import com.foxminded.foxuniversity.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;

import static com.foxminded.foxuniversity.controllers.util.Constants.*;
import static java.lang.String.format;
import static java.util.Collections.singletonList;

@Controller
@RequestMapping("/teachers")
public class TeacherController {
    private TeacherService teacherService;
    private CourseService courseService;
    private LessonService lessonService;
    private static final String REDIRECT_TO_INFO_PAGE = "redirect:/teachers/info/?id=%s&hint=%s";
    private static final String DELETION_CANCELLED = "Deletion was cancelled: the teacher is assigned to some classes.";

    @Autowired
    public TeacherController(TeacherService teacherService,
                             CourseService courseService,
                             LessonService lessonService) {
        this.teacherService = teacherService;
        this.courseService = courseService;
        this.lessonService = lessonService;
    }

    @GetMapping
    public String showAllTeachers(Model model, @RequestParam(value = "hint", required = false) String hint) {
        if (hint != null) {
            model.addAttribute(hint, format(SUCCESSFULLY_DELETED, "Teacher"));
        }
        List<Teacher> teachers = teacherService.getAll();
        model.addAttribute("teachers", teachers);
        return "teachers/teachers";
    }

    @GetMapping("/info")
    public String showTeacherById(Model model, int id,
                                  @RequestParam(value = "hint", required = false) String hint) {
        if (hint != null) {
            if (hint.equals(SUCCESS)) {
                model.addAttribute(hint, SUCCESSFULLY_SAVED);
            } else {
                model.addAttribute(hint, DELETION_CANCELLED);
            }
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
        if (lessonService.getByTeacher(teacher).isEmpty()) {
            List<Course> courses = courseService.getAll();
            model.addAttribute("courses", courses);
        } else {
            model.addAttribute("courses", singletonList(teacher.getCourse()));
            model.addAttribute(WARNING, TEACHERS_COURSE_CHANGE_WARNING);
        }
        model.addAttribute("teacher", teacher);
        return "teachers/teacher-edit";
    }

    @PostMapping("/new")
    public ModelAndView saveTeacher(@ModelAttribute("newTeacher") Teacher teacher) {
        teacherService.save(teacher);
        String redirect = format(REDIRECT_TO_INFO_PAGE, teacher.getId(), SUCCESS);
        return new ModelAndView(redirect);
    }

    @PostMapping("/edit")
    public ModelAndView updateTeacher(@ModelAttribute("teacher") Teacher teacher) {
        teacherService.update(teacher);
        String redirect = format(REDIRECT_TO_INFO_PAGE, teacher.getId(), SUCCESS);
        return new ModelAndView(redirect);
    }

    @PostMapping("/editWithCourse")
    public ModelAndView updateTeacherWithCourse(@ModelAttribute("teacher") Teacher teacher) {
        teacherService.updateWithCourse(teacher);
        String redirect = format(REDIRECT_TO_INFO_PAGE, teacher.getId(), SUCCESS);
        return new ModelAndView(redirect);
    }

    @PostMapping("/delete")
    public ModelAndView deleteCourse(@ModelAttribute("teacher") Teacher teacher) {
        if (teacherService.delete(teacher)) {
            return new ModelAndView("redirect:/teachers/" + format(HINT, SUCCESS));
        }
        String redirect = format(REDIRECT_TO_INFO_PAGE, teacher.getId(), DANGER);
        return new ModelAndView(redirect);
    }
}
