package ua.com.rtim.university.util;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ua.com.rtim.university.domain.Course;
import ua.com.rtim.university.domain.Group;
import ua.com.rtim.university.domain.Student;

public class DataGenerator {

	public DataGeneratorDao generateRandomData() {
		List<Group> groups = getGeneratedGroups(10);
		List<Course> courses = getGeneratedCourses();
		List<Student> students = getGeneratedStudents(200);
		students.forEach(student -> student.setGroup(groups.get(current().nextInt(groups.size()))));
		students = assignStudentsToGroups(students, 10, 30);
		bindCourses(students, courses);
		return new DataGeneratorDao(students, courses, groups);
	}

	private List<Group> getGeneratedGroups(int amount) {
		List<Group> groups = new ArrayList<>();
		IntStream.range(0, amount).forEach(s -> {
			String characters = randomAlphabetic(2).toUpperCase();
			String numbers = randomNumeric(2);
			Group group = new Group();
			group.setName(String.format("%s-%s", characters, numbers));
			groups.add(group);
		});
		return groups;
	}

	private List<Course> getGeneratedCourses() {
		List<String> coursesNames = Arrays.asList("HYSTORY", "ENGLISH", "FRENCH", "BUSINESS", "PHILOSOPHY", "SOCIOLOGY",
				"CHEMISTRY", "PHYSICS", "MATHEMATICS", "ANTHROPOLOGY");
		List<Course> courses = new ArrayList<>();
		IntStream.range(0, coursesNames.size()).forEach(s -> {
			Course course = new Course();
			course.setName(coursesNames.get(s));
			courses.add(course);
		});
		return courses;
	}

	private List<Student> getGeneratedStudents(int amount) {
		List<String> firstNames = Arrays.asList("Molly", "Emily", "Katie", "Madeline", "Claire", "Emma", "Abigail",
				"Kiara", "Jasmine", "Carly", "Dustin", "Logan", "Dylan", "Hunter", "Marquis", "Darnell", "Willie",
				"Dominique", "Darius", "Xavier");
		List<String> lastNames = Arrays.asList("Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller",
				"Davis", "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas",
				"Taylor", "Moore", "Jackson", "Martin");
		List<Student> students = new ArrayList<>();
		IntStream.range(0, amount).forEach(s -> {
			Student student = new Student();
			String studentFirstName = firstNames.get(current().nextInt(firstNames.size()));
			String studentLastName = lastNames.get(current().nextInt(lastNames.size()));
			student.setFirstName(studentFirstName);
			student.setLastName(studentLastName);
			students.add(student);
		});
		return students;
	}

	private List<Student> assignStudentsToGroups(List<Student> allStudents, int minGroupSize, int maxGroupSize) {
		List<Student> students = new ArrayList<>();
		allStudents.stream().collect(Collectors.groupingBy(Student::getGroup)).forEach((group, studentsList) -> {
			if (studentsList.size() >= minGroupSize && studentsList.size() <= maxGroupSize) {
				students.addAll(studentsList);
			}
		});
		return students;
	}

	private List<Student> bindCourses(List<Student> allStudents, List<Course> courses) {
		List<Student> students = new ArrayList<>();
		allStudents.forEach(student -> {
			IntStream.range(0, current().nextInt(1, 4))
					.forEach(s -> student.setCourse(courses.get(current().nextInt(courses.size()))));
			students.add(student);
		});
		return students;
	}
}