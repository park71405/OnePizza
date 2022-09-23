package kr.or.onepizza;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class OwnerSystem {

	private Scanner scan;
	private Map<String, User> userList;
	Calendar openInfo; // 오픈 시간 안내
	Calendar closeInfo; // 마감 시간 안내
	boolean operatingHours; // 오픈 여부 확인

	private File salesFile;
	private File memberFile;

	private int shrimpPizzaPrice = 20000;
	private int sweetPotatoPizzaPrice = 15000;
	private int pepperoniPizzaPrice = 12000;
	private int coke = 1000;
	private int pickle = 500;
	private int hotsauce = 300;

	// 재고 (하루 생산량 총 100판으로 가정했을 때)
	static int box; // 박스
	static int dough; // 도우
	static int tomatoSauce; // 토마토 소스
	static int mustardSauce; // 머스타드 소스
	static int mayoneseSauce; // 마요네즈 소스
	static int cheese; // 치즈
	static int shrimp; // 쉬림프 개수
	static int bacon; // 베이컨 개수
	static int pepperoni; // 페퍼로니 개수
	static int sweetPotato; // 고구마 개수
	static int onion; // 양파 개수
	static int mushroom; // 버섯 개수
	static int sweetCorn; // 옥수수 개수
	static int parsley; // 파슬리 개수

	public OwnerSystem() {
		scan = new Scanner(System.in);
		userList = new HashMap<String, User>();
		openInfo = Calendar.getInstance(Locale.KOREA);
		openInfo.set(Calendar.HOUR_OF_DAY, 11);
		openInfo.set(Calendar.MINUTE, 00);
		openInfo.set(Calendar.SECOND, 00);

		salesFile = new File("TotalSales.txt");
		memberFile = new File("Member.txt");

		closeInfo = Calendar.getInstance(Locale.KOREA);
		closeInfo.set(Calendar.HOUR_OF_DAY, 23);
		closeInfo.set(Calendar.MINUTE, 00);
		closeInfo.set(Calendar.SECOND, 00);
		operatingHours = false;
	}

	// 관리자 초기 화면
	private void start() {

		boolean returnValue = true;

		while (returnValue) {
			System.out.println("가게 오픈 상황 : " + operatingHours + "\n");
			System.out.println("[관리자 메뉴]");
			System.out.println("1. 매출 확인 | 2. 회원 관리 | 3. 오픈/마감 | 4. 재고 확인");
			System.out.println("0. 이전 화면"); // 회원, 관리자 선택하는 화면
			System.out.print("번호를 선택해 주세요: ");
			String select = scan.nextLine();
			System.out.println();
			switch (select) {
			case "1": // 매출 확인
				viewSales();
				break;
			case "2": // 회원 관리
				userManagement();
				break;
			case "3": // 매장 관리 (오픈 마감 시간)
				viewSetTime();
				break;
			case "4": // 재고 확인
				System.out.println(viewStock());
				break;
			case "0":
				System.out.println("이전 화면으로 돌아갑니다.\n");
				returnValue = false;
				break;
			default:
				System.out.println("잘못된 입력값입니다.\n");

			}
		}
	}

	// 매출 및 주문 내역 확인 메뉴
	private void viewSales() {

		boolean check = true;
		while (check) {

			System.out.println("1: 월별 매출 확인 | 2: 일별 매출 확인 | 3: 총 주문 내역 확인");
			System.out.println("0: 이전");
			System.out.print("번호를 선택해 주세요: ");
			String select = scan.nextLine();
			System.out.println();

			switch (select) {
			case "1":
				monthlySales();
				check = false;
				break;
			case "2":
				dailySales();
				check = false;
				break;
			case "3":
				nowViewSales();
				check = false;
				break;
			case "0":
				check = false;
				break;
			default:
				System.out.println("잘못된 입력값입니다.\n");
			}
		}
	}

	// 총 매출 확인
	private void nowViewSales() {
		String filename = "TotalSales.txt";

		FileReader fileReader = null;
		BufferedReader bufferedReader = null;

		if (!salesFile.exists()) {
			System.out.println("매출 내역이 없습니다.\n");
		} else {
			try {
				fileReader = new FileReader(filename);
				bufferedReader = new BufferedReader(fileReader);
				String line = ""; // 라인 단위로 처리
				for (int i = 0; (line = bufferedReader.readLine()) != null; i++) {
					System.out.println(line);
				}

			} catch (Exception e) {
				e.getMessage();
			} finally {
				try {

					bufferedReader.close();
					fileReader.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}

	// 일별 매출 확인
	private void dailySales() {
		System.out.print("보려는 월을 입력해 주세요 : ");
		String month = scan.nextLine();
		System.out.println();
		System.out.print("보려는 일을 입력해 주세요 : ");
		String day = scan.nextLine();
		System.out.println();
		System.out.println();
		int plus = 0;
		String price = null;
		List<String> list = new ArrayList<String>();
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader("TotalSales.txt"));
			String textLine;
			String[] splitLine = new String[15];
			while ((textLine = bufferedReader.readLine()) != null) {
				splitLine = textLine.split("\\s+");

				if (splitLine[9].equals(month + "월") && splitLine[10].equals(day + "일")) {
					if (splitLine[1].equals("피자]")) {
						price = splitLine[5];
						list.add(price);
					} else {
						price = splitLine[4];
						list.add(price);
					}
				} else if (splitLine[8].equals(month + "월") && splitLine[9].equals(day + "일")) {
					if (splitLine[1].equals("피자]")) {
						price = splitLine[5];
						list.add(price);
					} else {
						price = splitLine[4];
						list.add(price);
					}
				} else {
					System.out.println("잘못된 입력값입니다.\n");
				}

			}
			for (String i : list) { // 가격만 출력
				int j = Integer.parseInt(i);
				plus += j;

			}
			System.out.println(plus + "원");

		} catch (Exception e) {
			e.getMessage();
		} finally {
			try {
				bufferedReader.close();
			} catch (Exception e2) {
				e2.getMessage();
			}
		}

	}

	// 월별 매출 확인
	private void monthlySales() {
		System.out.print("보려는 월을 입력해 주세요 : ");
		String month = scan.nextLine();
		int plus = 0;
		String price = null;
		List<String> list = new ArrayList<String>();
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader("TotalSales.txt"));
			String textLine; // 여기 넣을거
			String[] splitLine = new String[15];
			while ((textLine = bufferedReader.readLine()) != null) {
				splitLine = textLine.split("\\s+");

				if (splitLine[9].equals(month + "월")) {
					if (splitLine[1].equals("피자]")) {
						price = splitLine[5];
						list.add(price);
					} else {
						price = splitLine[4];
						list.add(price);
					}
				} else if (splitLine[8].equals(month + "월")) {
					if (splitLine[1].equals("피자]")) {
						price = splitLine[5];
						list.add(price);
					} else {
						price = splitLine[4];
						list.add(price);
					}
				} else {
					System.out.println("잘못된 입력값입니다.");
				}
			}
			for (String i : list) { // 가격만 출력
				int j = Integer.parseInt(i);
				plus += j;

			}
			System.out.println(plus + "원");

		} catch (Exception e) {
			e.getMessage();
		} finally {
			try {
				bufferedReader.close();
			} catch (Exception e2) {
				e2.getMessage();
			}
		}
	}

	// 오픈 시간, 마감 시간 보기
	private void viewSetTime() {
		boolean check = true;
		while (check) {
			System.out.println("1. 오픈/마감 시간 설정 | 2. 오픈/마감 시간 보기 | 3. 오픈하기 | 4. 마감하기");
			System.out.println("0: 이전");
			System.out.print("번호를 선택해 주세요: ");
			String select = scan.nextLine();
			System.out.println();
			switch (select) {
			case "1": // 오픈/마감 시간 안내 설정
				setTime();
				check = false;
				break;
			case "2": // 오픈/마감 시간 안내 보기
				getTime();
				check = false;
				break;
			case "3": // 오픈하기
				open();
				check = false;
				break;
			case "4": // 마감하기
				close();
				check = false;
				break;
			case "0": // 이전
				check = false;
				break;
			default:
				System.out.println("잘못된 입력값입니다.\n");
			}
		}

	}

	// 오픈하기
	private void open() {
		operatingHours = true;
		box = 100; // 박스
		dough = 100; // 도우
		tomatoSauce = 100; // 토마토 소스
		mustardSauce = 100; // 머스타드 소스
		mayoneseSauce = 100; // 마요네즈 소스
		cheese = 100; // 치즈
		shrimp = 800; // 쉬림프 개수
		bacon = 1000; // 베이컨 개수
		pepperoni = 2000; // 페퍼로니 개수
		sweetPotato = 200; // 고구마 개수
		onion = 100; // 양파 개수
		mushroom = 300; // 버섯 개수
		sweetCorn = 100; // 옥수수 개수
		parsley = 100; // 파슬리 개수

		System.out.println("가게 오픈 완료");
		System.out.println("재고 세팅 완료\n");
	}

	// 마감하기
	private void close() {
		operatingHours = false;
		System.out.println("가게 마감 완료\n");
	}

	// 숫자 검증하기
	private boolean inNumeric(String innumeric) {
		return innumeric.replaceAll("[+-]?\\d+", "").equals("") ? true : false;
	}

	// 오픈/마감 시간 설정
	private void setTime() {
		int hour = 0;
		int minute = 0;
		boolean check = true;
		SimpleDateFormat simpleDataFormat = new SimpleDateFormat("hh시 mm분");

		while (check) {
			System.out.println("1. 오픈 시간 설정 | 2. 마감 시간 설정");
			System.out.println("0. 관리자 메인");
			System.out.print("번호를 선택해 주세요: ");
			String select = scan.nextLine();
			System.out.println();

			switch (select) {
			case "1":
				System.out.print("오픈하고자 하는 시간(00~23)을 입력하세요 : ");
				String tempOpenHour = scan.nextLine();
				System.out.println();

				if (inNumeric(tempOpenHour)) {
					hour = Integer.parseInt(tempOpenHour);
					if (hour >= 00 && hour <= 23) {
						openInfo.set(Calendar.HOUR_OF_DAY, hour);
					} else {
						System.out.println("잘못된 입력값입니다.\n");
						break;
					}
				} else {
					System.out.println("잘못된 입력값입니다.\n");
					break;
				}
				System.out.print("오픈하고자 하는 분(00~59)을 입력하세요 : ");
				String tempOpenMinute = scan.nextLine();
				System.out.println();

				if (inNumeric(tempOpenMinute)) {
					minute = Integer.parseInt(tempOpenMinute);
					if (minute >= 00 && minute <= 59) {
						openInfo.set(Calendar.MINUTE, minute);
						openInfo.set(Calendar.SECOND, 00);
					} else {
						System.out.println("잘못된 입력값입니다.\n");
						break;
					}
				} else {
					System.out.println("잘못된 입력값입니다.\n");
					break;
				}

				if (closeInfo.after(openInfo)) {
					System.out.println("오픈 시간이 설정되었습니다.");
					System.out.println(simpleDataFormat.format(openInfo.getTime()));
					check = false;
				} else {
					System.out.println("오픈 시간이 마감 시간보다 느립니다.");
					System.out.println("다시 설정하세요.\n");
					closeInfo.set(Calendar.HOUR_OF_DAY, 11);
					closeInfo.set(Calendar.MINUTE, 00);
					closeInfo.set(Calendar.SECOND, 00);
				}
				break;
			case "2":
				System.out.print("마감하고자 하는 시간(00~24)을 입력하세요 : ");
				String tempCloseHour = scan.nextLine();
				System.out.println();

				if (inNumeric(tempCloseHour)) {
					hour = Integer.parseInt(tempCloseHour);
					if (hour >= 00 && hour <= 24) {
						closeInfo.set(Calendar.HOUR_OF_DAY, hour);
					} else {
						System.out.println("잘못된 입력값입니다.\n");
						break;
					}
				} else {
					System.out.println("잘못된 입력값입니다.\n");
					break;
				}

				System.out.print("마감하고자 하는 분(00~59)을 입력하세요 : ");
				String tempCloseMinute = scan.nextLine();
				System.out.println();

				if (inNumeric(tempCloseMinute)) {
					if (minute >= 00 && minute <= 59) {
						closeInfo.set(Calendar.MINUTE, minute);
						closeInfo.set(Calendar.SECOND, 00);
					} else {
						System.out.println("잘못된 입력값입니다.\n");
						break;
					}
				} else {
					System.out.println("잘못된 입력값입니다.\n");
					break;
				}

				if (openInfo.before(closeInfo)) {
					System.out.println("마감 시간이 설정되었습니다.");
					System.out.println(simpleDataFormat.format(closeInfo.getTime()));
					check = false;
				} else {
					System.out.println("마감 시간이 오픈 시간보다 빠릅니다.");
					System.out.println("다시 설정하세요.\n");
					closeInfo.set(Calendar.HOUR_OF_DAY, 23);
					closeInfo.set(Calendar.MINUTE, 00);
					closeInfo.set(Calendar.SECOND, 00);
				}
				break;
			case "0":
				check = false;
				break;
			default:
				System.out.println("잘못된 입력값입니다.\n");
			}
		}

	}

	// 오픈 시간, 마감 시간 출력
	public void getTime() {
		System.out.println(
				"금일 매장 오픈 시간 안내: " + openInfo.get(Calendar.HOUR_OF_DAY) + "시 " + openInfo.get(Calendar.MINUTE) + "분");
		System.out.println("금일 매장 마감 시간 안내: " + closeInfo.get(Calendar.HOUR_OF_DAY) + "시 "
				+ closeInfo.get(Calendar.MINUTE) + "분\n");
	}

	// 회원 관리 화면
	private void userManagement() {

		boolean check = true;

		while (check) {
			System.out.println("1. 회원 리스트 | 2. 회원 삭제");
			System.out.println("0. 이전 화면");
			System.out.print("번호를 선택해 주세요: ");
			String select = scan.nextLine();
			System.out.println();
			switch (select) {
			case "1":
				viewUserList();
				break;
			case "2":
				removeUser();
				break;
			case "0":
				check = false;
				break;
			default:
				System.out.println("잘못된 입력값입니다.\n");
			}
		}

	}

	// 회원 리스트
	private void viewUserList() {
		String file = "Member.txt";
		FileInputStream fileInputStream = null;
		ObjectInputStream objectInputStream = null;

		if (!memberFile.exists()) {
			System.out.println("회원이 없습니다.\n");
		} else {
			try {
				fileInputStream = new FileInputStream(file);
				objectInputStream = new ObjectInputStream(fileInputStream);

				userList = (HashMap) objectInputStream.readObject();

				Set<String> set = userList.keySet();
				for (String string : set) {
					String emailid = userList.get(string).getEmailId();
					String password = userList.get(string).getPassword();
					String phonenumber = userList.get(string).getPhoneNumber();
					String address = userList.get(string).getAddress();

					System.out.printf("%s\t%s\t%s\t%s\n\n", emailid, password, phonenumber, address);
				}

			} catch (Exception e) {
				System.out.println(e.getMessage());
			} finally {
				try {
					objectInputStream.close();
					fileInputStream.close();
				} catch (Exception e2) {
					e2.getMessage();
				}
			}
		}
	}

	// 회원 삭제
	private void removeUser() {
		String file = "Member.txt";
		if (!memberFile.exists()) {
			System.out.println("회원이 없습니다.\n");
		} else {
			System.out.print("삭제하실 Email-Id를 입력해주세요 : ");
			String emailId = scan.nextLine();
			System.out.println();

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

				userList = (HashMap) objectInputStream.readObject();

				if (userList.containsKey(emailId)) {
					userList.remove(emailId);
					System.out.println(emailId + "-> 해당 아이디는 정상적으로 삭제되었습니다.\n");
				} else {
					System.out.println("해당 아이디가 없습니다. 회원 관리 시스템으로 돌아갑니다.\n");
					viewUserList();
				}

				fileOutputStream = new FileOutputStream(file);
				bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
				objectOutputStream = new ObjectOutputStream(bufferedOutputStream);

				objectOutputStream.writeObject(userList);

			} catch (Exception e) {
				System.out.println(e.getMessage());
			} finally {
				try {
					objectOutputStream.close();
					bufferedOutputStream.close();
					fileOutputStream.close();
					objectInputStream.close();
					bufferedInputStream.close();
					fileInputStream.close();
				} catch (Exception e2) {
					System.out.println(e2.getMessage());
				}
			}
		}

	}

	// 로그인
	public void signIn() {
		Owner owner = new Owner();
		int countId = 0;
		int countPassword = 0;

		while (countId < 3) {
			System.out.println("0: 이전");
			System.out.print("관리자 아이디: ");
			String ownerId = scan.nextLine();
			System.out.println();

			if (ownerId.equals(owner.getId())) { // 제대로 입력한 경우

				// 로그인 횟수 3회 제한
				while (countPassword < 3) {
					System.out.print("관리자 비밀번호: ");
					String ownerPassword = scan.nextLine();
					System.out.println();
					if (ownerPassword.equals(owner.getPassword())) {
						System.out.println("로그인에 성공하였습니다.\n");
						start(); // 관리자용 시작 화면으로 진입
						countPassword = 4;
						countId = 4;
					} else if (ownerPassword.equals("0")) {
						countPassword = 5;
					} else {
						System.out.println("비밀번호를 확인해 주세요.\n");
						countPassword++;
						if (countPassword >= 3) {
							System.out.println("로그인 시도 횟수를 초과하였습니다.");
							System.out.println("이전 화면으로 돌아갑니다.\n");
							countId = 4;
						}
					}
				}
			} else if (ownerId.equals("0")) {
				countId = 5;
			} else {
				System.out.println("존재하지 않는 아이디입니다.\n");
				countId++;
				if (countId >= 3) {
					System.out.println("이전 화면으로 돌아갑니다.\n");
				}
			}
		}
	}

	// 모든 재고 현황 보기
	private String viewStock() {

		return "<모든 재고 현황 보기> \n box =\t\t" + box + "\n dough =\t" + dough + "\n tomatoSauce = \t" + tomatoSauce
				+ "\n cheese =\t" + cheese + "\n shrimp =\t" + shrimp + "\n bacon =\t" + bacon + "\n pepperoni =\t"
				+ pepperoni + "\n sweetPotato =\t" + sweetPotato + "\n onion =\t" + onion + "\n";
	}

	public int getShrimpPizzaPrice() {
		return shrimpPizzaPrice;
	}

	public int getSweetPotatoPizzaPrice() {
		return sweetPotatoPizzaPrice;
	}

	public int getPepperoniPizzaPrice() {
		return pepperoniPizzaPrice;
	}

	public int getCoke() {
		return coke;
	}

	public int getPickle() {
		return pickle;
	}

	public int getHotsauce() {
		return hotsauce;
	}

	public boolean isOperatingHours() {
		return operatingHours;
	}

	public Calendar getOpenInfo() {
		return openInfo;
	}

	public Calendar getCloseInfo() {
		return closeInfo;
	}

}