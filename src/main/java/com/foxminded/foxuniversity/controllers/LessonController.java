package com.foxminded.foxuniversity.controllers;

import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Lesson;
import com.foxminded.foxuniversity.service.CourseService;
import com.foxminded.foxuniversity.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static java.lang.String.format;

@Controller
@RequestMapping("/lessons")
public class LessonController {
    private CourseService courseService;
    private LessonService lessonService;

    @Autowired
    public LessonController(CourseService courseService, LessonService lessonService) {
        this.courseService = courseService;
        this.lessonService = lessonService;
    }

    @GetMapping
    public String showAllLessons(Model model) {
        List<Lesson> lessons = lessonService.getAll();
        model.addAttribute("title", "All lessons");
        model.addAttribute("lessons", lessons);
        return "lessons/lessons";
    }

    @GetMapping("/by-course")
    public String showLessonsByCourse(Model model, int courseId) {
        Course course = courseService.getById(courseId);
        String title = format("Lessons of course %s", course.getName());
        model.addAttribute("title", title);
        model.addAttribute("lessons", course.getLessons());
        return "lessons/lessons";
    }

    @GetMapping("/info")
    public String showLessonById(Model model, int id) {
        Lesson lesson = lessonService.getById(id);
        model.addAttribute("lesson", lesson);
        return "lessons/lesson-info";
    }
}
