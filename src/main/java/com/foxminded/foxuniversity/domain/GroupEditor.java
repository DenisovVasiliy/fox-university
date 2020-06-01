package com.foxminded.foxuniversity.domain;

import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;

import static java.lang.Integer.parseInt;

public class GroupEditor extends PropertyEditorSupport {
    @Override
    public String getAsText() {
        Group group = (Group) getValue();
        return group == null ? "" : group.toString();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.isEmpty(text)) {
            setValue(null);
        } else {
            int parsedId = parseInt(text.trim());
            Group group = new Group(parsedId);
            setValue(group);
        }
    }
}
