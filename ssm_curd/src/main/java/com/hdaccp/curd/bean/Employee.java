package com.hdaccp.curd.bean;

import javax.validation.constraints.Pattern;

public class Employee {

	@Override
	public String toString() {
		return "Employee [empId=" + empId + ", empName=" + empName
				+ ", gender=" + gender + ", email=" + email + ", dId=" + dId
				+ ", dept=" + dept + "]";
	}

	private Integer empId;
	
	@Pattern(regexp="(^[a-zA-Z0-9_-]{6,16})|(^[\u2E80-\u9FFF]{2,5})$",message="�û���������2-5λ���Ļ���6-16λӢ�ĺ����ֵ����")
    private String empName;

    private String gender;
    
    @Pattern(regexp="^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$",message="�����ʽ����ȷ")
    private String email;

    private Integer dId;
    
    private Department dept;
    public void setDept(Department dept) {
		this.dept = dept;
	}
    public Department getDept() {
		return dept;
	}

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName == null ? null : empName.trim();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender == null ? null : gender.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Integer getdId() {
        return dId;
    }

    public void setdId(Integer dId) {
        this.dId = dId;
    }
}