package com.trade.dailytrade.model;

import java.util.Date;

public class TradeModel extends BaseModel{
	private String entityName;
	private String tradeType;
	private Double agreedFx;
	private String currency;
	private Date instructionDate;
	private Date settlementDate;
	private Integer units;
	private Double unitPrice;
	private Double tradeAmount;
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public Double getAgreedFx() {
		return agreedFx;
	}
	public void setAgreedFx(Double agreedFx) {
		this.agreedFx = agreedFx;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Date getInstructionDate() {
		return instructionDate;
	}
	public void setInstructionDate(Date instructionDate) {
		this.instructionDate = instructionDate;
	}
	public Date getSettlementDate() {
		return settlementDate;
	}
	public void setSettlementDate(Date settlementDate) {
		this.settlementDate = settlementDate;
	}
	public Integer getUnits() {
		return units;
	}
	public void setUnits(Integer units) {
		this.units = units;
	}
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Double getTradeAmount() {
		return tradeAmount;
	}
	public void setTradeAmount(Double tradeAmount) {
		this.tradeAmount = tradeAmount;
	}
	@Override
	public String toString() {
		return this.getEntityName() + "\t" + this.getAgreedFx().toString() + "\t" + this.getCurrency() + "\t"
				+ this.getInstructionDate() + "\t" + this.getSettlementDate() + "\t" + this.getUnits().toString() + "\t"
				+ this.getUnitPrice().toString();
	}

}
