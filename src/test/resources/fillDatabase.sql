insert into courses (name, description) values ('C-01', 'C-01 course'), ('C-02', 'C-02 course'), ('C-03', 'C-03 course');

insert into groups (name) values ('gr-01'), ('gr-02'), ('gr-03');

insert into students (first_name, last_name) values ('S-01', 'Student'), ('S-02', 'Student'), ('S-03', 'Student'), ('S-04', 'Student');

insert into teachers (first_name, last_name, course_id) values ('T-01', 'Teacher', 1), ('T-02', 'Teacher', 2), ('T-03', 'Teacher', 2);

insert into lessons (course_id, teacher_id, classroom, day, time, type) values (1, 1, 10, 'MONDAY', '09:30:00', 'LECTURE'), (2, 1, 10, 'FRIDAY', '09:30:00', 'LECTURE'), (2, 2, 20, 'FRIDAY', '09:30:00', 'LECTURE');

insert into students_groups (student_id, group_id) values (1, 1), (2, 1), (3, 2);

insert into groups_courses (group_id, course_id) values (1, 1), (1, 2), (2, 2), (3, 3);

insert into lessons_groups (lesson_id, group_id) values (1, 1), (1, 2), (2, 3);