package com.hdaccp.curd.test;

import java.util.List;
import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hdaccp.curd.bean.Department;
import com.hdaccp.curd.bean.Employee;
import com.hdaccp.curd.bean.EmployeeExample;
import com.hdaccp.curd.bean.EmployeeExample.Criteria;
import com.hdaccp.curd.dao.DepartmentMapper;
import com.hdaccp.curd.dao.EmployeeMapper;

/**
 * 测试Dao方法
 * @author Administrator
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class DaoTest {
	
	@Autowired
	EmployeeMapper empdao;
	@Autowired
	DepartmentMapper deptdao;
	@Autowired
	SqlSession sqlsession;
	
	/**
	 * 测试员工查询
	 */
	@org.junit.Test
	public void test(){
		Employee selectByPrimaryKeyWithDept = empdao.selectByPrimaryKeyWithDept(1);
		System.out.println(selectByPrimaryKeyWithDept);
	}
	
	/**
	 * 测试部门查询
	 */
	@org.junit.Test
	public void test1(){
		List<Department> selectByExample = deptdao.selectByExample(null);
		for(Department d:selectByExample){
			System.out.println(d);
		}
	}
	/**
	 * 测试部门添加
	 */
	@org.junit.Test
	public void test2(){
		Department d=new Department();
		d.setDeptName("设计部");
		deptdao.insertSelective(d);
	}
	/**
	 * 批量添加
	 */
	@Test
	public void test3(){
		EmployeeMapper emp = sqlsession.getMapper(EmployeeMapper.class);
		for(int i=0;i<1000;i++){
			Employee e=new Employee();
			String empname=UUID.randomUUID().toString().substring(0, 5)+i;
			e.setEmpName(empname);
			e.setdId(1);
			e.setEmail(empname+"@163.com");
			e.setGender("1");
			emp.insertSelective(e);
		}
	}
}