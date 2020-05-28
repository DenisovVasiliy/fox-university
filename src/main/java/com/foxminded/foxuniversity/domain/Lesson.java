package com.foxminded.foxuniversity.domain;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class Lesson {
    private int id;
    private Course course;
    private List<Group> groups = new ArrayList<>();
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

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public void setType(LessonsType type) {
        this.type = type;
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

        return id == lesson.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", course=" + course +
                ", groups=" + groups +
                ", teacher=" + teacher +
                ", classroom=" + classroom +
                ", day=" + day +
                ", startTime=" + startTime +
                ", type=" + type +
                '}';
    }
}
