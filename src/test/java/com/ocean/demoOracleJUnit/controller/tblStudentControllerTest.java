package com.ocean.demoOracleJUnit.controller;

import com.ocean.demoOracleJUnit.model.tblStudent;
import com.ocean.demoOracleJUnit.service.tblStudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(tblStudentController.class)
class tblStudentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private tblStudentService studentService;

	private tblStudent createSampleStudent() {
		tblStudent s = new tblStudent();
		s.setStudentId(1L);
		s.setFirstName("John");
		s.setLastName("Doe");
		s.setEmail("john@example.com");
		s.setEnrolledDate(LocalDate.of(2024, 9, 1));
		return s;
	}

	// ===================== POST /api/students =====================

	@Test
	void createStudent_ShouldReturn201() throws Exception {
		tblStudent input = createSampleStudent();
		input.setStudentId(null);
		tblStudent output = createSampleStudent();

		when(studentService.createStudent(any(tblStudent.class))).thenReturn(output);

		mockMvc.perform(post("/api/students")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(input)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.studentId").value(1))
				.andExpect(jsonPath("$.firstName").value("John"));
	}

	// ===================== GET /api/students =====================

	@Test
	void getAllStudents_ShouldReturn200() throws Exception {
		when(studentService.getAllStudents()).thenReturn(List.of(createSampleStudent()));

		mockMvc.perform(get("/api/students"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(1));
	}

	// ===================== GET /api/students/{id} =====================

	@Test
	void getStudentById_WhenExists_ShouldReturn200() throws Exception {
		when(studentService.getStudentById(1L)).thenReturn(Optional.of(createSampleStudent()));

		mockMvc.perform(get("/api/students/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.studentId").value(1));
	}

	@Test
	void getStudentById_WhenNotExists_ShouldReturn404() throws Exception {
		when(studentService.getStudentById(999L)).thenReturn(Optional.empty());

		mockMvc.perform(get("/api/students/999"))
				.andExpect(status().isNotFound());
	}

	// ===================== PUT /api/students/{id} =====================

	@Test
	void updateStudent_WhenExists_ShouldReturn200() throws Exception {
		tblStudent updated = createSampleStudent();
		updated.setFirstName("Jane");
		updated.setLastName("Smith");

		when(studentService.updateStudent(eq(1L), any(tblStudent.class))).thenReturn(updated);

		mockMvc.perform(put("/api/students/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updated)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.firstName").value("Jane"));
	}

	@Test
	void updateStudent_WhenNotExists_ShouldReturn404() throws Exception {
		when(studentService.updateStudent(eq(99L), any(tblStudent.class)))
				.thenThrow(new RuntimeException("Student not found with id: 99"));

		mockMvc.perform(put("/api/students/99")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new tblStudent())))
				.andExpect(status().isNotFound());
	}

	// ===================== DELETE /api/students/{id} =====================

	@Test
	void deleteStudent_WhenExists_ShouldReturn204() throws Exception {
		mockMvc.perform(delete("/api/students/1"))
				.andExpect(status().isNoContent());
	}

	@Test
	void deleteStudent_WhenNotExists_ShouldReturn404() throws Exception {
		doThrow(new RuntimeException("Student not found with id: 123"))
				.when(studentService).deleteStudent(123L);

		mockMvc.perform(delete("/api/students/123"))
				.andExpect(status().isNotFound());
	}

	// ===================== GET /api/students/search/firstName =====================

	@Test
	void searchByFirstName_ShouldReturn200() throws Exception {
		when(studentService.searchByFirstName("John")).thenReturn(List.of(createSampleStudent()));

		mockMvc.perform(get("/api/students/search/firstName")
						.param("q", "John"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(1));
	}

	// ===================== GET /api/students/search/lastName =====================

	@Test
	void searchByLastName_ShouldReturn200() throws Exception {
		when(studentService.searchByLastName("Doe")).thenReturn(List.of(createSampleStudent()));

		mockMvc.perform(get("/api/students/search/lastName")
						.param("q", "Doe"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(1));
	}

	// ===================== GET /api/students/byEmail =====================

	@Test
	void findByEmail_ShouldReturn200() throws Exception {
		when(studentService.findByEmail("john@example.com")).thenReturn(List.of(createSampleStudent()));

		mockMvc.perform(get("/api/students/byEmail")
						.param("email", "john@example.com"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(1));
	}
}
