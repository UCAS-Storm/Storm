package cn.ac.ucas.tallybook.model;

import java.util.Date;

public class BuyService {

	private int buyServiceID;
	
	private String tenantID;
	
	private int serviceID;
	
	private double money;
	
	private Date startTime;
	
	private Date endTime;
	
	private String note;

	public int getBuyServiceID() {
		return buyServiceID;
	}

	public void setBuyServiceID(int buyServiceID) {
		this.buyServiceID = buyServiceID;
	}

	public String getTenantID() {
		return tenantID;
	}

	public void setTenantID(String tenantID) {
		this.tenantID = tenantID;
	}

	public int getServiceID() {
		return serviceID;
	}

	public void setServiceID(int serviceID) {
		this.serviceID = serviceID;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
