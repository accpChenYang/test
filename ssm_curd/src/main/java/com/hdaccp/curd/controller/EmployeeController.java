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
		List<Integer> ids=new ArrayList<Integer>(Arrays.asList(id));//����תlist
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
	 * ����Ա��
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
	 * �û����Ƿ��ظ�
	 * @param empName
	 * @return
	 */
	@RequestMapping("/chckName")
	@ResponseBody
	public Message checkUser(String empName){
		//У���û����Ƿ���Ϲ淶
		String reg="(^[a-zA-Z0-9_-]{6,16})|(^[\u2E80-\u9FFF]{2,5})$";
		boolean matches = empName.matches(reg);
		if(!matches)
			return Message.error().add("validata_msg", "�û���������2-5λ���Ļ���6-16λӢ�ĺ����ֵ����");
		boolean chckUser = emp.chckUser(empName);
		if(chckUser)
			return Message.success().add("validata_msg", "�û�������");
		return Message.error().add("validata_msg", "�û���������");
	}
	
	/**
	 * Ա����Ϣ
	 * @param pageno ҳ��
	 * @return 
	 */
	@RequestMapping(value="/emp",method=RequestMethod.GET)
	@ResponseBody
	public Message getEmployee(@RequestParam(value="pageno",defaultValue="1")Integer pageno){
		//��ҳ��ѯ���
		PageHelper.startPage(pageno, 5);
		List<Employee> employee = emp.getEmployee();
		//�Խ�����з�װ
		PageInfo<Employee> page=new PageInfo<>(employee,5);
		return Message.success().add("page", page);
	}
	/**
	 * ���
	 * @param emp
	 * @return
	 */
	@RequestMapping(value="/emp",method=RequestMethod.POST)
	@ResponseBody
	//@Valid :���У��
	public Message empAdd(@Valid Employee emp,BindingResult result){
		if(result.hasErrors()){
			Map<String, Object> map=new HashMap<>();
			List<FieldError> fieldErrors = result.getFieldErrors();//���г�����ֶ�
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
