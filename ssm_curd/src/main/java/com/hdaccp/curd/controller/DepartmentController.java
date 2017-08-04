package com.hdaccp.curd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hdaccp.curd.bean.Department;
import com.hdaccp.curd.bean.Message;
import com.hdaccp.curd.service.DepartmentService;

@Controller
public class DepartmentController {
	@Autowired
	private DepartmentService deptservice;
	
	@RequestMapping("/depts")
	@ResponseBody
	public Message queryDepart(){
		List<Department> dept = deptservice.getQueryDept();
		return Message.success().add("depts", dept);
	}
}
