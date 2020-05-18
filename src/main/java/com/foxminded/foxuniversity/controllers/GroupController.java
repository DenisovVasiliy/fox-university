package com.foxminded.foxuniversity.controllers;

import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.service.CourseService;
import com.foxminded.foxuniversity.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/groups")
public class GroupController {
    private GroupService groupService;
    private CourseService courseService;

    @Autowired
    public GroupController(GroupService groupService, CourseService courseService) {
        this.groupService = groupService;
        this.courseService = courseService;
    }

    @GetMapping
    public String showAllGroups(Model model) {
        List<Group> groups = groupService.getAll();
        model.addAttribute("groups", groups);
        return "groups/groups";
    }

    @GetMapping("/info")
    public String showGroupById(Model model, int id) {
        Group group = groupService.getById(id);
        List<Course> courses = courseService.getByGroup(group);
        model.addAttribute("group", group);
        model.addAttribute("courses", courses);
        return "groups/groupInfo";
    }
}
