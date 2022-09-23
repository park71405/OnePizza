package kr.or.onepizza;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Payment {

	private Scanner scan;
	private String cardNumber;
	private String cardPassword;

	public Payment() {
		scan = new Scanner(System.in);
	}

	// 카드 번호 확인
	private boolean checkCardNumber() {
		System.out.print("카드 번호를 입력해주세요 : ");
		cardNumber = scan.nextLine();
		System.out.println();
		String patternCardNumber = "^\\d{4}-?\\d{4}-?\\d{4}-?\\d{4}$";
		boolean matchCardNumber = Pattern.matches(patternCardNumber, cardNumber);

		return matchCardNumber;
	}

	// 카드 비밀번호 확인
	private boolean checkCardPassword() {
		System.out.print("비밀번호를 입력해주세요 : ");
		cardPassword = scan.nextLine();
		System.out.println();
		String patternCardPassword = "^\\d{4}";
		boolean matchCardPassword = Pattern.matches(patternCardPassword, cardPassword);

		return matchCardPassword;
	}

	// 결제
	public boolean pay() {
		boolean returnValue = false;
		System.out.println("1. 카드 결제 | 2. 돌아가기");
		System.out.print("번호를 선택해주세요 : ");
		String select = scan.nextLine();
		System.out.println();

		int count = 0;

		switch (select) {
		case "1":
			while (true) {
				if (count >= 3) {
					System.out.println("결제정보가 올바르지 않습니다. 이전화면으로 돌아갑니다.\n");
					returnValue = false; // 결제 실패 viewCart();
					break;
				}
				if (checkCardNumber() == true) {
					if (checkCardPassword() == true) {
						returnValue = true; // 결제 성공
						break;
					} else {
						System.out.println("비밀번호가 올바르지 않습니다.\n");
						count++;
					}
				} else {
					System.out.println("카드 번호가 올바르지 않습니다.\n");
					count++;
				}
			}
			break;
		case "2":
			returnValue = false; // 결제 안함 viewCart();
			break;
		}
		return returnValue;
	}

}