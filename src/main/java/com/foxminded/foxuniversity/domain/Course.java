package com.foxminded.foxuniversity.domain;

import java.util.List;

public class Course {
    private int id;
    private String name;
    private String description;
    private List<Lesson> lessons;
    private List<Group> groups;

    public Course(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Course(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public  void addLessons(List<Lesson> lessons) {
        for (Lesson lesson : lessons) {
            if (!this.lessons.contains(lesson)) {
                this.lessons.add(lesson);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;

        Course course = (Course) o;

        if (id != course.id) return false;
        if (!name.equals(course.name)) return false;
        return description.equals(course.description);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + description.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "\nCourse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
