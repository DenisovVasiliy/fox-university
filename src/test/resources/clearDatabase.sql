delete from lessons_groups;

delete from groups_courses;

delete from students_groups;

delete from lessons;
ALTER TABLE lessons ALTER COLUMN id RESTART WITH 1;

delete from teachers;
ALTER TABLE teachers ALTER COLUMN id RESTART WITH 1;

delete from students;
ALTER TABLE students ALTER COLUMN id RESTART WITH 1;

delete from groups;
ALTER TABLE groups ALTER COLUMN id RESTART WITH 1;

delete from courses;
ALTER TABLE courses ALTER COLUMN id RESTART WITH 1;