package kr.or.onepizza;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Cart {

	private Map<String, Integer> cart;
	private Scanner scan;

	public Cart() {
		cart = new HashMap<String, Integer>();
		scan = new Scanner(System.in);
	}

	// 장바구니 보기
	public int viewCart() {
		for (Entry<String, Integer> entry : cart.entrySet()) {
			System.out.println("[" + entry.getKey() + "] " + entry.getValue() + "개");
		}

		return selectCart();
	}

	// 장바구니 출력 화면
	private int selectCart() {
		int returnValue = 0;

		System.out.println("1: 결제 | 2: 수량 변경 | 3. 비우기");
		System.out.println("0: 이전");
		System.out.print("번호를 선택해 주세요 : ");
		String select = scan.nextLine();
		System.out.println();
		Payment payment = new Payment();

		switch (select) {
		case "1":
			if (cart.size() != 0) {
				if (!payment.pay()) { // 결제 실패
					returnValue = 0;
				} else { // 결제 성공 viewOrder();
					returnValue = 1;
				}
			} else {
				System.out.println("먼저 메뉴를 선택해 주세요.\n");
			}
			break;
		case "2":
			selectAmount(); // 수량 변경
			break;
		case "3":
			emptyCart(); // 비우기
			break;
		case "0": // 이전 메뉴
			returnValue = -1;
			break;
		default:
			System.out.println("잘못된 입력값입니다.\n");
		}
		return returnValue;
	}

	// 수량 변경
	private void selectAmount() {
		String changeMenu = null;
		String regex = "^[1-9]$";
		int count = 0;
		if (cart.isEmpty()) {
			System.out.println("지금은 장바구니가 비어있습니다. 메뉴를 선택하고 시도해 주세요.\n");
		}

		while (count < 3) {
			System.out.print("수량을 변경할 메뉴: ");
			changeMenu = scan.nextLine();
			System.out.println();
			if (cart.containsKey(changeMenu)) {
				break;
			} else {
				System.out.println("장바구니에 존재하지 않는 메뉴입니다.\n");
				count++;
			}
		}
		while (count < 3) {
			System.out.print("변경햘 수량 (최대 수량 9개): ");
			String tmpCount = scan.nextLine();
			System.out.println();
			if (Pattern.matches(regex, tmpCount)) {
				int changeMenuCount = Integer.parseInt(tmpCount);
				System.out.println(changeMenu + "의 수량을 " + changeMenuCount + "개로 변경합니다.\n");
				cart.put(changeMenu, changeMenuCount);
				break;
			} else {
				System.out.println("잘못된 입력값입니다.\n");
				count++;
			}
		}

	}

	// 장바구니 비우기
	public void emptyCart() {
		if (cart.isEmpty()) {
			System.out.println("장바구니가 비어있습니다.\n");
		} else {
			System.out.println("장바구니 비우기에 성공했습니다.\n");
		}
		cart.clear();
	}

	public void addCart(String name, int count) {
		cart.put(name, count);
	}

	public Map<String, Integer> getCart() {
		return cart;
	}

	public void setCart(String name, int count) {
		cart.put(name, count);
	}

}