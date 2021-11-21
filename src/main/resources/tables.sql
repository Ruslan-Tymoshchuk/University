DROP TABLE IF EXISTS groups CASCADE;
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS courses CASCADE;
DROP TABLE IF EXISTS students_courses CASCADE;
   
CREATE TABLE groups (
    group_id SERIAL PRIMARY KEY,
    group_name VARCHAR NOT NULL
);

CREATE TABLE students (
    student_id SERIAL PRIMARY KEY,
    group_id INTEGER REFERENCES groups(group_id) ON DELETE CASCADE ON UPDATE CASCADE,
    first_name VARCHAR NOT NULL,
    last_name VARCHAR NOT NULL
);
    
CREATE TABLE courses (   
    course_id SERIAL PRIMARY KEY,
    course_name VARCHAR NOT NULL,
    course_description VARCHAR DEFAULT 'The best science'
);

CREATE TABLE students_courses(
    student_id INTEGER REFERENCES students(student_id) ON DELETE CASCADE ON UPDATE CASCADE,
    course_id INTEGER REFERENCES courses(course_id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (student_id, course_id),
    UNIQUE (student_id, course_id)
);


