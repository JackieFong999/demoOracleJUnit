package com.ocean.demoOracleJUnit.repository;

import com.ocean.demoOracleJUnit.model.tblStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface tblStudentRepository extends JpaRepository<tblStudent, Long> {

	// Search by first name (partial, case-insensitive)
	List<tblStudent> findByFirstNameContainingIgnoreCase(String firstName);

	// Search by last name (partial, case-insensitive)
	List<tblStudent> findByLastNameContainingIgnoreCase(String lastName);

	// Find by exact email (case-insensitive)
	List<tblStudent> findByEmailIgnoreCase(String email);
}
