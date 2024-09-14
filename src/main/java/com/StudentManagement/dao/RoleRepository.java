package com.StudentManagement.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.StudentManagement.entity.Role;

/*used for authentication to show some view based on role (not implemented)*/
@Repository
public interface RoleRepository extends JpaRepository<Role, String>{

}
