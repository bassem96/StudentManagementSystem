package com.StudentManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.StudentManagement.entity.Role;
import com.StudentManagement.dao.RoleRepository;

@Service
public class roleServiceImpl implements RoleService {
	
	private RoleRepository roleRepo;
	
	
	public roleServiceImpl() {
	}
	@Autowired
	public roleServiceImpl(RoleRepository theRoleRepository) {
		roleRepo = theRoleRepository;
	}
	
	
	@Override
	public void save(Role theRole) {
		roleRepo.save(theRole);
	}


}
