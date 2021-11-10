package controller;

public class LoginCommand {
	
	private String email;
	private String password;
	private boolean rememberEmail;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isRememberEmail() { //게터, 세터 설정에서 boolean 타입의 게터는 is~ 로 시작하는 메서드명이 됨
		return rememberEmail;
	}
	public void setRememberEmail(boolean rememberEmail) {
		this.rememberEmail = rememberEmail;
	}

}
