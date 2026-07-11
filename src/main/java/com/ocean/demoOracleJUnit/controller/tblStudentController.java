package com.ocean.demoOracleJUnit.controller;

import com.ocean.demoOracleJUnit.model.tblStudent;
import com.ocean.demoOracleJUnit.service.tblStudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class tblStudentController {

	private final tblStudentService studentService;

	public tblStudentController(tblStudentService studentService) {
		this.studentService = studentService;
	}

	// ===== POST : Create a student =====
	@PostMapping
	public ResponseEntity<tblStudent> createStudent(@RequestBody tblStudent student) {
		tblStudent created = studentService.createStudent(student);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	// ===== GET : Get all students =====
	@GetMapping
	public ResponseEntity<List<tblStudent>> getAllStudents() {
		List<tblStudent> students = studentService.getAllStudents();
		return ResponseEntity.ok(students);
	}

	// ===== GET : Get student by ID =====
	@GetMapping("/{id}")
	public ResponseEntity<tblStudent> getStudentById(@PathVariable Long id) {
		return studentService.getStudentById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	// ===== PUT : Update student by ID =====
	@PutMapping("/{id}")
	public ResponseEntity<tblStudent> updateStudent(@PathVariable Long id,
													@RequestBody tblStudent studentDetails) {
		try {
			tblStudent updated = studentService.updateStudent(id, studentDetails);
			return ResponseEntity.ok(updated);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	// ===== DELETE : Delete student by ID =====
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
		try {
			studentService.deleteStudent(id);
			return ResponseEntity.noContent().build();
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	// ===== GET : Search by first name =====
	@GetMapping("/search/firstName")
	public ResponseEntity<List<tblStudent>> searchByFirstName(@RequestParam String q) {
		List<tblStudent> results = studentService.searchByFirstName(q);
		return ResponseEntity.ok(results);
	}

	// ===== GET : Search by last name =====
	@GetMapping("/search/lastName")
	public ResponseEntity<List<tblStudent>> searchByLastName(@RequestParam String q) {
		List<tblStudent> results = studentService.searchByLastName(q);
		return ResponseEntity.ok(results);
	}

	// ===== GET : Find by email =====
	@GetMapping("/byEmail")
	public ResponseEntity<List<tblStudent>> findByEmail(@RequestParam String email) {
		List<tblStudent> results = studentService.findByEmail(email);
		return ResponseEntity.ok(results);
	}
}
