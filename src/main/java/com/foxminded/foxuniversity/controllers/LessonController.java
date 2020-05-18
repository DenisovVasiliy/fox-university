package com.foxminded.foxuniversity.controllers;

import com.foxminded.foxuniversity.domain.Lesson;
import com.foxminded.foxuniversity.service.LessonService;
import com.foxminded.foxuniversity.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static java.lang.Integer.parseInt;

@Controller
@RequestMapping("/timetable")
@PropertySource("classpath:lessonsTime.properties")
public class LessonController {
    private LessonService lessonService;
    private TeacherService teacherService;

    @Value("#{'${time}'.split(',')}")
    private List<String> times;

    @Autowired
    public LessonController(LessonService lessonService, TeacherService teacherService) {
        this.lessonService = lessonService;
        this.teacherService = teacherService;
    }
// It's full ***
    @GetMapping
    public String getTeacherTimetable(Model model) {
        List<Lesson> lessons = lessonService.getByTeacher(teacherService.getById(parseInt("1")));
        model.addAttribute("times", times);
        return "lessons/timetable";

    }
}
