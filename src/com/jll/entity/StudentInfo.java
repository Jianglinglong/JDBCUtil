package com.jll.entity;

import java.util.Date;

public class StudentInfo {
	private int stuId;
	private String stuName;
	private String stuPass;
	private String stuSex;
	private int stuAge;
	private Date stuBirth;
	
	public StudentInfo() {
		super();
	}
	
	public StudentInfo(int stuID) {
		super();
		this.stuId = stuID;
	}
	public StudentInfo(String stuName, String stuPass, String stuSex, int stuAge, Date stuBirth) {
		super();
		this.stuName = stuName;
		this.stuPass = stuPass;
		this.stuSex = stuSex;
		this.stuAge = stuAge;
		this.stuBirth = stuBirth;
	}
	public Date getStuBirth() {
		return stuBirth;
	}
	public void setStuBirth(Date stuBirth) {
		this.stuBirth = stuBirth;
	}

	public int getStuId() {
		return stuId;
	}
	
	public String getStuName() {
		return stuName;
	}
	
	public String getStuPass() {
		return stuPass;
	}
	
	public String getStuSex() {
		return stuSex;
	}
	
	public int getStuAge() {
		return stuAge;
	}
	public void setStuId(int stuId) {
		this.stuId = stuId;
	}
	public void setStuName(String stuName) {
		this.stuName = stuName;
	}
	public void setStuPass(String stuPass) {
		this.stuPass = stuPass;
	}
	public void setStuSex(String stuSex) {
		this.stuSex = stuSex;
	}
	public void setStuAge(int stuAge) {
		this.stuAge = stuAge;
	}
	
	
}
