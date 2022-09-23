package kr.or.onepizza;

abstract public class Pizza extends OwnerSystem {
	OrderSystem odersystem = new OrderSystem();

	// 피자 만드는 함수
	public void createPizza(String pizzaName) {
		prepareDough(); // 도우준비
		prepareCheese(); // 치즈올리기
		prepareTopping(); // 토핑올리기
		prepareSauce(); // 소스뿌리기
		packaging(); // 포장여부에 따라 포장하기
	}

	private void prepareDough() {
		dough--;
	}

	// 피자마다 토핑 재정의
	abstract public void prepareTopping();

	private void packaging() {
		if (odersystem.getTakeOut() == 2) {
			box--;
		}
	}

	private void prepareCheese() {
		cheese--;
	}

	private void prepareSauce() {
		tomatoSauce--;
	}

}