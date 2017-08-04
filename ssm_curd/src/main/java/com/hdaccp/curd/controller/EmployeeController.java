package com.hdaccp.curd.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdaccp.curd.bean.Employee;
import com.hdaccp.curd.bean.Message;
import com.hdaccp.curd.service.EmployeeService;

@Controller
public class EmployeeController {
	@Autowired
	private EmployeeService emp;
	
	@RequestMapping(value="/emp/{id}",method=RequestMethod.DELETE)
	@ResponseBody
	public Message delPrimaryKey(@PathVariable(name="id")Integer[] id){
		List<Integer> ids=new ArrayList<Integer>(Arrays.asList(id));//数组转list
		this.emp.delBathPrimaryKey(ids);
		return Message.success();
	}
	
	@RequestMapping(value="/emp/{empId}",method=RequestMethod.PUT)
	@ResponseBody
	public Message updateEmp(Employee emp){
		this.emp.updateEmp(emp);
		return Message.success();
	}
	
	
	/**
	 * 单个员工
	 * @param empId
	 * @return
	 */
	@RequestMapping(value="/emp/{id}",method=RequestMethod.GET)
	@ResponseBody
	public Message getEmp(@PathVariable(name="id") Integer empId){
		Employee emp = this.emp.getEmp(empId);
		return Message.success().add("emp", emp);
	}
	
	
	/**
	 * 用户名是否重复
	 * @param empName
	 * @return
	 */
	@RequestMapping("/chckName")
	@ResponseBody
	public Message checkUser(String empName){
		//校验用户名是否符合规范
		String reg="(^[a-zA-Z0-9_-]{6,16})|(^[\u2E80-\u9FFF]{2,5})$";
		boolean matches = empName.matches(reg);
		if(!matches)
			return Message.error().add("validata_msg", "用户名必须是2-5位中文或者6-16位英文和数字的组合");
		boolean chckUser = emp.chckUser(empName);
		if(chckUser)
			return Message.success().add("validata_msg", "用户名可用");
		return Message.error().add("validata_msg", "用户名不可用");
	}
	
	/**
	 * 员工信息
	 * @param pageno 页码
	 * @return 
	 */
	@RequestMapping(value="/emp",method=RequestMethod.GET)
	@ResponseBody
	public Message getEmployee(@RequestParam(value="pageno",defaultValue="1")Integer pageno){
		//分页查询插件
		PageHelper.startPage(pageno, 5);
		List<Employee> employee = emp.getEmployee();
		//对结果进行封装
		PageInfo<Employee> page=new PageInfo<>(employee,5);
		return Message.success().add("page", page);
	}
	/**
	 * 添加
	 * @param emp
	 * @return
	 */
	@RequestMapping(value="/emp",method=RequestMethod.POST)
	@ResponseBody
	//@Valid :后端校验
	public Message empAdd(@Valid Employee emp,BindingResult result){
		if(result.hasErrors()){
			Map<String, Object> map=new HashMap<>();
			List<FieldError> fieldErrors = result.getFieldErrors();//所有出错的字段
			for(FieldError error:fieldErrors){
				map.put(error.getField(), error.getDefaultMessage());
			}
			return Message.error().add("message", map);
		}else{
			this.emp.addEmp(emp);
			return Message.success();
		}
		
	}
}
