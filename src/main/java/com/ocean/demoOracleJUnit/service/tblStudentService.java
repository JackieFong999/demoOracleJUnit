package com.ocean.demoOracleJUnit.service;

import com.ocean.demoOracleJUnit.model.tblStudent;
import java.util.List;
import java.util.Optional;

public interface tblStudentService {

	/** Create a new student */
	tblStudent createStudent(tblStudent student);

	/** Update an existing student */
	tblStudent updateStudent(Long id, tblStudent studentDetails);

	/** Get all students */
	List<tblStudent> getAllStudents();

	/** Get a student by ID */
	Optional<tblStudent> getStudentById(Long id);

	/** Delete a student by ID */
	void deleteStudent(Long id);

	/** Search students by first name (partial, case-insensitive) */
	List<tblStudent> searchByFirstName(String firstName);

	/** Search students by last name (partial, case-insensitive) */
	List<tblStudent> searchByLastName(String lastName);

	/** Find a student by exact email */
	List<tblStudent> findByEmail(String email);
}
