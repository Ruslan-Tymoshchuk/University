package ua.com.rtim.university.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Student {

	private int studentId;
	private Group group;
	private String firstName;
	private String lastName;
	private Set<Course> courses = new HashSet<>();

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Set<Course> getCourses() {
		return courses;
	}

	public void setCourse(Course course) {
		this.courses.add(course);
	}

	@Override
	public int hashCode() {
		return Objects.hash(firstName, group, lastName, studentId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Student other = (Student) obj;
		return Objects.equals(firstName, other.firstName) && Objects.equals(group, other.group)
				&& Objects.equals(lastName, other.lastName) && studentId == other.studentId;
	}
}