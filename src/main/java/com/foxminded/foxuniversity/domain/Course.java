package com.foxminded.foxuniversity.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Course {
    @NonNull
    @EqualsAndHashCode.Include
    private int id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @ToString.Exclude
    private List<Lesson> lessons = new ArrayList<>();
    @ToString.Exclude
    private List<Group> groups = new ArrayList<>();

    public Course(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Course(int id) {
        this.id = id;
    }
}
