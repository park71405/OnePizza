package kr.or.onepizza;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class UserSystem {

	private Map<String, User> userList; // 유저 리스트

	private Scanner scan;

	private String emailId;
	private String password;
	private String phoneNumber;
	private String address;
	private File memberFile;

	public UserSystem() {
		scan = new Scanner(System.in);
		userList = new HashMap<String, User>();
		memberFile = new File("Member.txt");
	}

	// 회원가입
	public void register() {

		String file = "Member.txt";

		email();

		System.out.print("비밀번호를 입력해 주세요 : ");
		password = scan.nextLine();
		System.out.println();

		phoneNumber();

		System.out.print("주소를 입력해 주세요 : ");
		address = scan.nextLine();
		System.out.println();

		User user = new User(emailId, password, phoneNumber, address);

		userList.put(emailId, user);

		System.out.println(user.toString() + "\n");

		FileOutputStream fileOutputStream = null;
		BufferedOutputStream bufferedOutputStream = null;
		ObjectOutputStream objectOutputStream = null;

		try {
			fileOutputStream = new FileOutputStream(file);
			bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
			objectOutputStream = new ObjectOutputStream(bufferedOutputStream);

			objectOutputStream.writeObject(userList);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				objectOutputStream.close();
				bufferedOutputStream.close();
				fileOutputStream.close();
			} catch (Exception e2) {
				System.out.println(e2.getMessage());
			}
		}
	}

	// 이메일 주소 입력
	private void email() {

		if (!memberFile.exists()) {
			while (true) {
				System.out.print("이메일 주소를 입력해 주세요 : ");
				emailId = scan.nextLine();
				System.out.println();
				String regex = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-z]+$";
				if (!Pattern.matches(regex, emailId)) {
					System.out.println("이메일 형식이 잘못되었습니다.\n");
					email();
				}
				return;
			}
		} else {
			String file = "Member.txt";
			FileInputStream fileInputStream = null;
			BufferedInputStream bufferedInputStream = null;
			ObjectInputStream objectInputStream = null;

			try {
				fileInputStream = new FileInputStream(file);
				bufferedInputStream = new BufferedInputStream(fileInputStream);
				objectInputStream = new ObjectInputStream(bufferedInputStream);

				userList = (HashMap) objectInputStream.readObject();

				while (true) {
					System.out.print("이메일 주소를 입력해 주세요 : ");
					emailId = scan.nextLine();
					System.out.println();
					String regex = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-z]+$";
					if (userList.containsKey(emailId)) {
						System.out.println("동일한 아이디가 존재합니다.\n");
						email();
					}
					if (!Pattern.matches(regex, emailId)) {
						System.out.println("이메일 형식이 잘못되었습니다.\n");
						email();
					}
					return;
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			} finally {
				try {
					objectInputStream.close();
					bufferedInputStream.close();
					fileInputStream.close();
				} catch (Exception e2) {
					System.out.println(e2.getMessage());
				}
			}

		}

	}

	// 휴대폰 번호 입력
	private void phoneNumber() {
		while (true) {
			System.out.print("휴대폰 번호를 입력해주세요(- 제외) : ");
			phoneNumber = scan.nextLine();
			System.out.println();
			String regex = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$";
			if (!Pattern.matches(regex, phoneNumber)) {
				System.out.println("휴대폰 번호 형식이 잘못되었습니다.\n");
				phoneNumber();
			}
			return;
		}
	}

	// 로그인
	public boolean signIn() {
		boolean returnValue = true;

		if (!memberFile.exists()) {
			System.out.println("회원가입된 정보가 없습니다.");
			System.out.println("회원가입해 주세요.\n");
			register();
			returnValue = false;
		} else {
			String memberList = "Member.txt";
			FileInputStream fileInputStream = null;
			BufferedInputStream bufferedInputStream = null;
			ObjectInputStream objectInputStream = null;

			try {
				fileInputStream = new FileInputStream(memberList);
				bufferedInputStream = new BufferedInputStream(fileInputStream);
				objectInputStream = new ObjectInputStream(bufferedInputStream);

				userList = (HashMap) objectInputStream.readObject();

				int count = 0;

				while (true) {
					System.out.println("0: 이전");
					System.out.print("이메일 아이디: ");
					String userId = scan.nextLine();
					System.out.println();

					if (userId.equals("0")) {
						returnValue = false;
						break;
					}

					if (count >= 3) {
						System.out.println("로그인 시도 횟수를 초과하였습니다. 회원 가입하시겠습니까?");
						System.out.println("1. 예 | 2. 아니오");
						String select = scan.nextLine();
						if (select.equals("1")) {
							register();
							break;
						} else if (select.equals("2")) {
							returnValue = false; // 틀릴 경우 start()로
							break;
						}
					}
					if (!userList.containsKey(userId)) {
						System.out.println("존재하지 않는 아이디입니다.\n");
						count++;
					} else {
						System.out.print("비밀번호: ");
						String userPassword = scan.nextLine();

						if (!userList.get(userId).getPassword().equals(userPassword)) {
							System.out.println("비밀번호를 확인해주세요\n");
							count++;
						} else {
							System.out.println("로그인에 성공하였습니다.\n");
							returnValue = true; // 로그인 성공 startUser()
							break;
						}
					}
				}
			} catch (Exception e) {
				System.out.println(e);
			} finally {
				try {
					objectInputStream.close();
					bufferedInputStream.close();
					fileInputStream.close();
				} catch (Exception e2) {
					System.out.println(e2);
				}
			}
		}
		return returnValue;
	}

	// 회원 탈퇴
	public boolean removeUser() {

		boolean returnValue = false;
		int count = 0;

		String file = "Member.txt";

		FileInputStream fileInputStream = null;
		BufferedInputStream bufferedInputStream = null;
		ObjectInputStream objectInputStream = null;
		FileOutputStream fileOutputStream = null;
		BufferedOutputStream bufferedOutputStream = null;
		ObjectOutputStream objectOutputStream = null;

		try {
			fileInputStream = new FileInputStream(file);
			bufferedInputStream = new BufferedInputStream(fileInputStream);
			objectInputStream = new ObjectInputStream(bufferedInputStream);
			fileOutputStream = new FileOutputStream(file);
			bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
			objectOutputStream = new ObjectOutputStream(bufferedOutputStream);

			userList = (HashMap<String, User>) objectInputStream.readObject();

			while (true) {
				if (count >= 3) {
					break;
				}
				System.out.print("회원 탈퇴를 하시려면 아이디를 입력해주세요 : ");
				String userId = scan.nextLine();
				System.out.println();
				if (!userList.containsKey(userId)) { // 유저 아이디가 없는 경우
					System.out.println("해당하는 아이디를 찾을 수 없습니다.\n");
					count++;
				} else {
					System.out.print("비밀번호를 입력해 주세요 :");
					String userPassword = scan.nextLine();
					System.out.println();
					if (!userList.get(userId).getPassword().equals(userPassword)) {
						System.out.println("비밀번호를 틀렸습니다.\n");
						count++;
					} else {
						userList.remove(userId);
						System.out.println("회원 탈퇴에 성공하였습니다.\n");
						returnValue = true;
						break;
					}
				}
			}

			objectOutputStream.writeObject(userList);

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				objectOutputStream.close();
				bufferedOutputStream.close();
				fileOutputStream.close();
				objectInputStream.close();
				bufferedInputStream.close();
				fileInputStream.close();
			} catch (Exception e1) {
				System.out.println(e1);
			}
		}
		return returnValue;
	}

}