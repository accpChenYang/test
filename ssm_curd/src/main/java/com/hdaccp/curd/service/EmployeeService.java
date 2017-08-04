package com.hdaccp.curd.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdaccp.curd.bean.Employee;
import com.hdaccp.curd.bean.EmployeeExample;
import com.hdaccp.curd.bean.EmployeeExample.Criteria;
import com.hdaccp.curd.dao.EmployeeMapper;

@Service
public class EmployeeService {
	
	@Autowired
	private EmployeeMapper emp;
	/**
	 * 查询员工信息
	 * @return
	 */
	public List<Employee> getEmployee(){
		EmployeeExample ex=new EmployeeExample();
		ex.setOrderByClause("emp_id");
		return emp.selectByExampleWithDept(ex);
	}
	/**
	 * 添加
	 * @param emp
	 * @return
	 */
	public int addEmp(Employee emp){
		return this.emp.insertSelective(emp);
	}
	/**
	 * 判断员工名是否重复
	 * @param empName
	 * @return
	 */
	public boolean chckUser(String empName) {
		EmployeeExample example=new EmployeeExample();
		Criteria criteria = example.createCriteria();
		criteria.andEmpNameEqualTo(empName);
		long count=this.emp.countByExample(example);
		return count==0;
	}
	/**
	 * 单个员工
	 * @param id
	 * @return
	 */
	public Employee getEmp(Integer id){
		return emp.selectByPrimaryKey(id);
	}
	
	/**
	 * 修改员工
	 * @param emp
	 * @return
	 */
	public int updateEmp(Employee emp){
		return this.emp.updateByPrimaryKeySelective(emp);
	}
	/**
	 * 删除员工
	 * @param id
	 * @return
	 */
	public int delPrimaryKey(Integer id){
		return this.emp.deleteByPrimaryKey(id);
	}
	/**
	 * 删除员工
	 * @param id
	 * @return
	 */
	public int delBathPrimaryKey(List<Integer> id){
		EmployeeExample example=new EmployeeExample();
		Criteria criter = example.createCriteria();
		criter.andEmpIdIn(id);
		return this.emp.deleteByExample(example);
	}
	
}
