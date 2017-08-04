package com.hdaccp.curd.test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
/**
 * 测试 mvc的请求
 * @author Administrator
 *
 */
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.github.pagehelper.PageInfo;
import com.hdaccp.curd.bean.Employee;
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:applicationContext.xml","classpath:springmvc.xml"})
public class MvcTest {
	
	@Autowired
	WebApplicationContext context;
	
	MockMvc mockmvc;
	
	@Before
	public void init(){
		mockmvc=MockMvcBuilders.webAppContextSetup(context).build();
	}
	/**
	 * 测试员工分页信息
	 * @throws Exception 
	 */
	@Test
	public void testPage() throws Exception{
		MvcResult result = mockmvc.perform(MockMvcRequestBuilders.get("/emp").param("pageno", "1"))
			.andReturn();
		
		PageInfo<Employee> page = (PageInfo<Employee>) result.getRequest().getAttribute("page");
		System.out.println("当前页码："+page.getPageNum());
		System.out.println("总记录数:"+page.getTotal());
		System.out.println("总页码:"+page.getPages());
		System.out.println("上一页:"+page.getPrePage());
		System.out.print("连续显示的页码:");
		int[] pages = page.getNavigatepageNums();
		for(int p:pages){
			System.out.print(p+",");
		}
		System.out.println();
		List<Employee> list = page.getList();
		for(Employee e:list){
			System.out.println(e);
		}
	}
}
