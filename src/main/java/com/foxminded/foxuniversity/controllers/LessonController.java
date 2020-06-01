package com.foxminded.foxuniversity.controllers;

import com.foxminded.foxuniversity.controllers.dto.LessonAssignmentDTO;
import com.foxminded.foxuniversity.domain.*;
import com.foxminded.foxuniversity.service.CourseService;
import com.foxminded.foxuniversity.service.LessonService;
import com.foxminded.foxuniversity.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static com.foxminded.foxuniversity.controllers.util.Constants.*;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Controller
@RequestMapping("/lessons")
@PropertySource("classpath:lessonsTime.properties")
public class LessonController {
    private CourseService courseService;
    private LessonService lessonService;
    private TeacherService teacherService;

    @Value("#{'${time}'.split(',')}")
    private List<String> times;

    private static final String REDIRECT_TO_INFO_PAGE = "redirect:/lessons/info/?id=%s&hint=%s";
    private static final String DELETION_CANCELLED = "Deletion was cancelled: something went wrong...";

    @Autowired
    public LessonController(CourseService courseService, LessonService lessonService,
                            TeacherService teacherService) {
        this.courseService = courseService;
        this.lessonService = lessonService;
        this.teacherService = teacherService;
    }

    @GetMapping
    public String showAllLessons(Model model, @RequestParam(value = "hint", required = false) String hint) {
        if (hint != null) {
            model.addAttribute(hint, format(SUCCESSFULLY_DELETED, "Lesson"));
        }
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
        model.addAttribute("course", course);
        model.addAttribute("lessons", course.getLessons());
        return "lessons/lessons";
    }

    @GetMapping("/info")
    public String showLessonById(Model model, int id,
                                 @RequestParam(value = "hint", required = false) String hint) {
        if (hint != null) {
            if (hint.equals(SUCCESS)) {
                model.addAttribute(hint, SUCCESSFULLY_SAVED);
            } else {
                model.addAttribute(hint, DELETION_CANCELLED);
            }
        }
        Lesson lesson = lessonService.getById(id);
        model.addAttribute("lesson", lesson);
        return "lessons/lesson-info";
    }

    @GetMapping("/new/by-course")
    public String showNewLessonPage(Model model, int courseId) {
        Lesson lesson = new Lesson();
        Course course = courseService.getById(courseId);
        List<Teacher> teachers = teacherService.getByCourse(course);
        model.addAttribute("lesson", lesson);
        model.addAttribute("days", Day.values());
        model.addAttribute("times", times);
        model.addAttribute("course", course);
        model.addAttribute("types", LessonsType.values());
        model.addAttribute("teachers", teachers);
        return "lessons/lesson-new";
    }

    @GetMapping("/edit")
    public String showEditLessonPage(Model model, int id) {
        Lesson lesson = lessonService.getById(id);
        List<Teacher> teachers = teacherService.getByCourse(lesson.getCourse());
        model.addAttribute("lesson", lesson);
        model.addAttribute("teachers", teachers);
        return "lessons/lesson-edit";
    }

    @GetMapping("/assign-groups")
    public String showAssignLessonPage(Model model, int id) {
        Lesson lesson = lessonService.getById(id);
        LessonAssignmentDTO lessonDto = new LessonAssignmentDTO();
        lessonDto.setLesson(lesson);
        model.addAttribute("lessonDTO", lessonDto);
        model.addAttribute("groups", lesson.getCourse().getGroups());
        return "lessons/lesson-assign";
    }

    @PostMapping("/delete")
    public ModelAndView deleteLesson(@ModelAttribute("lesson") Lesson lesson) {
        if (lessonService.delete(lesson)) {
            return new ModelAndView("redirect:/lessons/" + format(HINT, SUCCESS));
        }
        String redirect = format(REDIRECT_TO_INFO_PAGE, lesson.getId(), DANGER);
        return new ModelAndView(redirect);
    }

    @PostMapping("/new")
    public ModelAndView saveLesson(@ModelAttribute("lesson") Lesson lesson) {
        lessonService.save(lesson);
        String redirect = format(REDIRECT_TO_INFO_PAGE, lesson.getId(), SUCCESS);
        return new ModelAndView(redirect);
    }

    @PostMapping("/edit")
    public ModelAndView editLesson(@ModelAttribute("lesson") Lesson lesson) {
        lessonService.update(lesson);
        String redirect = format(REDIRECT_TO_INFO_PAGE, lesson.getId(), SUCCESS);
        return new ModelAndView(redirect);
    }

    @PostMapping("/assign-groups")
    public ModelAndView assignLesson(@ModelAttribute("lessonDTO") LessonAssignmentDTO lessonDTO) {
        List<Group> groups = lessonDTO.getGroups().stream().
                filter(group -> group.getId() != 0).collect(toList());
        lessonService.assignGroups(lessonDTO.getLesson(), groups);
        String redirect = format(REDIRECT_TO_INFO_PAGE, lessonDTO.getLesson().getId(), SUCCESS);
        return new ModelAndView(redirect);
    }

    @PostMapping("/assign/delete")
    public ModelAndView deleteAssignedGroup(int lessonId, int groupId) {
        Lesson lesson = lessonService.getById(lessonId);
        Group group = new Group(groupId);
        String redirect;
        if (lessonService.deleteGroup(lesson, group)) {
            redirect = format(REDIRECT_TO_INFO_PAGE, lesson.getId(), SUCCESS);
        } else {
            redirect = format(REDIRECT_TO_INFO_PAGE, lesson.getId(), WARNING);
        }
        return new ModelAndView(redirect);
    }
}
