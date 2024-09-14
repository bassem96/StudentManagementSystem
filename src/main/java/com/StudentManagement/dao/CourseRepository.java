package com.StudentManagement.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.StudentManagement.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

}
