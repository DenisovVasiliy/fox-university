package com.foxminded.foxuniversity.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "courses")
public class Course {
    @NonNull
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NonNull
    @Column
    private String name;
    @NonNull
    @Column
    private String description;
    @ToString.Exclude
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<Lesson> lessons = new ArrayList<>();
    @ToString.Exclude
    private List<Group> groups = new ArrayList<>();

    public Course() {}

    public Course(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Course(int id) {
        this.id = id;
    }
}
