package com.trade.dailytrade.common;

public enum TradeFields {
	ENTITY(1),TRADETYPE(2),FX(3),CURRENCY(4),INSTRUCTION_DATE(5),SETTLEMENT_DATE(6),UNITS(7),PRICE(8),
	ASYNC(100),SYNC(101);
	
	private Integer counter;
	private TradeFields(Integer counter) {
		this.counter = counter;
	}
	public Integer getCounter() {
		return counter;
	}
	
}
