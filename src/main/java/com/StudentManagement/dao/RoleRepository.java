package com.StudentManagement.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.StudentManagement.entity.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role, String>{

}
