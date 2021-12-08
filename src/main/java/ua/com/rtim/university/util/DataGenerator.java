package ua.com.rtim.university.util;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import ua.com.rtim.university.domain.Course;
import ua.com.rtim.university.domain.Group;
import ua.com.rtim.university.domain.Student;

public class DataGenerator {

	public static final String SPACE_DELIMITER = " ";
	public static final String COURSES_NAMES = "HYSTORY ENGLISH FRENCH BUSINESS PHILOSOPHY SOCIOLOGY "
			+ "CHEMISTRY PHYSICS MATHEMATICS ANTHROPOLOGY";
	public static final String FIRST_NAMES = "Molly Emily Katie Madeline Claire Emma Abigail Kiara "
			+ "Jasmine Carly Dustin Logan Dylan Hunter Marquis Darnell Willie Dominique Darius Xavier";
	public static final String LAST_NAMES = "Smith Johnson Williams Brown Jones Garcia Miller Davis Rodriguez "
			+ "Martinez Hernandez Lopez Gonzalez Wilson Anderson Thomas Taylor Moore Jackson Martin";

	public List<Group> getGeneratedGroups(int amount) {
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

	public List<Course> getGeneratedCourses() {
		List<String> coursesNames = Arrays.asList(COURSES_NAMES.split(SPACE_DELIMITER));
		List<Course> courses = new ArrayList<>();
		IntStream.range(0, coursesNames.size()).forEach(s -> {
			Course course = new Course();
			course.setName(coursesNames.get(s));
			course.setDescription("The best science");
			courses.add(course);
		});
		return courses;
	}

	public List<Student> getGeneratedStudents(int amount) {
		List<String> firstNames = Arrays.asList(FIRST_NAMES.split(SPACE_DELIMITER));
		List<String> lastNames = Arrays.asList(LAST_NAMES.split(SPACE_DELIMITER));
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
}