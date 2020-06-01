package com.foxminded.foxuniversity.controllers.dto;

import com.foxminded.foxuniversity.domain.Group;
import com.foxminded.foxuniversity.domain.Lesson;

import java.util.ArrayList;
import java.util.List;

public class LessonAssignmentDTO {
    Lesson lesson = new Lesson();
    List<Group> groups = new ArrayList<>();

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LessonAssignmentDTO)) return false;

        LessonAssignmentDTO that = (LessonAssignmentDTO) o;

        if (!lesson.equals(that.lesson)) return false;
        return groups.equals(that.groups);
    }

    @Override
    public int hashCode() {
        int result = lesson.hashCode();
        result = 31 * result + groups.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "LessonAssignmentDTO{" +
                "lesson=" + lesson +
                ", groups=" + groups +
                '}';
    }
}
