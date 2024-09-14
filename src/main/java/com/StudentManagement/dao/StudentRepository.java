package com.StudentManagement.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.StudentManagement.entity.Student;


@Repository
public interface StudentRepository extends JpaRepository<Student, Long>{
Student findByUsername(String username);
}
