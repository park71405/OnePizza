package kr.or.onepizza;

import java.io.Serializable;

public class User implements Serializable {

	private String emailId; // 아이디 (이메일 정규표현식)
	private String password; // 비밀번호
	private String phoneNumber; // 전화번호
	private String address; // 주소
	private static final long serialVersionUID = 4341708937646597533L; // UId를 통일화 시키기 위해서

	public User(String emailId, String password, String phoneNumber, String address) {
		super();
		this.emailId = emailId;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.address = address;
	}
	@Override
	public String toString() {
		return "이메일 주소 :" + emailId + ", 비밀번호 :" + password + ", 전화번호 :" + phoneNumber + ", 주소 :" + address;
	}

	public String getEmailId() {
		return emailId;
	}

	public String getPassword() {
		return password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getAddress() {
		return address;
	}
}