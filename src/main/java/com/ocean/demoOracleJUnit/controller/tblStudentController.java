package com.ocean.demoOracleJUnit.controller;

import com.ocean.demoOracleJUnit.model.tblStudent;
import com.ocean.demoOracleJUnit.service.tblStudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class tblStudentController {

	private static final Logger log = LoggerFactory.getLogger(tblStudentController.class);

	private final tblStudentService studentService;

		
	public tblStudentController(tblStudentService studentService) {
		this.studentService = studentService;
	}

	// ===== POST : Create a student =====
	@PostMapping
	public ResponseEntity<tblStudent> createStudent(@RequestBody tblStudent student) {
		log.info("Creating student: {} {}", student.getFirstName(), student.getLastName());
		tblStudent created = studentService.createStudent(student);
		log.info("Student created with ID: {}", created.getStudentId());
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	// ===== GET : Get all students =====
	@GetMapping
	public ResponseEntity<List<tblStudent>> getAllStudents() {
		log.debug("tblStudentController : Fetching all students");
		List<tblStudent> students = studentService.getAllStudents();
		log.info("Returning {} students", students.size());
		return ResponseEntity.ok(students);
	}

	// ===== GET : Get student by ID =====
	@GetMapping("/{id}")
	public ResponseEntity<tblStudent> getStudentById(@PathVariable Long id) {
		log.debug("Fetching student by ID: {}", id);
		return studentService.getStudentById(id)
				.map(student -> {
					log.info("Found student: ID={}", id);
					return ResponseEntity.ok(student);
				})
				.orElseGet(() -> {
					log.warn("Student not found with ID: {}", id);
					return ResponseEntity.notFound().build();
				});
	}

	// ===== PUT : Update student by ID =====
	@PutMapping("/{id}")
	public ResponseEntity<tblStudent> updateStudent(@PathVariable Long id,
													@RequestBody tblStudent studentDetails) {
		log.info("Updating student ID: {}", id);
		try {
			tblStudent updated = studentService.updateStudent(id, studentDetails);
			log.info("Student ID:{} updated successfully", id);
			return ResponseEntity.ok(updated);
		} catch (RuntimeException e) {
			log.warn("Update failed - student not found with ID: {}", id);
			return ResponseEntity.notFound().build();
		}
	}

	// ===== DELETE : Delete student by ID =====
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
		log.info("Deleting student ID: {}", id);
		try {
			studentService.deleteStudent(id);
			log.info("Student ID:{} deleted successfully", id);
			return ResponseEntity.noContent().build();
		} catch (RuntimeException e) {
			log.warn("Delete failed - student not found with ID: {}", id);
			return ResponseEntity.notFound().build();
		}
	}

	// ===== GET : Search by first name =====
	@GetMapping("/search/firstName")
	public ResponseEntity<List<tblStudent>> searchByFirstName(@RequestParam String q) {
		log.debug("Searching by first name: {}", q);
		List<tblStudent> results = studentService.searchByFirstName(q);
		log.info("Found {} students with first name containing '{}'", results.size(), q);
		return ResponseEntity.ok(results);
	}

	// ===== GET : Search by last name =====
	@GetMapping("/search/lastName")
	public ResponseEntity<List<tblStudent>> searchByLastName(@RequestParam String q) {
		log.debug("Searching by last name: {}", q);
		List<tblStudent> results = studentService.searchByLastName(q);
		log.info("Found {} students with last name containing '{}'", results.size(), q);
		return ResponseEntity.ok(results);
	}

	// ===== GET : Find by email =====
	@GetMapping("/byEmail")
	public ResponseEntity<List<tblStudent>> findByEmail(@RequestParam String email) {
		log.debug("Finding student by email: {}", email);
		List<tblStudent> results = studentService.findByEmail(email);
		if (results.isEmpty()) {
			log.warn("No student found with email: {}", email);
		} else {
			log.info("Found {} student(s) with email: {}", results.size(), email);
		}
		return ResponseEntity.ok(results);
	}
}
