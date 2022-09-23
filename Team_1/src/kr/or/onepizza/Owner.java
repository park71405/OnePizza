package kr.or.onepizza;

public class Owner {

	private String id; // 관리자 아이디 Admin
	private String password; // 관리자 비밀번호 1234

	public Owner() {
		id = "Admin";
		password = "1234";
	}

	public String getId() {
		return id;
	}

	public String getPassword() {
		return password;
	}
}