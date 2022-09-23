package kr.or.onepizza;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class OrderSystem {

	private boolean login; // 로그인 여부 T/F
	private String request;
	private Scanner scan;
	private Cart cart;
	private PizzaFactory pizzaFactory;
	private int takeOut;
	private Calendar calendar;
	private int totalPrice;

	public OrderSystem() {
		scan = new Scanner(System.in);
		cart = new Cart();
		pizzaFactory = new PizzaFactory();
		takeOut = -1;
		calendar = null;
		totalPrice = 0;
	}

	// 초기 화면
	public void start() {
		System.out.println("[*** OnePizza ***]");
		System.out.println("안녕하세요. 세계 정상을 노리는 OnePizza입니다.");
		UserSystem userSystem = new UserSystem();
		while (true) {
			System.out.println("1: 메뉴 확인 | 2: 로그인 | 3. 회원 가입");
			System.out.println("0: 프로그램 종료");
			System.out.print("번호를 선택해 주세요 : ");
			String select = scan.nextLine();
			System.out.println();
			switch (select) {
			case "1": // 메뉴 확인
				viewMenu();
				break;
			case "2": // 로그인
				System.out.println("1: 회원 | 2: 관리자");
				System.out.println("0: 이전");
				System.out.print("번호를 선택해주세요 : ");
				String select1 = scan.nextLine();
				System.out.println();
				switch (select1) {
				case "1":
					login = userSystem.signIn();
					if (!login) {
						start();
					} else {
						startUser();
					}
					break;
				case "2":
					pizzaFactory.signIn();
					break;
				case "0": // 이전
					break;
				default:
					System.out.println("잘못된 입력값입니다.\n");
				}
				break;
			case "3": // 회원가입
				userSystem.register();
				break;
			case "0":
				System.out.println("감사합니다. 또 방문해주세요~");
				System.exit(0);
				break;
			default:
				System.out.println("잘못된 입력값입니다.\n");
			}
		}
	}

	// 회원용 초기 화면
	private void startUser() {
		Calendar nowTime = Calendar.getInstance(Locale.KOREA);
		System.out.println("세계 정상을 향해 함께 가는 여러분 반갑습니다~!");
		pizzaFactory.getTime();
		System.out.println();
		UserSystem userSystem = new UserSystem();
		while (true) {
			System.out.println("[*** Main Home ***]");
			System.out.println("1. 메뉴 | 2. 장바구니 | 3. 로그아웃 | 4. 회원 탈퇴");
			System.out.print("번호를 선택해주세요 : ");
			String select = scan.nextLine();
			System.out.println();
			switch (select) {
			case "1":
				int checkNumber = viewMenu(); // 메뉴 확인
				if (checkNumber == 0) {
					getRequest();
				}
				break;
			case "2": // 장바구니
				while (true) {
					if (pizzaFactory.dough > 0 && nowTime.after(pizzaFactory.getOpenInfo())
							&& nowTime.before(pizzaFactory.getCloseInfo()) && pizzaFactory.operatingHours) {
						int select1 = cart.viewCart();
						if (select1 == 1) { // 결제 성공
							if (pizzaFactory.pizzaMap(cart.getCart())) {
								System.out.println("결제가 완료되었습니다.\n");
								viewOrder(); // 결제 내역 보여주기 + txt 파일로 저장하기
							}
							cart.emptyCart();
							break;
						} else if (select1 == -1) {
							break;
						}
					} else {
						System.out.println("매장이 마감되었습니다.");
						System.out.println("다음에 다시 방문해 주세요.");
						cart.emptyCart();
						break;
					}
				}
				break;

			case "3":
				login = false; // 로그아웃
				start();
				break;

			case "4":
				if (userSystem.removeUser()) { // 회원 탈퇴 성공
					start();
				}
				break;
			default:
				System.out.println("잘못된 입력값입니다.");
				break;
			}
		}

	}

	// 1. 메뉴 확인
	private int viewMenu() {
		int returnNumber = 0;

		while (true) {
			if (login) {
				System.out.println("[메뉴]");
			}
			System.out.println("1. 페퍼로니 피자 | 2. 고구마 피자 | 3. 쉬림프 피자 ");
			System.out.println("0. 메인 화면");
			if (login) {
				System.out.print("번호를 선택해주세요 : ");
			}
			if (!login) {
				System.out.println("주문은 회원 가입 후 이용해 주세요.\n");
				start();
			} else {
				returnNumber = selectMenu();
				break;
			}
		}

		return returnNumber;
	}

	// 메뉴 선택
	private int selectMenu() {

		int returnNumber = 0;
		String name = null;
		int tempCount = 0;

		while (tempCount == 0) {
			String select = scan.nextLine();
			System.out.println();

			switch (select) {
			case "1":
				name = "페퍼로니 피자";
				tempCount = 1;
				break;
			case "2":
				name = "고구마 피자";
				tempCount = 1;
				break;
			case "3":
				name = "쉬림프 피자";
				tempCount = 1;
				break;
			case "0":
				startUser();
				break;
			default:
				System.out.println("잘못된 입력값입니다.");
				System.out.print("다시 입력해 주세요 : ");
			}
		}

		String regex = "^[1-9]$";
		int count = 0;
		int countTry = 0;

		while (countTry < 3) {
			System.out.print("수량을 입력해 주세요 (최대 수량 9개) : ");
			String tmpCount = scan.nextLine();
			System.out.println();

			if (Pattern.matches(regex, tmpCount)) { // 제대로 입력한 경우
				count = Integer.parseInt(tmpCount);
				countTry = 4;

				if (!(pizzaFactory.dough >= count)) { // 주문하려는 피자 양이 현재 재고량보다 많은 경우
					System.out.println("죄송합니다. 재고가 부족합니다.");
					countTry = 5;
					returnNumber = 1;
				}
			} else {
				System.out.println("잘못된 입력값입니다.\n");
				countTry++;
			}
		}
		if (countTry == 4) { // 제대로된 수량을 입력한 경우에만 장바구니에 넣기
			Map<String, Integer> tmpCart = cart.getCart();
			if (!tmpCart.containsKey(name)) {
				cart.addCart(name, count);
			} else {
				count += tmpCart.get(name);
				cart.setCart(name, count);
			}
			viewAddMenu();
		} else if (countTry == 5) { // 재고가 부족한 경우
			
		} else {
			System.out.println("잘못된 입력값입니다.\n");
		}

		return returnNumber;
	}

	// 추가 메뉴 확인
	private void viewAddMenu() {
		System.out.println("[추가 메뉴 선택]");
		System.out.println("1: 콜라 | 2: 피클 | 3. 핫소스 | 4. 추가 없음");
		System.out.println("0: 메인 화면");
		System.out.print("번호를 선택해 주세요 : ");
		selectAddMenu();
	}

	private void selectAddMenu() { // 추가 메뉴 선택
		String select = scan.nextLine();
		System.out.println();
		boolean check = true;
		String name = null;

		while (check) {
			switch (select) {
			case "1":
				name = "콜라";
				break;
			case "2":
				name = "피클";
				break;
			case "3":
				name = "핫소스";
				break;
			case "4":
				check = false;
				break;
			case "0":
				startUser();
				break;
			default:
				System.out.println("잘못된 입력값입니다.");
				System.out.println("다시 입력해 주세요.\n");
				System.out.println("1: 콜라 | 2: 피클 | 3. 핫소스 | 4. 추가 없음");
				System.out.println("0: 메인 화면");
				System.out.print("번호를 선택해 주세요 : ");
				selectAddMenu();
			}
			int count = 0;
			int countTry = 0;

			while (countTry < 3) {
				if (check) {
					System.out.print("수량을 입력해 주세요 (최대 수량 9개) : ");
					String tmp_count = scan.nextLine();
					System.out.println();

					String regex = "^[1-9]$";

					if (Pattern.matches(regex, tmp_count)) {
						count = Integer.parseInt(tmp_count);

						Map<String, Integer> tmpCart = cart.getCart();
						if (!tmpCart.containsKey(name)) {
							cart.addCart(name, count);
						} else {
							count += tmpCart.get(name);
							cart.setCart(name, count);
						}
						check = false;
						countTry = 4;
					} else {
						System.out.println("잘못된 입력값입니다.\n");
						countTry++;
					}

				} else {
					break;
				}
			}
		}
		while (true) {
			System.out.println("추가 메뉴를 더 선택하시겠습니까?");
			System.out.println("1: 네 | 2: 아니오");
			System.out.print("번호를 선택해 주세요 : ");
			String select2 = scan.nextLine();
			System.out.println();

			if (select2.equals("1")) {
				viewAddMenu();
				break;
			} else if (select2.equals("2")) {
				check = false;
				break;
			} else {
				System.out.println("잘못된 입력값입니다.\n");
			}
		}
	}

	// 요구사항
	private void getRequest() {
		boolean check = true;
		while (check) {
			System.out.print("요구사항을 입력해주세요(최대 100자) : ");
			request = scan.nextLine();
			System.out.println();
			String regex = "^.*{0,100}$";
			if (request.matches(regex)) {
				takeOut = viewTakeOut();
				check = false;
			} else {
				System.out.println("잘못된 입력값입니다.\n");
			}
		}

	}

	// 테이크 아웃 여부 확인
	private int viewTakeOut() {

		System.out.println("식사하실 장소를 선택해주세요.");
		System.out.println("1. 매장에서 식사 | 2. 테이크 아웃");
		System.out.print("번호를 선택해주세요: ");

		boolean check = true;
		int count = 0;

		while (check) {
			String select = scan.nextLine();
			System.out.println();
			switch (select) {
			case "1":
				System.out.println("[매장에서 식사]를 선택하였습니다.\n");
				count = 1;
				check = false;
				break;
			case "2":
				System.out.println("[테이크 아웃]을 선택하였습니다.\n");
				count = 2;
				pizzaFactory.box--; // 포장 박스 재고량 줄이기
				check = false;
				break;
			default:
				System.out.println("잘못된 입력값입니다.");
				System.out.print("번호를 선택해주세요: ");
			}
		}

		System.out.println("장바구니에서 결제를 완료해주세요.\n");
		return count;
	}

	// 주문 내역 확인 + 저장
	private void viewOrder() {
		Map<String, Integer> tempCart = cart.getCart();

		int price = 0;
		String salesFilename = "TotalSales.txt";
		String priceFilename = "PriceSales.txt";
		calendar = Calendar.getInstance(Locale.KOREA);

		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;
		FileWriter fileWriter1 = null;
		BufferedWriter bufferedWriter1 = null;

		System.out.println("[주문내역]");

		for (Entry<String, Integer> entry : tempCart.entrySet()) {
			switch (entry.getKey()) {
			case "쉬림프 피자":
				price = entry.getValue() * pizzaFactory.getShrimpPizzaPrice();
				break;
			case "고구마 피자":
				price = entry.getValue() * pizzaFactory.getSweetPotatoPizzaPrice();
				break;
			case "페퍼로니 피자":
				price = entry.getValue() * pizzaFactory.getPepperoniPizzaPrice();
				break;
			case "콜라":
				price = entry.getValue() * pizzaFactory.getCoke();
				break;
			case "피클":
				price = entry.getValue() * pizzaFactory.getPickle();
				break;
			case "핫소스":
				price = entry.getValue() * pizzaFactory.getHotsauce();
				break;
			}
			String priceOutPut = "[" + entry.getKey() + "] " + entry.getValue() + "개, 가격 : " + price + " / " + "주문시간 : "
					+ (calendar.get(Calendar.MONTH) + 1) + "월 " + (calendar.get(Calendar.DAY_OF_MONTH)) + "일 - "
					+ (calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":"
							+ calendar.get(Calendar.SECOND));
			System.out.println(priceOutPut + "\n");
			totalPrice += price;
			String totalPriceString = String.valueOf(totalPrice);

			try {
				fileWriter = new FileWriter(salesFilename, true);
				bufferedWriter = new BufferedWriter(fileWriter);

				fileWriter1 = new FileWriter(priceFilename, false);
				bufferedWriter1 = new BufferedWriter(fileWriter1);

				bufferedWriter.write(priceOutPut);
				bufferedWriter1.write(totalPriceString);

				bufferedWriter.newLine();
				bufferedWriter1.newLine();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					bufferedWriter.close();
					fileWriter.close();
					bufferedWriter1.close();
					fileWriter.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}

		}

	}

	public int getTakeOut() {
		return takeOut;
	}

}