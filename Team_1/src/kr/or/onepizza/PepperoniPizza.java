package kr.or.onepizza;

public class PepperoniPizza extends Pizza {

	@Override
	public void prepareTopping() { // 페퍼로니 올리기
		pepperoni -= 20; // 1판 당 20개
		parsley--; // 1판당 1개
	}

	@Override
	public String toString() {
		return "PepperoniPizza baking 완료";
	}

}