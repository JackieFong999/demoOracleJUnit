package com.ocean.demoOracleJUnit.service;

import com.ocean.demoOracleJUnit.model.tblStudent;
import com.ocean.demoOracleJUnit.repository.tblStudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class tblStudentServiceImpl implements tblStudentService {

	private final tblStudentRepository studentRepository;

	public tblStudentServiceImpl(tblStudentRepository studentRepository) {
		this.studentRepository = studentRepository;
	}

	@Override
	public tblStudent createStudent(tblStudent student) {
		return studentRepository.save(student);
	}

	@Override
	public tblStudent updateStudent(Long id, tblStudent studentDetails) {
		tblStudent existing = studentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

		existing.setFirstName(studentDetails.getFirstName());
		existing.setLastName(studentDetails.getLastName());
		existing.setEmail(studentDetails.getEmail());
		existing.setEnrolledDate(studentDetails.getEnrolledDate());

		return studentRepository.save(existing);
	}

	@Override
	@Transactional(readOnly = true)
	public List<tblStudent> getAllStudents() {
		return studentRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<tblStudent> getStudentById(Long id) {
		return studentRepository.findById(id);
	}

	@Override
	public void deleteStudent(Long id) {
		if (!studentRepository.existsById(id)) {
			throw new RuntimeException("Student not found with id: " + id);
		}
		studentRepository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<tblStudent> searchByFirstName(String firstName) {
		return studentRepository.findByFirstNameContainingIgnoreCase(firstName);
	}

	@Override
	@Transactional(readOnly = true)
	public List<tblStudent> searchByLastName(String lastName) {
		return studentRepository.findByLastNameContainingIgnoreCase(lastName);
	}

	@Override
	@Transactional(readOnly = true)
	public List<tblStudent> findByEmail(String email) {
		return studentRepository.findByEmailIgnoreCase(email);
	}
}
