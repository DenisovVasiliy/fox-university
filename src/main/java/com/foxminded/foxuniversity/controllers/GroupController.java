package com.foxminded.foxuniversity.controllers;

import com.foxminded.foxuniversity.controllers.dto.GroupAssignmentDTO;
import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.service.CourseService;
import com.foxminded.foxuniversity.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

import static com.foxminded.foxuniversity.controllers.util.Constants.*;
import static com.foxminded.foxuniversity.controllers.util.Constants.DANGER;
import static java.lang.String.format;

@Controller
@RequestMapping("/groups")
public class GroupController {
    private GroupService groupService;
    private CourseService courseService;

    private static final String REDIRECT_TO_INFO_PAGE = "redirect:/groups/info/?id=%s&hint=%s";
    private static final String DELETION_CANCELLED = "Deletion cancelled: something went wrong...";

    @Autowired
    public GroupController(GroupService groupService, CourseService courseService) {
        this.groupService = groupService;
        this.courseService = courseService;
    }

    @GetMapping
    public String showAllGroups(Model model,
                                @RequestParam(value = "hint", required = false) String hint) {
        if (hint != null) {
            model.addAttribute(hint, format(SUCCESSFULLY_DELETED, "Group"));
        }
        List<Group> groups = groupService.getAll();
        model.addAttribute("groups", groups);
        model.addAttribute("newGroup", new Group());
        return "groups/groups";
    }

    @GetMapping("/info")
    public String showGroupById(Model model, int id,
                                @RequestParam(value = "hint", required = false) String hint) {
        if (hint != null) {
            if (hint.equals(SUCCESS)) {
                model.addAttribute(hint, SUCCESSFULLY_SAVED);
            } else {
                model.addAttribute(hint, DELETION_CANCELLED);
            }
        }
        Group group = groupService.getById(id);
        List<Course> courses = courseService.getByGroup(group);
        GroupAssignmentDTO groupDto = new GroupAssignmentDTO();
        groupDto.setGroup(group);
        model.addAttribute("group", group);
        model.addAttribute("courses", courses);
        model.addAttribute("groupDto", groupDto);
        return "groups/group-info";
    }

    @GetMapping("/assign")
    public String showAssignGroupPage(Model model, int id) {
        Group group = groupService.getById(id);
        List<Course> groupsCourses = courseService.getByGroup(group);
        List<Course> allCourses = courseService.getAll();
        GroupAssignmentDTO groupDTO = new GroupAssignmentDTO();
        groupDTO.setGroup(group);
        groupDTO.addCoursesByCounter(allCourses.size());
        model.addAttribute("groupsCourses", groupsCourses);
        model.addAttribute("courses", allCourses);
        model.addAttribute("groupDTO", groupDTO);
        return "groups/groups-assign";
    }

    @PostMapping("/new")
    public ModelAndView saveGroup(@ModelAttribute("newGroup") Group group) {
        groupService.save(group);
        String redirect = format(REDIRECT_TO_INFO_PAGE, group.getId(), SUCCESS);
        return new ModelAndView(redirect);
    }

    @PostMapping("/edit")
    public ModelAndView updateGroup(@ModelAttribute("group") Group group) {
        groupService.update(group);
        String redirect = format(REDIRECT_TO_INFO_PAGE, group.getId(), SUCCESS);
        return new ModelAndView(redirect);
    }

    @PostMapping("/delete")
    public ModelAndView deleteGroup(@ModelAttribute("group") Group group) {
        if (groupService.delete(group)) {
            String redirect = "redirect:/groups/" + format(HINT, SUCCESS);
            return new ModelAndView(redirect);
        }
        String redirect = format(REDIRECT_TO_INFO_PAGE, group.getId(), DANGER);
        return new ModelAndView(redirect);
    }

    @PostMapping("/assign")
    public ModelAndView assignGroup(@ModelAttribute("groupDTO") GroupAssignmentDTO groupDTO) {
        List<Course> courses = groupDTO.getCourses().stream().
                filter(course -> course.getId() != 0).collect(Collectors.toList());
        groupService.assignToCourses(groupDTO.getGroup(), courses);
        String redirect = format(REDIRECT_TO_INFO_PAGE, groupDTO.getGroup().getId(), SUCCESS);
        return new ModelAndView(redirect);
    }

    @PostMapping("/assign/delete")
    public ModelAndView deleteGroupFromCourse(int groupId, int courseId) {
        Group group = new Group(groupId);
        Course course = courseService.getById(courseId);
        groupService.deleteFromCourse(group, course);
        String redirect = format(REDIRECT_TO_INFO_PAGE, group.getId(), SUCCESS);
        return new ModelAndView(redirect);
    }
}
