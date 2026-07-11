package com.ocean.demoOracleJUnit.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "TBLSTUDENT")
public class tblStudent {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_seq")
	@SequenceGenerator(name = "student_seq", sequenceName = "STUDENT_SEQ", allocationSize = 1)
	@Column(name = "STUDENT_ID")
	private Long studentId;

	@Column(name = "FIRST_NAME", length = 100, nullable = false)
	private String firstName;

	@Column(name = "LAST_NAME", length = 100, nullable = false)
	private String lastName;

	@Column(name = "EMAIL", length = 50, unique = true)
	private String email;

	@Column(name = "ENROLLED_DATE")
	private LocalDate enrolledDate;

	// ===== Getters & Setters =====

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getEnrolledDate() {
		return enrolledDate;
	}

	public void setEnrolledDate(LocalDate enrolledDate) {
		this.enrolledDate = enrolledDate;
	}
}
