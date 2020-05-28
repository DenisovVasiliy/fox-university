package com.foxminded.foxuniversity.controllers.dto;

import com.foxminded.foxuniversity.domain.Course;
import com.foxminded.foxuniversity.domain.Group;

import java.util.ArrayList;
import java.util.List;

public class GroupAssignmentDTO {
    private Group group = new Group();
    private List<Course> courses = new ArrayList<>();

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public void addCoursesByCounter(int counter) {
        for (int i = 0; i < counter; i++) {
            courses.add(new Course());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroupAssignmentDTO)) return false;

        GroupAssignmentDTO groupDTO = (GroupAssignmentDTO) o;

        if (!group.equals(groupDTO.group)) return false;
        return courses.equals(groupDTO.courses);
    }

    @Override
    public int hashCode() {
        int result = group.hashCode();
        result = 31 * result + courses.hashCode();
        return result;
    }
}
