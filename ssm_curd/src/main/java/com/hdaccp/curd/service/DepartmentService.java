package com.hdaccp.curd.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdaccp.curd.bean.Department;
import com.hdaccp.curd.dao.DepartmentMapper;

@Service
public class DepartmentService {
	@Autowired
	private DepartmentMapper dept;
	
	public List<Department> getQueryDept(){
		return dept.selectByExample(null);
	}
}
