CREATE TABLE IF NOT EXISTS groups
(
  id SERIAL PRIMARY KEY,
  name VARCHAR(30) NOT NULL UNIQUE CHECK (name != '')
);

CREATE TABLE IF NOT EXISTS students
(
  id SERIAL PRIMARY KEY,
  first_name VARCHAR(30) NOT NULL CHECK (first_name != ''),
  last_name VARCHAR(30) NOT NULL CHECK (last_name != '')
);

CREATE TABLE IF NOT EXISTS courses
(
  id SERIAL PRIMARY KEY,
  name VARCHAR(30) NOT NULL CHECK (name != ''),
  description VARCHAR(200) NOT NULL CHECK (description != '')
);

CREATE TABLE IF NOT EXISTS groups_courses
(
  group_id INTEGER,
  course_id INTEGER,
  UNIQUE (group_id , course_id),
  FOREIGN KEY (group_id) REFERENCES groups(id) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (course_id) REFERENCES courses(id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS students_groups
(
  student_id INTEGER UNIQUE,
  group_id INTEGER,
  FOREIGN KEY (student_id) REFERENCES students(id) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (group_id) REFERENCES groups(id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS teachers
(
  id SERIAL PRIMARY KEY,
  first_name VARCHAR(30) NOT NULL CHECK (first_name != ''),
  last_name VARCHAR(30) NOT NULL CHECK (last_name != ''),
  course_id INTEGER,
  FOREIGN KEY (course_id) REFERENCES courses(id) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS lessons
(
  id SERIAL PRIMARY KEY,
  teacher_id INTEGER,
  classroom INTEGER,
  day VARCHAR(30) NOT NULL CHECK (day != ''),
  time TIME NOT NULL,
  type VARCHAR(30) NOT NULL CHECK (type != ''),
  FOREIGN KEY (teacher_id) REFERENCES teachers(id) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS lessons_courses
(
  course_id INTEGER,
  lesson_id INTEGER UNIQUE,
  FOREIGN KEY (course_id) REFERENCES courses(id) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (lesson_id) REFERENCES lessons(id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS lessons_groups
(
  group_id INTEGER,
  lesson_id INTEGER,
  UNIQUE (group_id , lesson_id),
  FOREIGN KEY (group_id) REFERENCES groups(id) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (lesson_id) REFERENCES lessons(id) ON UPDATE CASCADE ON DELETE CASCADE
);