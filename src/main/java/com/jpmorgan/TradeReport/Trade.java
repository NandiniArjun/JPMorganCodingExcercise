package com.jpmorgan.TradeReport;

import java.math.BigDecimal;
import java.util.Date;

public class Trade {
	
	private String entity;
	private char type;
	private double agreedFx;
	private String currency;
	private Date instructionDate;
	private Date settlementDate;
	private int units;
	private double price;
	
	private BigDecimal incAmount = BigDecimal.ZERO;
	private BigDecimal outAmount = BigDecimal.ZERO;
	
	public Trade() {
		// this empty constructor is required by Super CSV
	}
	
	public Trade(String entity, char type, double agreedFx, String currency,
            Date instructionDate, Date settlementDate, int units, double price) {
        this.entity = entity;
        this.type = type;
        this.agreedFx = agreedFx;
        this.currency = currency;
        this.instructionDate = instructionDate;
        this.settlementDate = settlementDate;
        this.units = units;
        this.price = price;
    }

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public void setType(char type) {
		this.type = type;
	}

	public void setAgreedFx(double agreedFx) {
		this.agreedFx = agreedFx;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void setInstructionDate(Date instructionDate) {
		this.instructionDate = instructionDate;
	}

	public void setSettlementDate(Date settlementDate) {
		this.settlementDate = settlementDate;
	}

	public void setUnits(int units) {
		this.units = units;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	String getEntity() {
		return entity;
	}
	char getType() {
		return type;
	}
	double getAgreedFx() {
		return agreedFx;
	}
	String getCurrency() {
		return currency;
	}
	Date getInstructionDate() {
		return instructionDate;
	}
	Date getSettlementDate() {
		return settlementDate;
	}
	int getUnits() {
		return units;
	}
	double getPrice() {
		return price;
	}
	
	BigDecimal getIncAmount() {
		return incAmount;
	}

	void setIncAmount(BigDecimal incAmount) {
		this.incAmount = incAmount;
	}

	BigDecimal getOutAmount() {
		return outAmount;
	}

	void setOutAmount(BigDecimal outAmount) {
		this.outAmount = outAmount;
	}

}
