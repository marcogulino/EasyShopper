/**
 * 
 */
package com.google.code.easyshopper.domain;

public enum PriceType {
	ByUnit(0),
	ByWeight(1);
	
	private final int type;
	private PriceType(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
	
	public static PriceType from(int type) {
		for(PriceType priceType: PriceType.values()) {
			if(priceType.type==type) return priceType;
		}
		return ByUnit;
	}
}