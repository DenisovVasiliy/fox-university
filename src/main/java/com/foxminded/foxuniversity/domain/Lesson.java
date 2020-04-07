package com.foxminded.foxuniversity.domain;

import java.sql.Time;
import java.util.List;

public class Lesson {
    private int id;
    private Course course;
    private List<Group> groups;
    private Teacher teacher;
    private int classroom;
    private Day day;
    private Time startTime;
    private LessonsType type;

    public Lesson(int id, Course course, Teacher teacher,
                  int classroom, Day day, Time startTime, LessonsType type) {
        this.id = id;
        this.course = course;
        this.teacher = teacher;
        this.classroom = classroom;
        this.day = day;
        this.startTime = startTime;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public Day getDay() {
        return day;
    }

    public Time getStartTime() {
        return startTime;
    }

    public LessonsType getType() {
        return type;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public int getClassroom() {
        return classroom;
    }

    public void setClassroom(int classroom) {
        this.classroom = classroom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lesson)) return false;

        Lesson lesson = (Lesson) o;

        if (id != lesson.id) return false;
        if (!course.equals(lesson.course)) return false;
        if (classroom != lesson.classroom) return false;
        if (day != lesson.day) return false;
        if (!startTime.equals(lesson.startTime)) return false;
        return type == lesson.type;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + course.hashCode();
        result = 31 * result + groups.hashCode();
        result = 31 * result + day.hashCode();
        result = 31 * result + startTime.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", course=" + course.getName() +
                ", groups=" + groups +
                ", teacher=" + teacher +
                ", classroom=" + classroom +
                ", day=" + day +
                ", startTime=" + startTime +
                ", type=" + type +
                '}';
    }
}
