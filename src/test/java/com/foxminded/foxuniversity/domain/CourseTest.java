package com.foxminded.foxuniversity.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CourseTest {
    private static Course course = new Course(1, "Test course", "Course for testing.");
    private static Group group = new Group(1, "test");
    private static List<Group> groups = Collections.singletonList(group);
    private static List<Lesson> lessons = new ArrayList<>();

    @BeforeAll
    public static void prepare() {
        for (int i = 0; i < 3; i++) {
            lessons.add(new Lesson(i + 1, course, groups, Day.MONDAY,
                    new Time((i + 9), 0, 0), LessonsType.LECTURE));
        }
    }

    @Test
    public void shouldAddOneOfTwoPassedLessons () {
        List<Lesson> preparedLessons = new ArrayList<>();
        preparedLessons.add(lessons.get(0));
        preparedLessons.add(lessons.get(1));
        course.setLessons(preparedLessons);

        assertEquals(preparedLessons.size(), course.getLessons().size());
        assertTrue(course.getLessons().containsAll(preparedLessons));

        List<Lesson> lessonsToAdd = new ArrayList<>();
        lessonsToAdd.add(lessons.get(1));
        lessonsToAdd.add(lessons.get(2));

        course.addLessons(lessonsToAdd);

        assertEquals(lessons.size(), course.getLessons().size());
        assertTrue(course.getLessons().containsAll(lessons));
    }
}