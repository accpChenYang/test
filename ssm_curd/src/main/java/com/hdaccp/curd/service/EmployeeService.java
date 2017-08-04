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
	 * ��ѯԱ����Ϣ
	 * @return
	 */
	public List<Employee> getEmployee(){
		EmployeeExample ex=new EmployeeExample();
		ex.setOrderByClause("emp_id");
		return emp.selectByExampleWithDept(ex);
	}
	/**
	 * ���
	 * @param emp
	 * @return
	 */
	public int addEmp(Employee emp){
		return this.emp.insertSelective(emp);
	}
	/**
	 * �ж�Ա�����Ƿ��ظ�
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
	 * ����Ա��
	 * @param id
	 * @return
	 */
	public Employee getEmp(Integer id){
		return emp.selectByPrimaryKey(id);
	}
	
	/**
	 * �޸�Ա��
	 * @param emp
	 * @return
	 */
	public int updateEmp(Employee emp){
		return this.emp.updateByPrimaryKeySelective(emp);
	}
	/**
	 * ɾ��Ա��
	 * @param id
	 * @return
	 */
	public int delPrimaryKey(Integer id){
		return this.emp.deleteByPrimaryKey(id);
	}
	/**
	 * ɾ��Ա��
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
