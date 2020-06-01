package com.foxminded.foxuniversity.domain;

import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;

import static java.lang.Integer.parseInt;

public class CourseEditor extends PropertyEditorSupport {
    @Override
    public String getAsText() {
        Course course = (Course) getValue();
        return course == null ? "" : course.toString();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.isEmpty(text)) {
            setValue(null);
        } else {
                int parsedId = parseInt(text.trim());
                Course course = new Course(parsedId);
                setValue(course);
        }
    }
}
