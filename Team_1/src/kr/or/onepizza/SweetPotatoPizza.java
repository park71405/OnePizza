package kr.or.onepizza;

public class SweetPotatoPizza extends Pizza {

	public void prepareTopping() { //
		sweetPotato -= 2; // 1판당 2개
		onion--; // 1판당 1개
		mustardSauce--; // 1판당 1개
		sweetCorn--; // 1판당 1개
		parsley--; // 1판당 1개
	}

	@Override
	public String toString() {
		return "SweetPotatoPizza baking 완료";
	}

}