package com.ocean.demoOracleJUnit.service;

import com.ocean.demoOracleJUnit.model.tblStudent;
import com.ocean.demoOracleJUnit.repository.tblStudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class tblStudentServiceTest {

	@Mock
	private tblStudentRepository studentRepository;

	@InjectMocks
	private tblStudentServiceImpl studentService;

	private tblStudent createSampleStudent() {
		tblStudent s = new tblStudent();
		s.setFirstName("John");
		s.setLastName("Doe");
		s.setEmail("john@example.com");
		s.setEnrolledDate(LocalDate.of(2024, 9, 1));
		return s;
	}

	// ===================== createStudent =====================

	@Test
	void createStudent_ShouldReturnSavedStudent() {
		tblStudent input = createSampleStudent();
		tblStudent saved = createSampleStudent();
		saved.setStudentId(100L);

		when(studentRepository.save(any(tblStudent.class))).thenReturn(saved);

		tblStudent result = studentService.createStudent(input);

		assertThat(result).isNotNull();
		assertThat(result.getStudentId()).isEqualTo(100L);
		assertThat(result.getFirstName()).isEqualTo("John");
		verify(studentRepository).save(input);
	}

	// ===================== getAllStudents =====================

	@Test
	void getAllStudents_ShouldReturnList() {
		when(studentRepository.findAll()).thenReturn(List.of(createSampleStudent()));

		List<tblStudent> result = studentService.getAllStudents();

		assertThat(result).hasSize(1);
		verify(studentRepository).findAll();
	}

	// ===================== getStudentById =====================

	@Test
	void getStudentById_WhenExists_ShouldReturnStudent() {
		tblStudent student = createSampleStudent();
		student.setStudentId(1L);
		when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

		Optional<tblStudent> result = studentService.getStudentById(1L);

		assertThat(result).isPresent();
		assertThat(result.get().getStudentId()).isEqualTo(1L);
	}

	@Test
	void getStudentById_WhenNotExists_ShouldReturnEmpty() {
		when(studentRepository.findById(999L)).thenReturn(Optional.empty());

		Optional<tblStudent> result = studentService.getStudentById(999L);

		assertThat(result).isEmpty();
	}

	// ===================== updateStudent =====================

	@Test
	void updateStudent_WhenExists_ShouldReturnUpdatedStudent() {
		tblStudent existing = createSampleStudent();
		existing.setStudentId(1L);

		tblStudent updates = new tblStudent();
		updates.setFirstName("Jane");
		updates.setLastName("Smith");
		updates.setEmail("jane@example.com");
		updates.setEnrolledDate(LocalDate.of(2025, 3, 15));

		when(studentRepository.findById(1L)).thenReturn(Optional.of(existing));
		when(studentRepository.save(any(tblStudent.class))).thenAnswer(invocation -> invocation.getArgument(0));

		tblStudent result = studentService.updateStudent(1L, updates);

		assertThat(result.getFirstName()).isEqualTo("Jane");
		assertThat(result.getLastName()).isEqualTo("Smith");
		assertThat(result.getEmail()).isEqualTo("jane@example.com");
		assertThat(result.getEnrolledDate()).isEqualTo(LocalDate.of(2025, 3, 15));
	}

	@Test
	void updateStudent_WhenNotExists_ShouldThrowException() {
		when(studentRepository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> studentService.updateStudent(99L, new tblStudent()))
				.isInstanceOf(RuntimeException.class)
				.hasMessageContaining("Student not found with id: 99");
	}

	// ===================== deleteStudent =====================

	@Test
	void deleteStudent_WhenExists_ShouldDelete() {
		when(studentRepository.existsById(1L)).thenReturn(true);

		studentService.deleteStudent(1L);

		verify(studentRepository).deleteById(1L);
	}

	@Test
	void deleteStudent_WhenNotExists_ShouldThrowException() {
		when(studentRepository.existsById(99L)).thenReturn(false);

		assertThatThrownBy(() -> studentService.deleteStudent(99L))
				.isInstanceOf(RuntimeException.class)
				.hasMessageContaining("Student not found with id: 99");

		verify(studentRepository, never()).deleteById(any());
	}

	// ===================== searchByFirstName =====================

	@Test
	void searchByFirstName_ShouldReturnMatches() {
		when(studentRepository.findByFirstNameContainingIgnoreCase("John"))
				.thenReturn(List.of(createSampleStudent()));

		List<tblStudent> result = studentService.searchByFirstName("John");

		assertThat(result).hasSize(1);
	}

	// ===================== searchByLastName =====================

	@Test
	void searchByLastName_ShouldReturnMatches() {
		when(studentRepository.findByLastNameContainingIgnoreCase("Doe"))
				.thenReturn(List.of(createSampleStudent()));

		List<tblStudent> result = studentService.searchByLastName("Doe");

		assertThat(result).hasSize(1);
	}

	// ===================== findByEmail =====================

	@Test
	void findByEmail_ShouldReturnMatches() {
		when(studentRepository.findByEmailIgnoreCase("john@example.com"))
				.thenReturn(List.of(createSampleStudent()));

		List<tblStudent> result = studentService.findByEmail("john@example.com");

		assertThat(result).hasSize(1);
	}
}
