package com.trade.dailytrade.model;

public abstract class BaseModel {
	private Double totalSettlementAmount;

	public Double getTotalSettlementAmount() {
		return totalSettlementAmount;
	}

	public void setTotalSettlementAmount(Double totalSettlementAmount) {
		this.totalSettlementAmount = totalSettlementAmount;
	}
	
}
