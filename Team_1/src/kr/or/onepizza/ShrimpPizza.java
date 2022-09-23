package kr.or.onepizza;

public class ShrimpPizza extends Pizza {

	public void prepareTopping() { //
		shrimp -= 8; // 1판당 8개
		bacon -= 10; // 1판당 10개
		mushroom -= 3; // 1판당 3개
		mayoneseSauce--; // 1판당 1개
		parsley--; // 1판당 1개
	}

	@Override
	public String toString() {
		return "ShrimpPizza baking 완료";
	}

}