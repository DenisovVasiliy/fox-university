package com.foxminded.foxuniversity.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;

import static java.lang.Integer.parseInt;

@Slf4j
public class TeacherEditor extends PropertyEditorSupport {
    @Override
    public String getAsText() {
        Teacher teacher = (Teacher) getValue();
        return teacher == null ? "" : teacher.toString();
    }

    @Override
    public void setAsText(String text) {
        if (StringUtils.isEmpty(text)) {
            setValue(null);
        } else {
            int parsedId = parseInt(text.trim());
            Teacher teacher = new Teacher(parsedId);
            setValue(teacher);
        }
    }
}
