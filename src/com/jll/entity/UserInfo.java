package com.jll.entity;

public class UserInfo {
	@Override
	public String toString() {
		return "UserInfo [userId=" + userId + ", userName=" + userName + ", userPass=" + userPass + ", userHobby="
				+ userHobby + ", userSex=" + userSex + "]";
	}
	private int userId;
	private String userName;
	private String userPass;
	private String userHobby;
	private String userSex;
	public int getUserId() {
		return userId;
	}
	public String getUserName() {
		return userName;
	}
	public String getUserPass() {
		return userPass;
	}
	public String getUserHobby() {
		return userHobby;
	}
	public String getUserSex() {
		return userSex;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}
	public void setUserHobby(String userHobby) {
		this.userHobby = userHobby;
	}
	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}
}
