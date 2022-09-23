package kr.or.onepizza;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;

public class PizzaFactory extends OwnerSystem {
	private Queue<Pizza> queue;
	private Queue<Calendar> fQueue; // 피자가 나가야 할 시간
	private Calendar calendar;

	public PizzaFactory() {
		queue = new LinkedList<Pizza>();
		fQueue = new LinkedList<Calendar>();
	}

	// 피자 제작 대기열에 넣기
	public boolean pizzaMap(Map<String, Integer> pizzaList) {

		int time = 3;
		boolean returnTemp = true;

		for (String pizza : pizzaList.keySet()) {
			for (int i = 0; i < pizzaList.get(pizza); i++) {
				// 박스 없을때
				if (box < 1) {
					System.out.println("재고가 부족합니다.");
					returnTemp = false;
					break;
				}
				if (dough < 1 && dough < i) {
					System.out.println("재고가 부족합니다.");
					returnTemp = false;
					break;
				}

				// 기본재료 존재x 피자별 재료x
				if (tomatoSauce > 0 || dough > 0 || cheese > 0) {
					if (pizza.equals("PepperoniPizza") && pepperoni < 1) {
						System.out.println("페퍼로니 피자는 재료 소진으로 인한 품절입니다");
						continue;
					} else if (pizza.equals("SweetPotatoPizza") && (sweetPotato < 1 || onion < 1)) {
						System.out.println("고구마 피자는 재료 소진으로 인한 품절입니다");
						continue;
					} else if (pizza.equals("ShrimpPizza") && (shrimp < 1 || bacon < 1)) {
						System.out.println("쉬림프 피자는 재료 소진으로 인한 품절입니다");
						continue;
					}

					// 피자 제작이 완료된 경우
					if (!queue.isEmpty()) {
						calendar = Calendar.getInstance(Locale.KOREA);
						while (true) {
							if (calendar.after(fQueue.peek())) {
								fQueue.poll();
								queue.poll();

							} else {
								break;
							}
						}
					}

					// 피자 메이킹(만들고 굽고 포장하고)
					Pizza makePizza = getPizza(pizza);
					if (makePizza != null) {
						time += 3;
						makePizza.createPizza(pizza);
						queue.offer(makePizza);

						calendar = Calendar.getInstance();
						calendar.add(Calendar.MINUTE, time);
						fQueue.offer(calendar); // 피자가 피자 제작 대기열에서 나와야하는 시간
						
					}

				} else {
					System.out.println("기본재료 소진으로 인한 전 메뉴 품절상황입니다.\n"); // 기본재료 : 도우, 소스, 치즈
				}
			}
			if (!returnTemp) {
				break;
			}
		}
		if(returnTemp) {
			System.out.println("피자 대기열에 추가, 현재 대기 피자 개수 : " + fQueue.size() + "대기 시간 : " + fQueue.size() * 3 + "분\n");
		}

		return returnTemp;
	}

	// 피자 제작
	private Pizza getPizza(String pizzaName) {
		return "페퍼로니 피자".equals(pizzaName) ? new PepperoniPizza()
				: "고구마 피자".equals(pizzaName) ? new SweetPotatoPizza()
						: "쉬림프 피자".equals(pizzaName) ? new ShrimpPizza() : null;
	}

	// 남은 대기시간 출력
	public void timePrint() {
		int time = fQueue.size() * 10; // 10초
		System.out.println("피자 대기시간은 : " + time + "분 입니다.\n"); // 피자 받기까지 남은 시간 계산
	}

}